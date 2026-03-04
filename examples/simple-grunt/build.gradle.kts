plugins {
    id("com.moowork.grunt")
}

node {
    download = true
}

tasks.register<com.moowork.gradle.node.task.GruntTask>("helloWorld") {
    dependsOn("installGrunt")
}

tasks.register("build") {
    dependsOn("helloWorld")
}
