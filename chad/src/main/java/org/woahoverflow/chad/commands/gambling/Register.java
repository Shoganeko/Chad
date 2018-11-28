package org.woahoverflow.chad.commands.gambling;

import org.woahoverflow.chad.core.ChadVar;
import org.woahoverflow.chad.handle.MessageHandler;
import org.woahoverflow.chad.handle.commands.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.List;

// command registers user into the database, specifically into their guild's mongodb
public class Register implements Command.Class {
    @Override
    public final Runnable run(MessageReceivedEvent e, List<String> args) {
        return () -> {
            if (ChadVar.databaseDevice.contains(e.getGuild(), e.getAuthor().getStringID() + "_balance"))
            {
                new MessageHandler(e.getChannel()).sendError("You've already got an account!");
                return;
            }

            ChadVar.databaseDevice.set(e.getGuild(), e.getAuthor().getStringID() + "_balance", 0L);
            new MessageHandler(e.getChannel()).send("You've now got an account!", "Money");
        };
    }

    @Override
    public final Runnable help(MessageReceivedEvent e) {
        HashMap<String, String> st = new HashMap<>();
        st.put("register", "Registers your account with Chad.");
        return Command.helpCommand(st, "Register", e);
    }
}