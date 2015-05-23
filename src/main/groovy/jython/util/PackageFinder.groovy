package jython.util

import groovy.util.logging.Slf4j

@Slf4j
class PackageFinder {

    static final String pypiBase='https://pypi.python.org/simple'

    static URL findPackageArchive(name, version) {
        def path = "${name.toLowerCase()}"
        def dir = "$pypiBase/${path}"
        log.info "inferred path is: $dir"

        def text=dir.toURL().text

        def doc = new XmlSlurper().parseText(text)
        def packageLink=doc.depthFirst().find {it.name() == "a" && it.text() == "$name-${version}.tar.gz"}

        new URL("$dir/${packageLink.@href.text()}").toURI().normalize().toURL()

    }
}
