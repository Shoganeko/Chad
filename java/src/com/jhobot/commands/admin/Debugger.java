package com.jhobot.commands.admin;

import com.jhobot.core.ChadBot;
import com.jhobot.handle.Log;
import com.jhobot.handle.LogLevel;
import com.jhobot.handle.MessageHandler;
import com.jhobot.handle.Util;
import com.jhobot.handle.commands.Command;
import com.jhobot.handle.commands.HelpHandler;
import com.jhobot.handle.commands.PermissionLevels;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Debugger implements Command {

    @Override
    public Runnable run(MessageReceivedEvent e, List<String> args) {
        return() -> {
            String domain = args.get(0);
            LogLevel level = LogLevel.INFO;
            if (args.size() > 1) {
                level = LogLevel.valueOf(args.get(1));
                List<Log> logs = ChadBot.DEBUG_HANDLER.getLogs(domain, level);
                EmbedBuilder b = new EmbedBuilder();
                b.withTitle("Internal Log");
                b.withDesc(domain);
                for (Log log : logs) {
                    b.appendField("Level", log.level.toString(), false);
                    b.appendField("Message", log.message, false);
                }
                b.withColor(new Color(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat()));
                b.withFooterText(Util.getTimeStamp());
                new MessageHandler(e.getChannel()).sendEmbed(b.build());
                return;
            }
            List<Log> logs = ChadBot.DEBUG_HANDLER.getLogs(domain, level);
        };
    }

    @Override
    public Runnable help(MessageReceivedEvent e, List<String> args) {
        HashMap<String, String> st = new HashMap<>();
        st.put("debugger <domain> [level]", "Displays the internal logs for the specified domain.");
        return HelpHandler.helpCommand(st, "Debugger", e);
    }

    @Override
    public PermissionLevels level() {
        return PermissionLevels.SYSTEM_ADMINISTRATOR;
    }
}