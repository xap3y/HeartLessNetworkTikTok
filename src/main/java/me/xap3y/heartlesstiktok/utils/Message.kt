package me.xap3y.heartlesstiktok.utils

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class Message {
    companion object{
        fun info(message: String) {
            Bukkit.getConsoleSender().sendMessage(colored(message))
        }

        fun send(player: Player, message: String) {
            player.sendMessage(colored(message))
        }

        fun colored(message: String): String {
            return ChatColor.translateAlternateColorCodes('&', message)
        }
    }
}