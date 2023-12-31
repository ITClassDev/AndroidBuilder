package ru.shtp.androidbuilder.dto

import com.google.gson.annotations.SerializedName

data class ReleaseManifest(
    @SerializedName("change_log")
    val releaseUpdateNotes: Map<String, String>,
    @SerializedName("release_date")
    val releaseEpochDate: Long,
    @SerializedName("version_name")
    val versionName: String,
    @SerializedName("version_tag")
    val versionId: Int
)