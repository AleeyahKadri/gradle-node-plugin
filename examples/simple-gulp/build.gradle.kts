plugins {
    id("com.moowork.gulp")
}

node {
    download = true
}

tasks.register<com.moowork.gradle.node.task.GulpTask>("helloWorld") {
    dependsOn("installGulp")
}

tasks.register("build") {
    dependsOn("helloWorld")
}
