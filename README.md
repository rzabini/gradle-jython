# Overview
This plugin allows you to execute a python script through [Jython](http://http://www.jython.org/)

# Usage
This plugin is still in dev stage, so it is not in any public repository.
To use it, clone the repository from https://github.com/rzabini/gradle-jython.git and install it in the local Maven repository.

```
	clone https://github.com/rzabini/gradle-jython.git
	cd gradle-jython
	gradlew publishToMavenLocal


To use the plugin, configure your `build.gradle` script and add the plugin:
```groovy
buildscript{
	repositories{
		mavenLocal()
		mavenCentral()
	}
	dependencies{
		classpath 'com.github.rzabini:gradle-jython:0+'
	}
}

apply plugin:'com.github.rzabini.gradle-jython'
```


 **JVM Compatibility:**
Java 7 and above (as requested by Jython 2.7)

# Tasks
The plugin adds the `jython.JythonTask` task type to your projects, which allows you to run a Jython script.

## Configuration
Add a task of type `jython.JythonTask` and set its `script` property to a string with a valid Jython script

## Example
```groovy
def sayHello="""
print 'hello from $project.name'
"""

task testJython(type:jython.JythonTask) {
	script sayHello
}
```

# License
This plugin is available under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

