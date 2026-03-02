import org.gradle.api.tasks.SourceSet

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(project.the<SourceSetContainer>()["main"].allSource)
}

val groovydocJar by tasks.registering(Jar::class) {
    dependsOn("groovydoc")
    archiveClassifier.set("groovydoc")
    from((tasks.getByName("groovydoc") as Groovydoc).destinationDir)
}

val javadocJar by tasks.registering(Jar::class) {
    dependsOn("javadoc")
    archiveClassifier.set("javadoc")
    from((tasks.getByName("javadoc") as Javadoc).destinationDir)
}

artifacts {
    add("archives", sourcesJar)
    add("archives", groovydocJar)
    add("archives", javadocJar)
}
