package me.xap3y.heartlesstiktok.utils

import org.bukkit.Bukkit
import org.bukkit.Sound

class InfoPlayers {
    companion object {
        private val players = Bukkit.getOnlinePlayers()
        fun notifyMessage(username: String, message: String, config: ConfigStructure, playerNotifyDB: PlayerNotifyDB) {
            players.forEach { player ->
                if (playerNotifyDB.getSetting(player) && (player.hasPermission(config.permissions.all) || player.hasPermission(config.permissions.comment))) {
                    Message.send(player, "${config.prefix}&9$username &7>> &f$message")
                } else if ((player.hasPermission(config.permissions.all) || player.hasPermission(config.permissions.comment)) && !playerNotifyDB.hasSetting(player)) {
                    if (config.enableNotifyOnJoin) {
                        playerNotifyDB.setSetting(player, true)
                        Message.send(player, "${config.prefix}&9$username &7>> &f$message")
                    }
                }
            }
        }

        fun notifyGift(username: String, gift: String, config: ConfigStructure, playerNotifyDB: PlayerNotifyDB) {
            players.forEach { player ->
                if (playerNotifyDB.getSetting(player) && (player.hasPermission(config.permissions.all) || player.hasPermission(config.permissions.gift))) {
                    Message.send(player, "${config.prefix}&6&kOO &r&a&l$username&r &e&kOO &7sent &6$gift")
                    player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
                    player.sendTitle(
                        Message.colored("&a&l$username"),
                        Message.colored("&6$gift"),
                        1, 30, 1)
                } else if ((player.hasPermission(config.permissions.all) || player.hasPermission(config.permissions.gift)) && !playerNotifyDB.hasSetting(player)) {
                    if (config.enableNotifyOnJoin) {
                        playerNotifyDB.setSetting(player, true)
                    }
                }
            }
        }
    }
}