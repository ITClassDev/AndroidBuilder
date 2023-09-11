package ru.shtp.androidbuilder

import com.google.gson.annotations.SerializedName

data class ReleaseManifest(
    @SerializedName("change_log")
    val releaseUpdateNotes: String,
    @SerializedName("release_date")
    val releaseEpochDate: Long,
    @SerializedName("version_name")
    val versionName: String,
    @SerializedName("version_tag")
    val versionId: Int
)