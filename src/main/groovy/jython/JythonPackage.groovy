package jython

/**
 * Represents Jython package coordinates
 */
class JythonPackage {

    String name
    String version
    String fileName

    JythonPackage(String fullName) {
        (name,version) = fullName.split(':')

    }
}
