tasks {
    val sourcesJar by registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }

    val groovydocJar by registering(Jar::class) {
        dependsOn("groovydoc")
        archiveClassifier.set("groovydoc")
        from((project.tasks["groovydoc"] as Groovydoc).destinationDir)
    }

    val javadocJar by registering(Jar::class) {
        dependsOn("javadoc")
        archiveClassifier.set("javadoc")
        from((project.tasks["javadoc"] as Javadoc).destinationDir)
    }

    artifacts {
        add("archives", sourcesJar)
        add("archives", groovydocJar)
        add("archives", javadocJar)
    }
}
