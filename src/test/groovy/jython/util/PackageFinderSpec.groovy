package jython.util

import jython.util.PackageFinder
import spock.lang.Specification


class PackageFinderSpec extends Specification{

    def "can find an existing package archive"(){

        when:
        URL url=PackageFinder.findPackageArchive(name, version)
        then:
        url.file.endsWith("$name-${version}.tar.gz")
        where:
        name        |version
        'Pygments'  |'2.0.2'
        'Sphinx'    |'1.3.1'
    }
}
