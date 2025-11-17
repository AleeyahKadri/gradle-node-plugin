import com.moowork.gradle.node.task.NodeTask

apply(plugin = "com.moowork.node")

configure<com.moowork.gradle.node.NodeExtension> {
    download = true
}

val helloWorld by tasks.registering(NodeTask::class) {
    dependsOn("npmInstall")
    script = file("src/node")
}

tasks.register("build") {
    dependsOn(helloWorld)
}
