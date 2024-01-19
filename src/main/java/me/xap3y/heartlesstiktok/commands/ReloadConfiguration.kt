package me.xap3y.heartlesstiktok.commands

import me.xap3y.heartlesstiktok.utils.ConfigStructure
import me.xap3y.heartlesstiktok.utils.Message
import me.xap3y.heartlesstiktok.utils.PlayerNotifyDB
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class ReloadConfiguration(private val plugin: JavaPlugin, private val config: ConfigStructure, private val playerNotifyDB: PlayerNotifyDB): CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, arg: String, args: Array<out String>?): Boolean {
        if (sender is Player && (sender.hasPermission(config.permissions.reloadConfig) || sender.hasPermission(config.permissions.all) || sender.hasPermission(config.permissions.allCommands))) {
            if(!playerNotifyDB.hasSetting(sender.player!!)) playerNotifyDB.setSetting(sender, false)

            if (!args.isNullOrEmpty() && args[0] == "toggle") {
                if (playerNotifyDB.getSetting(sender.player!!)) {
                    playerNotifyDB.setSetting(sender.player!!, false)
                    Message.send(sender.player!!, "${config.prefix}&fNotifications turned &cOFF")
                } else {
                    playerNotifyDB.setSetting(sender, true)
                    Message.send(sender.player!!, "${config.prefix}&fNotifications turned &aON")
                }
            } else if (!args.isNullOrEmpty() && args[0] == "reload") {
                plugin.reloadConfig()
                Message.send(sender.player!!, "${config.prefix}&aConfiguration reloaded!")
            } else {
                Message.send(sender.player!!, "${config.prefix}&cWrong usage of /ht command! &8(/ht <toggle|reload>)")
            }
        } else if (sender !is Player) {
            plugin.reloadConfig()
            Message.info("${config.prefix}&aConfiguration reloaded!")
        } else {
            Message.info("${config.prefix}&cYou don't have permission to do that!")
            return false
        }
        return true
    }
}