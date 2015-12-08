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

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.JavaExec

/**
 * Allows the execution of jython commands and scripts.
 */
class JythonTask extends JavaExec {
		
		JythonTask() {
			main 'org.python.util.jython'
			classpath project.configurations.jython.asPath
		}
		
		@TaskAction
		void exec() {
			logger.info 'pythonpath:'
			logger.info project.configurations.pythonpath.asPath
			
			systemProperties(['python.path': project.configurations.pythonpath.asPath])
			super.exec()
		}

        void script (String script) {
            args '-c', script
        }

        void script (File script) {
            args script
        }

}
