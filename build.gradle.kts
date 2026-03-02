buildscript {
    repositories {
        jcenter()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
        classpath("org.jfrog.buildinfo:build-info-extractor-gradle:4.4.0")
    }
}

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
apply(from = "$rootDir/gradle/publishing.gradle.kts")

val compatibilityVersion: String by extra { "1.8" }
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    jcenter()
}

configurations {
    val integTestCompile by creating {
        extendsFrom(configurations.getByName("testCompile"))
    }
    val integTestRuntime by creating {
        extendsFrom(configurations.getByName("testRuntime"))
    }
}

dependencies {
    add("compile", gradleApi())
    add("testCompile", "cglib:cglib-nodep:3.2.4")
    add("testCompile", "org.apache.commons:commons-io:1.3.2")
    add("testCompile", "org.spockframework:spock-core:1.0-groovy-2.4") {
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
    create("integTest")
}

tasks.register<Test>("integTest") {
    shouldRunAfter("test")
    
    testClassesDirs = sourceSets["integTest"].output.classesDirs
    classpath = sourceSets["integTest"].runtimeClasspath
}
