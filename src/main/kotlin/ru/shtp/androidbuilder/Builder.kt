package ru.shtp.androidbuilder

import org.apache.commons.io.IOUtils
import org.apache.log4j.Logger
import org.eclipse.jgit.api.Git
import ru.shtp.androidbuilder.dto.ReleaseManifest
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class Builder(private val logger: Logger) {
    private val loggerStream = LoggingOutputStream(logger)
    fun run() {
        val androidRepoFile = File(Data.androidRepo)
        androidRepoFile.parentFile.mkdirs()
        val git = if (androidRepoFile.exists())
            Git.open(androidRepoFile)
        else
            Git.cloneRepository().setURI(Data.uri).setDirectory(androidRepoFile).call()
        loop(git)
    }

    private fun loop(git: Git) {
        while (true) {
            logger.info("Pull check")
            git.pull()
            val name = git.log().setMaxCount(1).call().iterator().next().name
            if (Manager.appState.checkedVersion != name)
                buildAll(name)
            logger.info("Sleeping 10 minutes")
            Thread.sleep(600000)
        }
    }

    private fun buildAll(name: String) {
        logger.info("New update available, building..")
        if (File(Data.outputFolder).exists()) File(Data.outputFolder).deleteRecursively()
        File(Data.outputFolder).mkdirs()

        val buildApplication = buildApk()
        if (buildApplication != 0)
            return

        val (versionName, versionCode) = FileReader(Data.versionInfo).use {
            val readLines = it.readLines()
            Pair(readLines[0], readLines[1].toInt())
        }
        val releaseInfo = FileReader(Data.releaseInfo).use { it.readText() }

        FileWriter(Data.tempReleaseManifest).use {
            Manager.gson.toJson(
                ReleaseManifest(
                    releaseInfo, System.currentTimeMillis(), versionName, versionCode
                ), it
            )
        }
        logger.info("Wrote release manifest")
        File(Data.tempReleaseManifest).copyTo(File(Data.releaseFileManifest), true)
        File(Data.tempDebugApk).copyTo(File(Data.releaseApk), true)
        logger.info("Copied files")
        Manager.appState.checkedVersion = name
        Manager.saveAppState()
    }

    private fun buildApk(): Int {
        logger.info("Start gradlew build")

        val waitChmod = ProcessBuilder("chmod", "+x", "./gradlew")
            .directory(File(Data.androidRepo))
            .redirectOutput(ProcessBuilder.Redirect.INHERIT).start().waitFor()
        if (waitChmod != 0) {
            logger.info("Chmod gradlew exit code $waitChmod")
            return waitChmod
        }

        val versionsProcess = ProcessBuilder("./gradlew", "generateVersions", "--console=plain", "--parallel")
            .directory(File(Data.androidRepo))
            .redirectErrorStream(true)
            .start()
        IOUtils.copy(versionsProcess.inputStream, loggerStream)
        versionsProcess.outputStream
        val waitVersions = versionsProcess.waitFor()
        if (waitVersions != 0) {
            logger.info("Finished generating versions with code $waitVersions")
            return waitVersions
        }

        val buildProcess = ProcessBuilder("./gradlew", "build", "--console=plain", "--parallel", "-Penv=production")
            .directory(File(Data.androidRepo))
            .redirectErrorStream(true).start()
        IOUtils.copy(buildProcess.inputStream, loggerStream)
        val waitBuild = buildProcess.waitFor()

        logger.info("Finished build with code $waitBuild")
        return waitBuild
    }
}