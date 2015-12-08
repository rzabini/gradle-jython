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

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.tasks.Delete

/**
 * Enables the gradle project to execute jython code, either as a command or as a script.
 */
class JythonPlugin implements Plugin<Project> {
	void apply(Project project) {
		assert org.gradle.api.JavaVersion.current().java7Compatible, 'at least Java 7 is needed'
		project.with {

			apply plugin: 'base'
			apply plugin: 'de.undercouch.download'

			configurations.create('pythonpath')
			repositories {
				mavenCentral()
			}

			configurations { jython }

			dependencies {
				jython 'org.python:jython-standalone:2.7.0'
			}

			extensions.create('jython', JythonExtension, project)
		}

		project.task('cleanJython', type: Delete) {
			delete "${project.buildDir}/jython"
		}
	}
}
