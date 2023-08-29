package ru.shtp.androidbuilder

import org.eclipse.jgit.api.Git
import java.io.File

val androidRepoFile = File("application")
val builtPath = File("output")
const val uri = "https://github.com/ITClassDev/Mobile"

fun main() {
    // TODO Loading and Saving Manager data

    val git = if (androidRepoFile.exists()) {
        Git.open(androidRepoFile)
    } else {
        Manager.lastCheck = System.currentTimeMillis()
        Git.cloneRepository()
            .setURI(uri)
            .setDirectory(androidRepoFile)
            .call()
    }

    if (System.currentTimeMillis() - Manager.lastCheck >= 600000) {
        buildApp()
    }

    while (true) {
        Thread.sleep(600000)
        checkAndBuild()
    }
}

fun checkAndBuild() {
    // TODO Check repository
    buildApp()
}

fun buildApp() {
    // TODO Build the application
}