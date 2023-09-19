package ru.shtp.androidbuilder

object Data {
    private val getenv: MutableMap<String, String> = System.getenv()

    val stateFile: String = getenv.getOrDefault("VOLUME_FOLDER", "workdir/state.json")

    val releaseFileManifest: String = getenv.getOrDefault("OUT_RELEASE_JSON", "workdir/output/release.json")
    val releaseApk: String = getenv.getOrDefault("OUT_RELEASE_APK", "workdir/output/ShTP.apk")

    val tempReleaseManifest: String = getenv.getOrDefault("TEMP_RELEASE_JSON", "workdir/temp/manifest.json")
    val androidRepo: String = getenv.getOrDefault("TEMP_RELEASE_APP", "workdir/temp/application")

    val waitMinutes: Double = getenv.getOrDefault("WAIT_MINUTES", "5").toDouble()

    val appReleaseMdsJson = "${androidRepo}/release.json"
    val appVersionJson = "${androidRepo}/app/version.txt"
    val appDebugApk = "${androidRepo}/app/build/outputs/apk/debug/app-debug.apk"

    val gitRepoUrl: String = getenv.getOrDefault("GIT_REPO", "https://github.com/ITClassDev/Mobile")
}