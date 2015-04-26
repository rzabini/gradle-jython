package jython

import jython.util.PackageFinder
import org.gradle.api.Project

class JythonExtension {
	Project project
	
	JythonExtension(Project project){
		this.project=project
	}
	
	void pypackage(String... pkgs){
		pkgs.each{pkg->
			installPackage(pkg)
		}
	}
	
	public void installPackage(String pkg){
	
		project.file("${project.buildDir}/jython").mkdirs()
		def (name,version)=pkg.split(':')
			project.logger.info "Downloading $name, version $version"
		if(project.file("$project.buildDir/jython/${name}-${version}.tar.gz").exists())
			project.logger.quiet "Skipping existing file ${name}-${version}.tar.gz"
		else{

			URL url = PackageFinder.findPackageArchive(name, version)
						

			project.download {
				src url
				dest "${project.buildDir}/jython"
			}

			def filename=url.path.substring( url.path.lastIndexOf('/')+1, url.path.length() )
			untar(filename, name)
		}
		project.dependencies{
			pythonpath project.files("${project.buildDir}/jython/${name}-${version}")
		}
	}



	private untar(filename, name) {
		project.ant.untar(
				src: "$project.buildDir/jython/${filename}",
				dest: "${project.buildDir}/jython",
				compression: 'gzip'
		) {
			patternset {
				include(name: "**/${name.toLowerCase()}/**/*")
			}
		}
	}

}