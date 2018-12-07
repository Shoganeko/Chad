package org.woahoverflow.chad.commands.music;

import java.util.List;
import org.woahoverflow.chad.framework.Chad;
import org.woahoverflow.chad.framework.Command;
import org.woahoverflow.chad.framework.handle.MessageHandler;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * @author sho
 * @since 0.7.0
 */
public class Volume implements Command.Class{
    @Override
    public Runnable run(MessageReceivedEvent e, List<String> args) {
        return () -> {
            // Makes sure the value is a valid integer
            int volume;
            try {
                volume = Integer.parseInt(args.get(0));
            } catch (NumberFormatException throwaway) {
                new MessageHandler(e.getChannel()).sendError("Invalid Volume!");
                return;
            }

            MessageHandler messageHandler = new MessageHandler(e.getChannel());

            // Make sure the value isn't negative
            if (0 > volume)
            {
                messageHandler.sendError("Please don't use negative numbers!");
                return;
            }

            // Makes sure the value isn't over 1000
            if (1000 > volume)
            {
                messageHandler.sendError("Please use values below 1000!\nPersonally, I suggest you use `100`. Anything above is distorted.");
                return;
            }

            // Sets the volume
            Chad.getMusicManager(e.getGuild()).player.setVolume(volume);

            messageHandler.sendMessage("Set the volume to `"+volume+"`!");
        };
    }

    @Override
    public Runnable help(MessageReceivedEvent e) {
        return null;
    }
}