package jython

import org.apache.ivy.util.url.ApacheURLLister
import org.gradle.api.Project

class JythonExtension {
    def pypiBase="https://pypi.python.org/packages/source/"
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

			URL url = findDonloadUrl(name, version)
						

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

	private URL findDonloadUrl(name, version) {
		def path = "${name[0]}/${name}"

		def dir = "$project.jython.pypiBase${path}"
		def urlLister = new ApacheURLLister()
		def files = urlLister.listFiles(new URL(dir))
		files.find { URL candidate -> candidate.path.toLowerCase().contains("$name-${version}.tar.gz".toLowerCase()) }

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