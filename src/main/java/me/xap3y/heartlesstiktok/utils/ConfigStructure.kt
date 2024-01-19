package me.xap3y.heartlesstiktok.utils


import kotlinx.serialization.Serializable
@Serializable
data class ConfigStructure(
    val tikTokUserName: String,
    val prefix: String,
    val debug: Boolean,
    val isRetryOnConnectionFailure: Boolean,
    val enableNotifyOnJoin: Boolean,
    val connectToStreamOnStartup: Boolean,
    val sessionID: String,
    val permissions: Permissions,
)

@Serializable
data class Permissions(
    val all: String,
    val comment: String,
    val gift: String,
    val allCommands: String,
    val reloadConfig: String,
    val reconnectStream: String,
)