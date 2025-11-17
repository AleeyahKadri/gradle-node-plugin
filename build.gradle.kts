group = "com.moowork.gradle"

plugins {
    idea
    groovy
    maven
    `maven-publish`
    `java-gradle-plugin`
}

apply(from = "$rootDir/gradle/additional-artifacts.gradle.kts")
apply(from = "$rootDir/gradle/coverage.gradle.kts")
apply(from = "$rootDir/gradle/travis-ci.gradle.kts")
// Note: Publishing configuration disabled due to deprecated dependencies
// apply(from = "$rootDir/gradle/publishing.gradle.kts")

val compatibilityVersion by extra { "1.8" }
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
}

val integTestImplementation by configurations.creating {
    extendsFrom(configurations.testImplementation.get())
}

val integTestRuntimeOnly by configurations.creating {
    extendsFrom(configurations.testRuntimeOnly.get())
}

configurations.getByName("integTestRuntimeOnly") {
    extendsFrom(configurations.getByName("testRuntimeOnly"))
}

dependencies {
    implementation(gradleApi())
    testImplementation("cglib:cglib-nodep:3.2.4")
    testImplementation("org.apache.commons:commons-io:1.3.2")
    testImplementation("org.spockframework:spock-core:1.0-groovy-2.4") {
        exclude(group = "org.codehaus.groovy")
    }
}

tasks.withType<Test> {
    testLogging {
        events("skipped", "failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

sourceSets {
    create("integTest") {
        compileClasspath += sourceSets["main"].output
        runtimeClasspath += sourceSets["main"].output
    }
}

val integTest by tasks.registering(Test::class) {
    shouldRunAfter(tasks.test)
    dependsOn(tasks.pluginUnderTestMetadata)
    
    testClassesDirs = sourceSets["integTest"].output.classesDirs
    classpath = sourceSets["integTest"].runtimeClasspath + files(tasks.pluginUnderTestMetadata)
}
