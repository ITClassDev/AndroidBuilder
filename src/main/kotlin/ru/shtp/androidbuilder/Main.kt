package ru.shtp.androidbuilder

import org.eclipse.jgit.api.Git
import ru.shtp.androidbuilder.Data.androidRepo
import ru.shtp.androidbuilder.Data.applicationFolder
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
    androidRepoFile.mkdirs()
    val git = if (androidRepoFile.exists()) {
        Git.open(androidRepoFile)
    } else {
        Git.cloneRepository()
            .setURI(uri)
            .setDirectory(androidRepoFile)
            .call()
    }
    while (true) {
        git.pull()
        val name = git.log().setMaxCount(1).call().iterator().next().name
        if (Manager.appState.checkedVersion != name) {
            println("Building")

            if (File(Data.outputFolder).exists())
                File(Data.outputFolder).deleteRecursively()
            File(Data.outputFolder).mkdirs()

            buildApplication()

            val (versionName, versionCode) = FileReader(versionInfo).use {
                Pair(it.readLines()[0], it.readLines()[1].toInt())
            }
            val releaseInfo = FileReader(releaseInfo).use { it.readText() }

            FileWriter(tempReleaseManifest).use {
                Manager.gson.toJson(
                    ReleaseManifest(
                        releaseInfo,
                        System.currentTimeMillis(),
                        versionName,
                        versionCode
                    ), it
                )
            }

            File(tempReleaseManifest).copyTo(File(releaseFileManifest), true)
            File(tempDebugApk).copyTo(File(releaseApk), true)

            Manager.appState.checkedVersion = name
            Manager.saveAppState()
        }
        Thread.sleep(600000)
    }
}

fun buildApplication() {
    Runtime.getRuntime().exec("sh -s \"cd $applicationFolder && ./gradlew build\"")
}