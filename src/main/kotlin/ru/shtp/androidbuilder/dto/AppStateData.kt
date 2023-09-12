package ru.shtp.androidbuilder.dto


import com.google.gson.annotations.SerializedName

data class AppStateData(
    @SerializedName("checked_version")
    var checkedVersion: String,
)