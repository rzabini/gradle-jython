package jython

import spock.lang.Specification

class JythonPackageSpec extends Specification {

    def "can be configured by closure"() {
        JythonPackage jythonPackage = new JythonPackage('name:version',
                { module 'moduleName'})
        expect:
            jythonPackage.moduleName == 'moduleName'
    }
}
