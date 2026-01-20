import com.moowork.gradle.node.gulp.GulpTask

apply(plugin = "com.moowork.gulp")

configure<com.moowork.gradle.node.NodeExtension> {
    download = true
}

val helloWorld by tasks.registering(GulpTask::class) {
    dependsOn("installGulp")
}

tasks.register("build") {
    dependsOn(helloWorld)
}
