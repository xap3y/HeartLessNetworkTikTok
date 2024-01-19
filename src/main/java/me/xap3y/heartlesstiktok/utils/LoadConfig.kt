package me.xap3y.heartlesstiktok.utils

import com.google.gson.Gson
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileReader

class LoadConfig {
    companion object {
        private val gson = Gson()
        private val defaultConfig = ConfigStructure(
            tikTokUserName = "xap3y.dev",
            prefix = "&7[&6TT&7] &r",
            debug = true,
            isRetryOnConnectionFailure = true,
            enableNotifyOnJoin = true,
            connectToStreamOnStartup = true,
            sessionID = "",
            permissions = Permissions(
                "heartLessTT.*",
                "heartLessTT.comment",
                "heartLessTT.gift",
                "heartLessTT.command.*",
                "heartLessTT.command.reload",
                "heartLessTT.command.reconnect",
            )
        )

        @JvmStatic
        fun loadConfig(file: File): ConfigStructure? {
            if (!file.exists()) {
                val json = Json { prettyPrint = true }
                val jsonString = json.encodeToString(defaultConfig)
                try {
                    file.writeText(jsonString)
                } catch (ex: Exception) {
                    Message.info("&cThere was error creating config file!")
                }
            }

            return try {
                FileReader(file).use { reader ->
                    gson.fromJson(reader, ConfigStructure::class.java)
                }
            } catch (ex: Exception) {
                Message.info("&cThere was error creating config file! Using default config.")
                defaultConfig
            }
        }
    }
}