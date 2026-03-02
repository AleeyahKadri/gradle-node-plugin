val isMaster: Boolean by extra { System.getenv("TRAVIS_BRANCH") == "master" }
val isCI: Boolean by extra { System.getenv("CI") != null }

tasks.register("ci") {
    dependsOn("clean", "build", "integTest")
    description = "Continuous integration tasks"
    group = "Build"
}
