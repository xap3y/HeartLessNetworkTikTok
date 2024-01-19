package me.xap3y.heartlesstiktok.utils


import org.bukkit.entity.Player
import kotlin.collections.HashMap

class PlayerNotifyDB {
    private val playerSettings: HashMap<String, Boolean> = HashMap()
    fun getSetting(player: Player): Boolean {
        return playerSettings.getOrDefault(player.uniqueId.toString(), false)
    }

    fun setSetting(player: Player, value: Boolean) {
        playerSettings[player.uniqueId.toString()] = value
    }

    fun hasSetting(player: Player): Boolean {
        return playerSettings.containsKey(player.uniqueId.toString())
    }
}