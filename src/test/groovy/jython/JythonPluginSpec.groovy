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
import org.gradle.api.internal.project.AbstractProject
import org.gradle.api.logging.Logger

class JythonPluginSpec extends ProjectSpec {

    private static final String PLUGIN = 'com.github.rzabini.gradle-jython'
    Logger testLogger

    def setup(){
        testLogger = Mock(Logger)
        AbstractProject.buildLogger = testLogger
    }

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
            jython {
                pypackage 'docutils:0.12'
                pypackage 'docutils:0.12'
            }
        }

        then:
        project.file("${project.buildDir}/jython/docutils-0.12.tar.gz").isFile()

        2 * testLogger._('Downloading docutils, version 0.12')
        1 * testLogger._('downloading https://pypi.python.org/packages/source/d/docutils/docutils-0.12.tar.gz#md5=4622263b62c5c771c03502afa3157768')
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
}
