package me.xap3y.heartlesstiktok

import com.sun.org.apache.xpath.internal.operations.Bool
import io.github.jwdeveloper.tiktok.TikTokLive
import io.github.jwdeveloper.tiktok.data.events.TikTokCommentEvent
import io.github.jwdeveloper.tiktok.data.events.TikTokConnectedEvent
import io.github.jwdeveloper.tiktok.data.events.TikTokErrorEvent
import io.github.jwdeveloper.tiktok.data.events.gift.TikTokGiftEvent
import io.github.jwdeveloper.tiktok.data.models.gifts.Gift
import io.github.jwdeveloper.tiktok.live.LiveClient
import io.github.jwdeveloper.tiktok.live.builder.LiveClientBuilder
import me.xap3y.heartlesstiktok.commands.ReloadConfiguration
import me.xap3y.heartlesstiktok.utils.ConfigStructure
import me.xap3y.heartlesstiktok.utils.InfoPlayers.Companion.notifyGift
import me.xap3y.heartlesstiktok.utils.InfoPlayers.Companion.notifyMessage
import me.xap3y.heartlesstiktok.utils.LoadConfig.Companion.loadConfig
import me.xap3y.heartlesstiktok.utils.Message
import me.xap3y.heartlesstiktok.utils.PlayerNotifyDB
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.lang.Exception
import java.time.Duration


class Main : JavaPlugin() {
    private lateinit var config: ConfigStructure
    private val configFile = File(dataFolder, "config.json")
    private var client: LiveClient? = null
    private lateinit var playerNotifyDB: PlayerNotifyDB
    override fun onEnable() {
        reloadConfig()
        try {
            client = createClient().buildAndConnect()
        } catch (e: Exception) {
            if (config.debug) e.message?.let { Message.info(it) }
        }

        playerNotifyDB = PlayerNotifyDB()
        getCommand("ht")?.setExecutor(ReloadConfiguration(this, config, playerNotifyDB))
    }

    /*override fun onDisable() {
        if (client !== null) {
            client?.disconnect()
            client = null
        }
    }*/

    override fun reloadConfig() {
        if(!dataFolder.exists()) dataFolder.mkdir()
        val configData = loadConfig(configFile)
        if (configData === null) {
            if (config.debug) Message.info("&cFailed to reload configuration!")
            return
        }
        configData.let {
            config = it
        }
    }
    private fun createClient(): LiveClientBuilder {
        return TikTokLive.newClient(config.tikTokUserName)
            .configure {
                it.isRetryOnConnectionFailure = config.isRetryOnConnectionFailure
                it.retryConnectionTimeout = Duration.ofSeconds(5)
                it.sessionId = config.sessionID
            }
            .onGift { _: LiveClient?, event: TikTokGiftEvent ->
                val message = when (event.gift) {
                    Gift.ROSE -> "&4ROSE!"
                    // Add other

                    else -> event.gift.getName()
                }
                notifyGift(event.user.name, message, config, playerNotifyDB)
            }
            .onComment { liveClient: LiveClient?, event: TikTokCommentEvent?  ->
                if (liveClient != null && event != null) {
                    notifyMessage(event.user.profileName, event.text, config, playerNotifyDB)
                }
            }
            .onConnected { _: LiveClient?, _: TikTokConnectedEvent? ->
                if (config.debug) logger.info("[TT] ${config.tikTokUserName}")
            }
            .onError { liveClient: LiveClient?, event: TikTokErrorEvent? ->
                if (liveClient != null && event != null) {
                    if (config.debug) logger.info("[TT ERR] ${event.exception.message}")
                }
            }
    }
}
