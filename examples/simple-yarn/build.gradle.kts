plugins {
    id("com.moowork.node")
}

node {
    download = true
}

tasks.register<com.moowork.gradle.node.task.NodeTask>("helloWorld") {
    dependsOn("yarn")
    script = file("src/node")
}

tasks.register("build") {
    dependsOn("helloWorld")
}
