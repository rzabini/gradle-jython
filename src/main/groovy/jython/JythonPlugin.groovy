package jython

import org.gradle.api.Project
import org.gradle.api.Plugin

class JythonPlugin implements Plugin<Project>{
	void apply(Project project){
		assert org.gradle.api.JavaVersion.current().java7Compatible, "at least Java 7 is needed"
		project.with {

			apply plugin:"base"
			apply plugin: "de.undercouch.download"
			
			configurations.maybeCreate('pythonpath')
			repositories{
				mavenCentral()
			}

			configurations{jython}

			dependencies{
				jython 'org.python:jython-standalone:2.7+'
			}
			
			if(extensions.findByName("jython")==null)
				extensions.create("jython", JythonExtension)
		}
		
		project.task('downloadPythonPackages') << {
			project.file("${project.buildDir}/jython").mkdirs()

			project.jython.packages.each { pkg ->
				
				def (name,version)=pkg.split(':')
				if(project.file("$project.buildDir/jython/${name}-${version}.tar.gz").exists())
				logger.quiet "Skipping existing file ${name}-${version}.tar.gz"
				else{
					def path="${name[0]}/${name}"
					def url="$project.jython.pypiBase${path}/$name-${version}.tar.gz"

					project.download {
						src url
						dest "${project.buildDir}/jython"
					}

				
				ant.untar(
					src: "$project.buildDir/jython/${name}-${version}.tar.gz",
					dest: "${project.buildDir}/jython",
					compression: 'gzip'
				) {
					patternset {
						include(name: "${name}-${version}/**/${name.toLowerCase()}/**/*")
					}
				}
				}
			}
		}
	}
}


