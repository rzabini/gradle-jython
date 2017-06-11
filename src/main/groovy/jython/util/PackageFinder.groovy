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
package jython.util

import groovy.util.logging.Slf4j
import groovy.util.slurpersupport.GPathResult
import jython.JythonPackage

/**
 * Finds the URL to download a package from the Python Package Index site.
 */
@Slf4j
class PackageFinder {

    /**
     * The http URL of Python Package Index site
     */
    static final String PYPI_BASE ='https://pypi.python.org/simple'

    /**
     * Retrieves the text of the web page relative to a package
     * and scans the html text to find the link to download a specific version
     * @param name  the package name, not null
     * @param version  the package version, not null
     * @return  the URL of the tar.gz file matching the given version
     */
    static URL findPackageArchive(name, version) {
        String path = "${name.toLowerCase()}"
        String dir = "$PYPI_BASE/${path}"
        log.info "inferred path is: $dir"

        String text=dir.toURL().text

        XmlSlurper parser = new XmlSlurper()
        parser.setFeature('http://apache.org/xml/features/disallow-doctype-decl', false)
        GPathResult doc = parser.parseText(text)
        Object packageLink=doc.depthFirst().find { it.name() == 'a' && it.text().equalsIgnoreCase("$name-${version}.tar.gz") }

        new URL("$dir/${packageLink?.@href?.text()}").toURI().normalize().toURL()
    }

    static URL findPackageArchive(JythonPackage jythonPackage) {
        findPackageArchive(jythonPackage.name, jythonPackage.version)

    }
}
