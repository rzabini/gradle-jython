package gradle

import nebula.test.IntegrationSpec

abstract class GradleIntegrationSpec extends IntegrationSpec {
    def setup() {
        setupGradleWrapper()
    }

    def buildscript() {
        def lines = file('.gradle-test-kit/init.gradle').readLines()
        lines.remove(0)

        if (isWindows()) {
            String buildDir = toUnixPath(new File('build').canonicalPath)

            [ 'resources/main', 'classes/main', 'resources/test', 'classes/test' ].each { dir -> lines.add(2, "classpath files('${buildDir}/${dir}')") }
            lines.add(2, "classpath fileTree(dir: '${buildDir}/tmp/expandedArchives', include: 'jacocoagent.jar')")
        }

        lines.remove(lines.size() - 1)
        lines.join(System.getProperty("line.separator")).stripIndent()
    }

    private String gradleWrapperExecutable() {
        "${isWindows() ? 'gradlew.bat' : './gradlew'}"
    }

    boolean isWindows() {
        System.properties['os.name'].toLowerCase().contains('windows')
    }

    def toUnixPath(String path){
        path.replaceAll('\\\\','/')
    }

    def setupGradleWrapper() {
        runTasksSuccessfully(':wrapper')
    }

    def executeGradleWrapper(File dir = projectDir, String... args) {
        println "========"
        println "executing gradlew ${args.join(' ')}"
        println "--------"
        def lines=[]
        List<String> gradlewCommand = [gradleWrapperExecutable()] + args.toList()
        def process = new ProcessBuilder(gradlewCommand)
                .directory(dir)
                .redirectErrorStream(true)
                .start()
        process.inputStream.eachLine {
            lines << it
        }
        def exitValue = process.waitFor()
        if (exitValue != 0) {
            throw new RuntimeException("failed to execute ${args.join(' ')}\nOutput was:\n" + lines.join('\n'))
        }
        return lines
    }

}
