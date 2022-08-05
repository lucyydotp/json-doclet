plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "net.lucypoulton"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    implementation("com.google.code.gson:gson:2.9.1")

}

tasks {
    shadowJar {
        archiveClassifier.set("")
    }
    build {
        dependsOn(shadowJar)
    }
    javadoc {
        source = sourceSets.test.get().allSource
        title = null
        options.verbose()
        options.docletpath(File("/Users/lucy/src/json-doclet/build/libs/json-doclet-1.0-SNAPSHOT.jar"))
        options.doclet("net.lucypoulton.jsondoclet.JsonDoclet")
        options.windowTitle(project.description)

        (options as StandardJavadocDocletOptions).let {
            it.noTimestamp(false)
            it.addStringOption("version", project.version.toString())
        }
    }
}
tasks.getByName<Test>("test") {
    useJUnitPlatform()
}