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

import gradle.GradleIntegrationSpec
import nebula.test.functional.ExecutionResult
import org.joda.time.LocalDateTime

class JythonPluginIntegrationSpec extends GradleIntegrationSpec{



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

    def "can execute imported package"(){
        given:
        int year = LocalDateTime.now().year
        file('test.py').text=
"""
import arrow

now= arrow.utcnow()
print 'current datetime is: {}'.format(now)
"""
        when:
        buildFile << buildscript()
        buildFile << '''
            apply plugin:'com.github.rzabini.gradle-jython'

            jython{
                pypackage 'requests:2.12.3','python-dateutil:2.4.2','arrow:0.7.0', 'six:1.10.0'
            }

            task testJython(type:jython.JythonTask) {
                script file('test.py')
            }
        '''.stripIndent()

        then:
        executeGradleWrapper('clean', 'testJython').find { line -> line.contains("current datetime is: $year")}
    }

    def "jython source files are in execution path"(){
        when:
        file('src/main/jython/mymodule.py').text =
'''
class MyClass:
    """A simple example class"""
    def f(self):
        return 'hello world'
'''
        file('test.py').text =
'''
from mymodule import MyClass
x = MyClass()
print x.f()
'''
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



}
