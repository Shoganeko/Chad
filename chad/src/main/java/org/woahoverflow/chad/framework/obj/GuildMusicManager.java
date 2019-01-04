package org.woahoverflow.chad.framework.obj;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import org.woahoverflow.chad.framework.handle.TrackScheduler;

/**
 * @author sho
 */
public class GuildMusicManager {
    /**
     * The Guild's Audio Player
     */
    public final AudioPlayer player;

    /**
     * The Guild's TrackScheduler
     */
    public final TrackScheduler scheduler;

    /**
     * Creates a Guild Music Manager
     *
     * @param manager The manager
     * @param guildId The guild's ID
     */
    public GuildMusicManager(AudioPlayerManager manager, long guildId) {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player, guildId);

        player.addListener(scheduler);
    }

    /**
     * @return The guild's audio provider
     */
    public AudioProvider getAudioProvider()
    {
        return new AudioProvider(player);
    }

    /**
     * Clear the current queue and stop the current track
     */
    public void clear() {
        scheduler.queue.clear();
        player.stopTrack();
    }
}
