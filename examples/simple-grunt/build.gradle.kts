import com.moowork.gradle.node.grunt.GruntTask

apply(plugin = "com.moowork.grunt")

configure<com.moowork.gradle.node.NodeExtension> {
    download = true
}

val helloWorld by tasks.registering(GruntTask::class) {
    dependsOn("installGrunt")
}

tasks.register("build") {
    dependsOn(helloWorld)
}
