val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

val groovydocJar by tasks.registering(Jar::class) {
    dependsOn(tasks.groovydoc)
    archiveClassifier.set("groovydoc")
    from(tasks.groovydoc.get().destinationDir)
}

val javadocJar by tasks.registering(Jar::class) {
    dependsOn(tasks.javadoc)
    archiveClassifier.set("javadoc")
    from(tasks.javadoc.get().destinationDir)
}

artifacts {
    archives(sourcesJar)
    archives(groovydocJar)
    archives(javadocJar)
}
