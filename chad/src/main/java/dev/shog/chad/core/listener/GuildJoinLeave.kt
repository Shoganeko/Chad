package dev.shog.chad.core.listener

import dev.shog.chad.core.getLogger
import dev.shog.chad.framework.handle.GuildHandler
import dev.shog.chad.framework.handle.PlayerHandler
import dev.shog.chad.framework.handle.database.DatabaseManager
import dev.shog.chad.framework.obj.Player.DataType
import org.json.JSONArray
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent
import sx.blah.discord.handle.impl.events.guild.GuildLeaveEvent
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.handle.obj.Permissions
import sx.blah.discord.util.RequestBuffer
import java.util.*
import kotlin.collections.ArrayList

/**
 * The Discord guild join and leave events
 *
 * @author sho
 */
class GuildJoinLeave {
    /**
     * Discord's Joining Guild Event
     *
     * @param event Guild Create Event
     */
    @EventSubscriber
    fun joinGuild(event: GuildCreateEvent) {
        // Makes sure all users are into the database
        Thread {
            val users = RequestBuffer.request<List<IUser>> { event.guild.users }.get()
            for (user in users) {
                val player = PlayerHandler.getPlayer(user.longID)
                val guildData = JSONArray(player.getObject(DataType.GUILD_DATA) as String)

                if (!guildData.contains(event.guild.longID)) {
                    guildData.put(event.guild.longID)
                    player.setObject(DataType.GUILD_DATA, guildData.toString())
                }
            }
        }.run()

        if (DatabaseManager.GUILD_DATA.getObject(event.guild.longID) == null) {
            // By retrieving the guild's instance, it creates an instance for the guild within the database
            GuildHandler.getGuild(event.guild.longID)

            // Send a log with the new guild
            getLogger().debug('['.toString() + event.guild.stringID + "] Joined Guild")

            // The guild's default channel
            val defaultChannel = RequestBuffer.request<IChannel> { event.guild.defaultChannel }.get()

            // The join message
            val joinMessage = ("Hello, i'm Chad!\n"
                    + "My prefix is `c!`\n\n"
                    + "View my commands with `c!help`")

            // If the bot has permission to, send the join message into the default channel
            if (RequestBuffer.request<Boolean> { defaultChannel.getModifiedPermissions(event.client.ourUser).contains(Permissions.SEND_MESSAGES) }.get())
                RequestBuffer.request<IMessage> { event.guild.defaultChannel.sendMessage(joinMessage) }
            else {
                // Parse through all of the guilds, and if the bot has permission send the join message.
                val guilds = RequestBuffer.request<List<IChannel>> { event.guild.channels }.get()
                val channelSize = guilds.size

                var i = 0
                while (channelSize > i) {
                    val channel = guilds[0]

                    if (RequestBuffer.request<Boolean> { channel.getModifiedPermissions(event.client.ourUser).contains(Permissions.SEND_MESSAGES) }.get()) {
                        RequestBuffer.request<IMessage> { channel.sendMessage(joinMessage) }
                    }
                    i++
                }
            }
        }
    }

    /**
     * Discord's Leave Guild Event
     *
     * @param event Guild Leave Event
     */
    @EventSubscriber
    fun leaveGuild(event: GuildLeaveEvent) {
        // Removed the guild's cached document
        GuildHandler.removeGuild(event.guild.longID)

        // Send a log
        getLogger().debug('<'.toString() + event.guild.stringID + "> Left Guild")
    }
}
