package jython

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.tasks.Delete

class JythonPlugin implements Plugin<Project>{
	void apply(Project project){
		assert org.gradle.api.JavaVersion.current().java7Compatible, "at least Java 7 is needed"
		project.with {

			apply plugin:"base"
			apply plugin: "de.undercouch.download"
			
			configurations.create('pythonpath')
			repositories{
				mavenCentral()
			}

			configurations{jython}

			dependencies{
				jython 'org.python:jython-standalone:2.7.0'
			}
			
			extensions.create("jython", JythonExtension, project)
		}
		
		project.task('cleanJython', type:Delete){
			delete "${project.buildDir}/jython"
		}
		

	}
}


