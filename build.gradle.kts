import org.gradle.api.JavaVersion
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.testing.Test

buildscript {
    apply(from = "$rootDir/gradle/buildscript.gradle", to = this)
}

group = "com.moowork.gradle"

apply(plugin = "idea")
apply(plugin = "groovy")
apply(plugin = "maven")
apply(plugin = "maven-publish")
apply(plugin = "java-gradle-plugin")
apply(from = "$rootDir/gradle/additional-artifacts.gradle")
apply(from = "$rootDir/gradle/coverage.gradle")
apply(from = "$rootDir/gradle/travis-ci.gradle")
apply(from = "$rootDir/gradle/publishing.gradle")

val compatibilityVersion = "1.8"
extra["compatibilityVersion"] = compatibilityVersion

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.toVersion(compatibilityVersion)
    targetCompatibility = JavaVersion.toVersion(compatibilityVersion)
}

repositories {
    jcenter()
}

val sourceSets = the<SourceSetContainer>()
sourceSets.create("integTest")

configurations {
    getByName("integTestCompile").extendsFrom(getByName("testCompile"))
    getByName("integTestRuntime").extendsFrom(getByName("testRuntime"))
}

dependencies {
    "compile"(gradleApi())
    "testCompile"("cglib:cglib-nodep:3.2.4")
    "testCompile"("org.apache.commons:commons-io:1.3.2")
    "testCompile"("org.spockframework:spock-core:1.0-groovy-2.4") {
        exclude(group = "org.codehaus.groovy")
    }
}

tasks.withType<Test> {
    testLogging {
        events("skipped", "failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

tasks.register<Test>("integTest") {
    shouldRunAfter("test")
    testClassesDirs = sourceSets["integTest"].output.classesDirs
    classpath = sourceSets["integTest"].runtimeClasspath
}
