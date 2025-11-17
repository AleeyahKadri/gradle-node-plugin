buildscript {
    apply(from = "$rootDir/gradle/buildscript.gradle.kts")
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
    create("integTest")
}

val integTest by tasks.registering(Test::class) {
    shouldRunAfter(tasks.test)
    
    testClassesDirs = sourceSets["integTest"].output.classesDirs
    classpath = sourceSets["integTest"].runtimeClasspath
}
