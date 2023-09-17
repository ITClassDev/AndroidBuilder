package ru.shtp.androidbuilder

object Data {
    val applicationFolder = "workdir"

    val dataFile = "$applicationFolder/state.json"

    val outputFolder = "$applicationFolder/output"
    val releaseFileManifest = "$outputFolder/release.json"
    val releaseApk = "$outputFolder/ShTP.apk"

    val tempFolder = "$applicationFolder/temp"
    val tempReleaseManifest = "${tempFolder}/manifest.json"
    val androidRepo = "${tempFolder}/application"
    val releaseInfo = "${androidRepo}/release.md"
    val versionInfo = "${androidRepo}/app/version.txt"
    val tempDebugApk = "${androidRepo}/app/build/outputs/apk/debug/app-debug.apk"

    val waitMinutes = 5

    const val uri = "https://github.com/ITClassDev/Mobile"
}