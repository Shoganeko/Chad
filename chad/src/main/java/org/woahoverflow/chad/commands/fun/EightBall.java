package org.woahoverflow.chad.commands.fun;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.woahoverflow.chad.framework.handle.JsonHandler;
import org.woahoverflow.chad.framework.handle.MessageHandler;
import org.woahoverflow.chad.framework.obj.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Shake the 8ball
 *
 * @author sho
 */
public class EightBall implements Command.Class  {
    @Override
    public final Runnable run(@NotNull MessageEvent e, @NotNull List<String> args) {
        return () -> {
            MessageHandler messageHandler = new MessageHandler(e.getChannel(), e.getAuthor());

            // Makes sure they asked a question
            if (args.isEmpty()) {
                messageHandler.sendError("You didn't ask a question!");
                return;
            }

            // Gets the answers from the cdn
            JSONArray answers = Objects.requireNonNull(JsonHandler.INSTANCE.readArray("https://cdn.woahoverflow.org/data/chad/8ball.json"));

            // Sends the answer
            messageHandler.sendEmbed(
                new EmbedBuilder().withDesc(
                    answers.getString(new SecureRandom().nextInt(Objects.requireNonNull(answers).length()))
                )
            );
        };
    }

    @Override
    public final Runnable help(@NotNull MessageEvent e) {
        HashMap<String, String> st = new HashMap<>();
        st.put("8ball [question]", "The eight ball always answers your best questions.");
        return Command.helpCommand(st, "Eight Ball", e);
    }
}
