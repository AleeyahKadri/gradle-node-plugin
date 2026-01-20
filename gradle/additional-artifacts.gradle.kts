val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(project.the<SourceSetContainer>()["main"].allSource)
}

val groovydocJar by tasks.registering(Jar::class) {
    dependsOn(tasks.named("groovydoc"))
    archiveClassifier.set("groovydoc")
    from(tasks.named<Groovydoc>("groovydoc").get().destinationDir)
}

val javadocJar by tasks.registering(Jar::class) {
    dependsOn(tasks.named("javadoc"))
    archiveClassifier.set("javadoc")
    from(tasks.named<Javadoc>("javadoc").get().destinationDir)
}

configurations.getByName("archives").artifacts.apply {
    add(project.artifacts.add("archives", sourcesJar))
    add(project.artifacts.add("archives", groovydocJar))
    add(project.artifacts.add("archives", javadocJar))
}
