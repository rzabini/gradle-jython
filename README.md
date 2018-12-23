[![Build Status](https://travis-ci.org/rzabini/gradle-jython.svg?branch=master)](https://travis-ci.org/rzabini/gradle-jython)
[![Coverage Status](https://coveralls.io/repos/rzabini/gradle-jython/badge.svg?branch=master&service=github)](https://coveralls.io/github/rzabini/gradle-jython?branch=master)

# Overview
This plugin allows you to execute a python script through [jython](http://http://www.jython.org/).

# Setup
Add plugin to your gradle build file according to:
https://plugins.gradle.org/plugin/com.github.rzabini.gradle-jython

**JVM Compatibility:**
Java 7 and above (as requested by Jython 2.7)

## Tasks
The plugin adds the `jython.JythonTask` task type to your projects, which allows you to run a jython script.

## Configuration
Add a task of type `jython.JythonTask` and set its `script` property to a string with a valid jython script, or to a file with a valid jython program

## Examples
Inline script:
```groovy
def sayHello="""
print 'hello from $project.name'
"""

task testJython(type:jython.JythonTask) {
	script sayHello
}
```

External script:
```groovy

task testJython(type:jython.JythonTask) {
	script file('scripts/sayHello.py')
}
```

If you need some python packages published in the Python Package Index site, use the `pypackage` method of the `jython` extension, as in the following example:
```groovy
jython {
    pypackage 'python-dateutil:2.4.2','arrow:0.7.0', 'six:1.10.0'
}
```
Use a string in the form *packageName*:*version*. The plugin will download and extract the python package distribution, and add it in the `pythonpath` environment variable used by tasks of class `JythonTask`.

If the python module name is different from the package file name, it can be specified in a configuration closure, as in the following example:
```groovy
jython {
    pypackage ('requests-mock:1.3.0') {
        module 'requests_mock'
    }
    pypackage 'requests:2.12.3','six:1.10.0'
}
```

The plugin does not automatically resolve python dependencies, so these must be declared explicitly, as in the previous example.

# Acknowledgements
This plugin uses the [gradle-download-task](https://github.com/michel-kraemer/gradle-download-task) by Michel Krämer.
The semantic version numbering of this plugin is handled by the [gradle-semantic-release-plugin](https://github.com/tschulte/gradle-semantic-release-plugin) by Tobias Schulte. From (the code of) this plugin I also adopted the integration testing strategy.

# License
This plugin is available under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

