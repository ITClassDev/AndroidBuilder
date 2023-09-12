package ru.shtp.androidbuilder

import org.eclipse.jgit.api.Git
import ru.shtp.androidbuilder.Data.androidRepo
import ru.shtp.androidbuilder.Data.releaseApk
import ru.shtp.androidbuilder.Data.releaseFileManifest
import ru.shtp.androidbuilder.Data.releaseInfo
import ru.shtp.androidbuilder.Data.tempDebugApk
import ru.shtp.androidbuilder.Data.tempReleaseManifest
import ru.shtp.androidbuilder.Data.uri
import ru.shtp.androidbuilder.Data.versionInfo
import ru.shtp.androidbuilder.dto.ReleaseManifest
import java.io.File
import java.io.FileReader
import java.io.FileWriter


fun main() {
    val androidRepoFile = File(androidRepo)
    androidRepoFile.parentFile.mkdirs()
    val git = if (androidRepoFile.exists()) {
        Git.open(androidRepoFile)
    } else {
        Git.cloneRepository().setURI(uri).setDirectory(androidRepoFile).call()
    }
    while (true) {
        println("Pull check")
        git.pull()
        val name = git.log().setMaxCount(1).call().iterator().next().name
        if (Manager.appState.checkedVersion != name) {
            println("New update available, building..")

            if (File(Data.outputFolder).exists()) File(Data.outputFolder).deleteRecursively()
            File(Data.outputFolder).mkdirs()

            buildApplication()

            val (versionName, versionCode) = FileReader(versionInfo).use {
                val readLines = it.readLines()
                Pair(readLines[0], readLines[1].toInt())
            }
            val releaseInfo = FileReader(releaseInfo).use { it.readText() }

            FileWriter(tempReleaseManifest).use {
                Manager.gson.toJson(
                    ReleaseManifest(
                        releaseInfo, System.currentTimeMillis(), versionName, versionCode
                    ), it
                )
            }
            println("Writed release manifwst")
            File(tempReleaseManifest).copyTo(File(releaseFileManifest), true)
            File(tempDebugApk).copyTo(File(releaseApk), true)
            println("Copied files")
            Manager.appState.checkedVersion = name
            Manager.saveAppState()
        }
        println("Sleeping 10 minutes")
        Thread.sleep(600000)
    }
}

fun buildApplication(): Int {
    println("Start gradlew build")

    ProcessBuilder("chmod", "+x", "./gradlew")
        .directory(File(androidRepo))
        .redirectOutput(ProcessBuilder.Redirect.INHERIT).start().waitFor()
    val process = ProcessBuilder("./gradlew", "build", "-Penv=production")
        .directory(File(androidRepo))
        .redirectOutput(ProcessBuilder.Redirect.INHERIT).start()
    val waitFor = process.waitFor()
    ProcessBuilder("./gradlew", "generateVersions")
        .directory(File(androidRepo))
        .redirectOutput(ProcessBuilder.Redirect.INHERIT).start().waitFor()

    println("Finished build with code $waitFor")

    return waitFor
}