apply(plugin = "maven")
apply(plugin = "maven-publish")
apply(plugin = "com.jfrog.artifactory")
apply(plugin = "com.jfrog.bintray")

fun resolveProperty(envVarKey: String, projectPropKey: String): String? {
    val propValue = System.getenv()[envVarKey]
    if (propValue != null) {
        return propValue
    }
    return if (project.hasProperty(projectPropKey)) {
        project.property(projectPropKey) as String
    } else {
        null
    }
}

val bintrayUser: String? by extra { resolveProperty("BINTRAY_USER", "bintrayUser") }
val bintrayKey: String? by extra { resolveProperty("BINTRAY_KEY", "bintrayKey") }
val isMaster: Boolean by extra

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["groovydocJar"])
            artifact(tasks["javadocJar"])
        }
    }
}

tasks.named("artifactoryPublish") {
    onlyIf { isMaster }
    mustRunAfter("build")
}

configure<com.jfrog.bintray.gradle.BintrayExtension> {
    user = bintrayUser
    key = bintrayKey
    publish = true
    setPublications("mavenJava")

    pkg.apply {
        name = project.name
        repo = "maven"
        userOrg = "srs"
        setLicenses("Apache-2.0")
        vcsUrl = "https://github.com/srs/gradle-node-plugin.git"
        websiteUrl = "https://github.com/srs/gradle-node-plugin"
        issueTrackerUrl = "https://github.com/srs/gradle-node-plugin/issues"
        githubRepo = "srs/gradle-node-plugin"
        setLabels("java", "gradle", "node", "yarn", "npm", "grunt", "gulp")
        desc = "Gradle plugin for executing node scripts."

        version.apply {
            vcsTag = "v${project.version}"
            attributes = mapOf(
                "gradle-plugin" to listOf(
                    "com.moowork.node:${project.group}:${project.name}",
                    "com.moowork.grunt:${project.group}:${project.name}",
                    "com.moowork.gulp:${project.group}:${project.name}"
                )
            )
        }
    }
}

// Configure artifactory
project.configure<org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention> {
    setContextUrl("https://oss.jfrog.org")
    
    publish {
        repository {
            setRepoKey("oss-snapshot-local")
            setUsername(bintrayUser)
            setPassword(bintrayKey)
            setMaven(true)
        }
        defaults {
            setPublications("mavenJava")
        }
    }
}
