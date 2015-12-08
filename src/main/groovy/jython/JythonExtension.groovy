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

import jython.util.PackageFinder
import org.gradle.api.Project

/**
 * Allows the gradle project to download python packages and use them in task execution.
 */
class JythonExtension {
	Project project
	
	JythonExtension(Project project) {
		this.project = project
	}
	
	void pypackage(String... pkgs) {
		pkgs.each { pkg ->
			installPackage(pkg)
		}
	}

/**
 * Downloads and extracts a python package into build/jython directory
 * @param pkg  the package name
 */
	void installPackage(String pkg) {
	
		project.file("${project.buildDir}/jython").mkdirs()
        String name, version
		(name,version) = pkg.split(':')
			project.logger.info "Downloading $name, version $version"
		if (project.file("$project.buildDir/jython/${name}-${version}.tar.gz").exists()) {
			project.logger.quiet "Skipping existing file ${name}-${version}.tar.gz"
		} else {
			URL url = PackageFinder.findPackageArchive(name, version)
			String filename = downloadPackage(url)
			untar(filename, name)
		}
		project.dependencies {
			pythonpath project.files("${project.buildDir}/jython/${name}-${version}")
		}
	}

	private String downloadPackage(URL url) {
		String filename = url.file [url.path.lastIndexOf('/') + 1 .. -1]

		project.download {
			project.logger.quiet "downloading $url"
			src url
			dest "${project.buildDir}/jython/${filename}"
		}
		filename
	}

	private untar(filename, name) {
		project.logger.quiet("untar $filename, $name")
		project.ant.untar(
				src: "$project.buildDir/jython/${filename}",
				dest: "${project.buildDir}/jython",
				compression: 'gzip'
		) {
			patternset {
				include(name: "**/${name.toLowerCase()}.py")
				include(name: "**/${name.toLowerCase()}/**/*")
				include(name: "**/${name.toLowerCase() - 'python-'}/**/*")
			}
		}
	}
}
