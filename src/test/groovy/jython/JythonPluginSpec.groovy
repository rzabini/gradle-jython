/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jython

import nebula.test.ProjectSpec
import org.gradle.api.logging.Logger

class JythonPluginSpec extends ProjectSpec {

    private static final String PLUGIN = 'com.github.rzabini.gradle-jython'
    Logger testLogger = Mock(Logger)

    def 'apply does not throw exceptions'() {
        when:
        project.apply plugin: PLUGIN

        then:
        noExceptionThrown()
    }

    def 'apply is idempotent'() {
        when:
        project.apply plugin: PLUGIN
        project.apply plugin: PLUGIN

        then:
        noExceptionThrown()
    }

    def "can download existing package"(){
        when:
        project.with {
            apply plugin: PLUGIN
            jython {
                pypackage 'docutils:0.12'
            }
        }

        then:
        project.file("${project.buildDir}/jython/docutils-0.12.tar.gz").isFile()
    }

    def "do not download package more than once"(){
        when:
        project.with {
            apply plugin: PLUGIN
            jython.logger = testLogger
            jython {
                pypackage 'docutils:0.12'
                pypackage 'docutils:0.12'
            }
        }

        then:
        project.file("${project.buildDir}/jython/docutils-0.12.tar.gz").isFile()

        1 * testLogger._({it =~ 'downloading https://files.pythonhosted.org/packages/.*/docutils-0.12.tar.gz#sha256=c7db717810ab6965f66c8cf0398a98c9d8df982da39b4cd7f162911eb89596fa'})
        1 * testLogger._('Skipping existing file docutils-0.12.tar.gz')
    }

    def "fail when package does not exist"() {
        when:
        project.with {
            apply plugin: PLUGIN
            jython {
                pypackage 'docutils:0.13'
            }
        }

        then:
        IllegalStateException ex = thrown()
        ex.message == 'Could not download file'
    }

    def "can download existing package ignoring case in package name"(){
        when:
            project.with {
                apply plugin: PLUGIN
                jython {
                    pypackage 'sqlalchemy:0.8.5'
                }
            }

        then:
            project.file("${project.buildDir}/jython/SQLAlchemy-0.8.5.tar.gz").isFile()
    }


}
