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

package jython.integration

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult
import org.joda.time.LocalDateTime

class JythonPluginIntegrationSpec extends IntegrationSpec{

    def setup() {
        setupGradleWrapper()
    }

    def buildscript() {
        def lines = file('.gradle-test-kit/init.gradle').readLines()
        lines.remove(0)

        if (isWindows()) {
            String buildDir = toUnixPath(new File('build').canonicalPath)

            [ 'resources/main', 'classes/main', 'resources/test', 'classes/test' ].each { dir -> lines.add(2, "classpath files('${buildDir}/${dir}')") }
            lines.add(2, "classpath fileTree(dir: '${buildDir}/tmp/expandedArchives', include: 'jacocoagent.jar')")
        }

        lines.remove(lines.size() - 1)
        lines.join(System.getProperty("line.separator")).stripIndent()
    }

    def "can execute jython code"(){
        when:
        buildFile << buildscript()
        buildFile << '''
            apply plugin:'com.github.rzabini.gradle-jython'

            task testJython(type:jython.JythonTask) {
                script "print 'hello'"
            }
        '''.stripIndent()

        ExecutionResult result = runTasksSuccessfully('testJython')

        then:
        notThrown(Exception)
    }

    def "can execute external jython script"(){
        when:
        file('test.py').text = "print 'hello'"
        buildFile << buildscript()
        buildFile << '''
            apply plugin:'com.github.rzabini.gradle-jython'

            task testJython(type:jython.JythonTask) {
                script file('test.py')
            }
        '''.stripIndent()

        ExecutionResult result = runTasksSuccessfully('testJython')

        then:
        notThrown(Exception)
    }

    boolean isWindows() {
        System.properties['os.name'].toLowerCase().contains('windows')
    }

    def toUnixPath(String path){
        path.replaceAll('\\\\','/')
    }

    def setupGradleWrapper() {
        runTasksSuccessfully(':wrapper')
    }

    def execute(File dir = projectDir, String... args) {
        println "========"
        println "executing ${args.join(' ')}"
        println "--------"
        def lines=[]
        def process = new ProcessBuilder(args)
                .directory(dir)
                .redirectErrorStream(true)
                .start()
        process.inputStream.eachLine {
            lines << it
        }
        def exitValue = process.waitFor()
        if (exitValue != 0) {
            throw new RuntimeException("failed to execute ${args.join(' ')}\nOutput was:\n" + lines.join('\n'))
        }
        return lines
    }

    def runGradleTask(String task){
        execute "${isWindows() ? 'gradlew.bat' : './gradlew'}", task
    }
}
