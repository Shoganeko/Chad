package com.jhobot.commands.fun;

import com.jhobot.core.ChadBot;
import com.jhobot.handle.JSONHandler;
import com.jhobot.handle.MessageHandler;
import com.jhobot.handle.commands.*;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class CatFact implements Command {
    @DefineCommand(category = Category.FUN)
    @Override
    public Runnable run(MessageReceivedEvent e, List<String> args) {
        return () -> {
            try{
                String fact = ChadBot.JSON_HANDLER.read("https://catfact.ninja/fact").getString("fact");
                new MessageHandler(e.getChannel()).send(fact, "Cat Fact");
            } catch (Exception ee)
            {
                ee.printStackTrace();
                new MessageHandler(e.getChannel()).sendError("There was an internal error.");
            }
        };
    }

    @Override
    public Runnable help(MessageReceivedEvent e, List<String> args) {
        HashMap<String, String> st = new HashMap<>();
        st.put("catfact", "Gives you a random catfact.");
        return HelpHandler.helpCommand(st, "Cat Fact", e);
    }
}
