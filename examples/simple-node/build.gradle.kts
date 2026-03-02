plugins {
    id("com.moowork.node")
}

node {
    download = true
}

tasks.register<com.moowork.gradle.node.task.NodeTask>("helloWorld") {
    dependsOn("npmInstall")
    script = file("src/node")
}

tasks.register("build") {
    dependsOn("helloWorld")
}
