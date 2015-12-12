package jython

class JythonPackage {

    String name
    String version
    String fileName

    JythonPackage(String fullName){
        (name,version) = fullName.split(':')

    }
}
