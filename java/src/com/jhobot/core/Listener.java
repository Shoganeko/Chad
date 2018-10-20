package com.jhobot.core;

import com.jhobot.handle.Messages;
import com.jhobot.handle.commands.CommandHandler;
import com.jhobot.handle.DB;
import com.jhobot.handle.JSON;
import com.jhobot.handle.Util;
import com.jhobot.handle.ui.UIHandler;
import org.bson.Document;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.GuildLeaveEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Timer;

class Listener
{
    @EventSubscriber
    public void messageRecieved(MessageReceivedEvent e)
    {
        CommandHandler.call(e);
    }

    @EventSubscriber
    public void userJoin(UserJoinEvent e)
    {
        IGuild g = e.getGuild();
        Messages m = new Messages(null);
        EmbedBuilder b = new EmbedBuilder();
        Date date = Date.from(e.getGuild().getCreationDate());
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        b.withTitle("User Join : " + e.getUser().getName())
                .withFooterIcon(e.getUser().getAvatarURL())
                .withFooterText(Util.getTimeStamp())
                .appendField("Join Time", format.format(date), true);

        m.sendLog(b.build(), JhoBot.db, g);

        String joinMsgCh = JhoBot.db.getString(e.getGuild(), "join_message_ch");

        if (!JhoBot.db.getBoolean(e.getGuild(), "join_msg_on"))
            return;
        if (joinMsgCh.equalsIgnoreCase("none"))
            return;

        Long id = Long.parseLong(joinMsgCh);
        IChannel ch = RequestBuffer.request(() -> g.getChannelByID(id)).get();

        if (ch.isDeleted())
            return;

        String msg = JhoBot.db.getString(e.getGuild(), "join_message");
        msg = msg.replaceAll("&user&", e.getUser().getName()).replaceAll("&guild&", e.getGuild().getName());
        new Messages(ch).sendMessage(msg);
    }

    @EventSubscriber
    public void userLeave(UserLeaveEvent e)
    {
        IGuild g = e.getGuild();
        Messages m = new Messages(null);
        EmbedBuilder b = new EmbedBuilder();
        Date date = Date.from(e.getGuild().getCreationDate());
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        b.withTitle("User Leave : " + e.getUser().getName())
                .withFooterIcon(e.getUser().getAvatarURL())
                .withFooterText(Util.getTimeStamp())
                .appendField("Leave Time", format.format(date), true);

        m.sendLog(b.build(), JhoBot.db, g);

        String leaveMsgCh = JhoBot.db.getString(e.getGuild(), "leave_message_ch");

        if (!JhoBot.db.getBoolean(e.getGuild(), "leave_msg_on"))
            return;
        if (leaveMsgCh.equalsIgnoreCase("none"))
            return;

        Long id = Long.parseLong(leaveMsgCh);
        IChannel ch = RequestBuffer.request(() -> g.getChannelByID(id)).get();

        if (ch.isDeleted())
            return;

        String msg = JhoBot.db.getString(e.getGuild(), "leave_message");
        msg = msg.replaceAll("&user&", e.getUser().getName()).replaceAll("&guild&", e.getGuild().getName());
        new Messages(ch).sendMessage(msg);
    }

    @EventSubscriber
    public void joinGuild(GuildCreateEvent e)
    {
        DB dbb = new DB(JSON.get("uri_link"));
        if (!dbb.exists(e.getGuild()))
        {
            Document doc = new Document();

            doc.append("guildid", e.getGuild().getStringID());
            doc.append("prefix", "j!");
            if (!e.getClient().getOurUser().getPermissionsForGuild(e.getGuild()).contains(Permissions.MANAGE_ROLES))
                doc.append("muted_role", "none_np");
            else
                doc.append("muted_role", "none");
            doc.append("muted_role", "none");
            doc.append("logging", false);
            doc.append("logging_channel", "none");
            doc.append("cmd_requires_admin", false);
            doc.append("music_requires_admin", false);
            doc.append("role_on_join", false);
            doc.append("join_role", "none");
            doc.append("ban_message", "You have been banned from &guild&. \n &reason&");
            doc.append("kick_message", "You have been kicked from &guild&. \n &reason&");
            doc.append("allow_level_message", false);
            doc.append("allow_leveling", false);
            doc.append("join_message", "`&user&` has joined the guild!");
            doc.append("leave_message", "`&user&` has left the guild!");
            doc.append("join_msg_on", false);
            doc.append("leave_msg_on", false);
            doc.append("ban_msg_on", true);
            doc.append("kick_msg_on", true);
            doc.append("join_message_ch", "none");
            doc.append("leave_message_ch", "none");

            dbb.getCollection().insertOne(doc);

            System.out.println(Util.getTimeStamp() + " <" + e.getGuild().getStringID() + "> Joined Guild");
        }
    }

    @EventSubscriber
    public void leaveGuild(GuildLeaveEvent e)
    {
        DB db = new DB(JSON.get("uri_link"));
        Document get = db.getCollection().find(new Document("guildid", e.getGuild().getStringID())).first();

        if (get == null)
            return;

        db.getCollection().deleteOne(get);

        System.out.println(Util.getTimeStamp() + " <" + e.getGuild().getStringID() + "> Left Guild");
    }

    @EventSubscriber
    public void onReadyEvent(ReadyEvent e)
    {
        JhoBot.exec.execute(() -> {
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    String[] ar = {"hello!", "gamers", "epic gamers", "a bad game", "j!help", "j!prefix set *", "what's going on gamers", "invite me please"};
                    e.getClient().changePresence(StatusType.ONLINE, ActivityType.PLAYING, ar[new Random().nextInt(ar.length)]);
                }
            }, 0, 60000*5);
        });

        JhoBot.exec.execute(() -> {
            Timer t = new Timer();
            UIHandler ui = new UIHandler(e.getClient());
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    ui.update();
                }
            }, 0, 60000*5);
            ui.getPanel().getRefreshButton().addActionListener((ActionEvent) ->  ui.update());
        });
    }
}