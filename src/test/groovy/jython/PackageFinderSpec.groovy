package jython

import jython.util.PackageFinder
import spock.lang.Specification


class PackageFinderSpec extends Specification{

    def "can find an existing package archive"(){

        when:
        URL url=PackageFinder.findPackageArchive('Pygments', '2.0.2')
        then:
        url.file.endsWith('Pygments-2.0.2.tar.gz')
    }
}