package jython

/**
 * Represents Jython package coordinates
 */
class JythonPackage {

    String name
    String version
    String fileName
    String moduleName

    JythonPackage(String fullName) {
        (name,version) = fullName.split(':')
        moduleName = name
    }

    JythonPackage(String fullName, Closure config) {
        this(fullName)
        config.delegate = this
        config.call()
    }

    void module(String moduleName) {
        this.moduleName = moduleName
    }
}
