# Overview
This plugin allows you to execute a python script through [Jython](http://http://www.jython.org/)

# Setup
#### Gradle >= 2.1

```groovy
plugins {
  id "com.github.rzabini.gradle-jython" version "1.0.0"
}
```

#### Gradle < 2.1

```groovy
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.com.github.rzabini:gradle-jython:1.0.0"
  }
}

apply plugin: "com.github.rzabini.gradle-jython"
```



 **JVM Compatibility:**
Java 7 and above (as requested by Jython 2.7)

## Tasks
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
# Acknowledgements
This plugin uses the [gradle-download-task](https://github.com/michel-kraemer/gradle-download-task) by Michel Krämer

# License
This plugin is available under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

