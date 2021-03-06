package dev.shog.chad.commands.`fun`

import dev.shog.chad.framework.handle.JsonHandler
import dev.shog.chad.framework.handle.MessageHandler
import dev.shog.chad.framework.obj.Command
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEvent
import java.util.*

/**
 * Gets a random cat fact from an API
 *
 * @author sho
 */
class CatFact : Command.Class {
    override suspend fun run(e: MessageEvent, args: MutableList<String>) {
        JsonHandler.read("https://catfact.ninja/fact")!!
                .getString("fact")
                .also { MessageHandler(e.channel, e.author).credit("catfact.ninja").sendEmbed { withDesc(it) } }
    }

    override suspend fun help(e: MessageEvent) {
        val st = HashMap<String, String>()
        st["catfact"] = "Gives you a random catfact."
        Command.helpCommand(st, "Cat Fact", e)
    }
}
