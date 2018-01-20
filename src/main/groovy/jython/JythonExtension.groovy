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
import org.gradle.api.logging.Logger

/**
 * Allows the gradle project to download python packages and use them in task execution.
 */
class JythonExtension {
	Project project
	List<JythonPackage> packages = []
    Logger logger

	JythonExtension(Project project) {
		this.project = project
        this.logger = project.logger
	}

	void pypackage(String... pkgs) {
		pkgs.each { pkg ->
			JythonPackage jythonPackage = new JythonPackage(pkg)
            packages << jythonPackage
			installPackage(jythonPackage)
		}
	}

    void pypackage(String pkg, Closure config) {
        JythonPackage jythonPackage = new JythonPackage(pkg, config)
        packages << jythonPackage
        installPackage(jythonPackage)
    }

/**
 * Downloads a python package into build/jython directory
 * @param pkg  the package to download
 */
	void installPackage(JythonPackage pkg) {
		project.file("${project.buildDir}/jython").mkdirs()
		logger.quiet "Downloading $pkg.name, version $pkg.version"
        downloadJythonPackage(pkg)
	}

    void downloadJythonPackage(JythonPackage jythonPackage) {
        URL url = PackageFinder.findPackageArchive(jythonPackage)
        jythonPackage.fileName = url.file [url.path.lastIndexOf('/') + 1 .. -1]
        String destinationFile = "${project.buildDir}/jython/${jythonPackage.fileName}"

        if (project.file(destinationFile).exists()) {
            logger.quiet "Skipping existing file ${jythonPackage.fileName}"
        } else {
            project.download {
                logger.quiet "downloading $url"
                src url
                dest destinationFile
            }
        }
    }

	void addPackagesToClasspath() {
		packages.each { JythonPackage jythonPackage ->

            downloadJythonPackage(jythonPackage)

            logger.quiet("untar ${jythonPackage.fileName}, ${jythonPackage.name}")
            project.ant.untar(
                    src: "$project.buildDir/jython/${jythonPackage.fileName}",
                    dest: "${project.buildDir}/classes/main",
                    compression: 'gzip'
            ) {
                patternset {
                    include(name: "**/${jythonPackage.moduleName.toLowerCase()}.py")
                    include(name: "**/${jythonPackage.moduleName.toLowerCase()}/**/*")
                    include(name: "**/${jythonPackage.moduleName.toLowerCase() - 'python-'}/**/*")
                }
                mapper(type: 'regexp', from: "${jythonPackage.name}-${jythonPackage.version}/(.+)", to: '\\1')
            }
        }

        project.dependencies {
            pythonpath project.files("${project.buildDir}/classes/main")
        }
    }
}
