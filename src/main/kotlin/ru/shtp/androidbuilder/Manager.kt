package ru.shtp.androidbuilder

import com.google.gson.Gson
import ru.shtp.androidbuilder.dto.AppStateData
import java.io.File
import java.io.FileReader
import java.io.FileWriter

object Manager {
    val gson = Gson()
    val appState: AppStateData = loadAppState()

    private fun loadAppState():AppStateData {
        val file = File(Data.stateFile)
        file.parentFile.mkdirs()
        return if (!file.exists()) {
            FileWriter(Data.stateFile).use {
                val newValue = AppStateData("")
                gson.toJson(newValue, it)
                newValue
            }
        } else FileReader(Data.stateFile).use {
            gson.fromJson(it, AppStateData::class.java)
        }
    }

    fun saveAppState() = FileWriter(Data.stateFile).use { gson.toJson(appState, it) }



}