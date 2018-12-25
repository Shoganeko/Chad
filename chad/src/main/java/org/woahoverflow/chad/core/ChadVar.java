package org.woahoverflow.chad.core;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import org.woahoverflow.chad.commands.admin.*;
import org.woahoverflow.chad.commands.fun.CatFact;
import org.woahoverflow.chad.commands.fun.CatGallery;
import org.woahoverflow.chad.commands.fun.Cuddle;
import org.woahoverflow.chad.commands.fun.DivorcePlayer;
import org.woahoverflow.chad.commands.fun.DownVote;
import org.woahoverflow.chad.commands.fun.EightBall;
import org.woahoverflow.chad.commands.fun.MarryPlayer;
import org.woahoverflow.chad.commands.fun.PhotoEditor;
import org.woahoverflow.chad.commands.fun.Profile;
import org.woahoverflow.chad.commands.fun.Random;
import org.woahoverflow.chad.commands.fun.RockPaperScissors;
import org.woahoverflow.chad.commands.fun.RussianRoulette;
import org.woahoverflow.chad.commands.info.SteamStatus;
import org.woahoverflow.chad.commands.info.SubscriberCount;
import org.woahoverflow.chad.commands.fun.UpVote;
import org.woahoverflow.chad.commands.fun.WordReverse;
import org.woahoverflow.chad.commands.function.AutoRole;
import org.woahoverflow.chad.commands.function.Logging;
import org.woahoverflow.chad.commands.function.Message;
import org.woahoverflow.chad.commands.function.Nsfw;
import org.woahoverflow.chad.commands.function.Permissions;
import org.woahoverflow.chad.commands.function.Prefix;
import org.woahoverflow.chad.commands.function.Purge;
import org.woahoverflow.chad.commands.function.Swearing;
import org.woahoverflow.chad.commands.gambling.Balance;
import org.woahoverflow.chad.commands.gambling.CoinFlip;
import org.woahoverflow.chad.commands.gambling.DailyReward;
import org.woahoverflow.chad.commands.info.Chad;
import org.woahoverflow.chad.commands.info.ChangeLog;
import org.woahoverflow.chad.commands.info.Contributors;
import org.woahoverflow.chad.commands.info.GuildInfo;
import org.woahoverflow.chad.commands.function.GuildSettings;
import org.woahoverflow.chad.commands.info.Help;
import org.woahoverflow.chad.commands.info.RedditNew;
import org.woahoverflow.chad.commands.info.RedditTop;
import org.woahoverflow.chad.commands.info.Steam;
import org.woahoverflow.chad.commands.info.UserInfo;
import org.woahoverflow.chad.commands.music.Leave;
import org.woahoverflow.chad.commands.music.Pause;
import org.woahoverflow.chad.commands.music.Play;
import org.woahoverflow.chad.commands.music.Queue;
import org.woahoverflow.chad.commands.music.Skip;
import org.woahoverflow.chad.commands.music.Volume;
import org.woahoverflow.chad.commands.nsfw.NB4K;
import org.woahoverflow.chad.commands.nsfw.NBLewdNeko;
import org.woahoverflow.chad.commands.punishments.Ban;
import org.woahoverflow.chad.commands.punishments.Kick;
import org.woahoverflow.chad.framework.handle.JsonHandler;
import org.woahoverflow.chad.framework.obj.Command;
import org.woahoverflow.chad.framework.obj.Command.Category;
import org.woahoverflow.chad.framework.obj.Command.Data;
import org.woahoverflow.chad.framework.obj.GuildMusicManager;
import sx.blah.discord.handle.obj.StatusType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Storing of Static Variables that don't have a home
 *
 * @author sho
 * @since 0.6.3 B2
 */
public final class ChadVar
{
    /**
     * Launch Arguments
     */
    public static final ConcurrentHashMap<String, Boolean> launchOptions = new ConcurrentHashMap<>();

    /**
     * Gigantic Words List
     */
    public static List<String> wordsList = new ArrayList<>();

    /*
    Add all of the available launch options
     */
    static
    {
        launchOptions.put("-denyui", false);
        launchOptions.put("-denyuiupdate", false);
    }

    /**
     * All the swear words for the swear filter
     */
    public static final List<String> swearWords = new ArrayList<>();

    /**
     * All results available for the eight ball command
     */
    public static final List<String> eightBallResults = new ArrayList<>();

    /**
     * The universal player manager for music playing
     */
    public static final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();

    /**
     * The music manager for guilds
     */
    public static final ConcurrentHashMap<Long, GuildMusicManager> musicManagers = new ConcurrentHashMap<>();

    /**
     * The Youtube API Key in the bot.json file
     */
    public static final String YOUTUBE_API_KEY = JsonHandler.handle.get("youtube_api_key");

    /**
     * The Steam API key in the bot.json file
     */
    public static final String STEAM_API_KEY = JsonHandler.handle.get("steam_api_key");

    /*
      Registers sources for the player manager
     */
    static
    {
        AudioSourceManagers.registerRemoteSources(playerManager);
        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        playerManager.registerSourceManager(new SoundCloudAudioSourceManager());
    }

    /**
     * The status type (idle, online, offline, dnd)
     */
    public static StatusType statusType = StatusType.ONLINE;

    /**
     * The current status string
     */
    public static String currentStatus = "";

    /**
     * The amount of time to take to rotate between presences
     */
    public static final int rotationInteger = 60*5; // 5 minutes

    /**
     * If it should rotate presence at all
     */
    public static boolean rotatePresence = true;

    /**
     * The presences to rotate between
     */
    public static final List<String> presenceRotation = new ArrayList<>();

    /**
     * The full list of Commands
     */
    public static final ConcurrentHashMap<String, Command.Data> COMMANDS = new ConcurrentHashMap<>();

    /**
     * List of Verified Developers
     */
    public static final List<Long> DEVELOPERS = new ArrayList<>();

    /*
      Define all of the Commands
     */
    static {
        // FUN!
        COMMANDS.put("random", new Command.Data(Command.Category.FUN, new Random()));
        COMMANDS.put("photoeditor", new Command.Data(Command.Category.FUN, new PhotoEditor(), "pe"));
        COMMANDS.put("eightball", new Command.Data(Command.Category.FUN, new EightBall(), "8ball"));
        COMMANDS.put("catgallery", new Command.Data(Command.Category.FUN, new CatGallery(), "catgal"));
        COMMANDS.put("catfact", new Command.Data(Command.Category.FUN, new CatFact()));
        COMMANDS.put("russianroulette", new Command.Data(Command.Category.FUN, new RussianRoulette(), "rrl"));
        COMMANDS.put("wordreverse", new Command.Data(Command.Category.FUN, new WordReverse(), "wr"));
        COMMANDS.put("rockpaperscissors", new Command.Data(Command.Category.FUN, new RockPaperScissors(), "rps"));

        // COMMUNITY!
        COMMANDS.put("divorce", new Data(Category.COMMUNITY, new DivorcePlayer()));
        COMMANDS.put("marry", new Data(Category.COMMUNITY, new MarryPlayer()));
        COMMANDS.put("cuddle", new Data(Category.COMMUNITY, new Cuddle()));
        COMMANDS.put("profile", new Data(Category.COMMUNITY, new Profile()));
        COMMANDS.put("upvote", new Data(Category.COMMUNITY, new UpVote()));
        COMMANDS.put("downvote", new Data(Category.COMMUNITY, new DownVote()));

        // INFO!
        COMMANDS.put("help", new Command.Data(Command.Category.INFO, new Help()));
        COMMANDS.put("userinfo", new Command.Data(Command.Category.INFO, new UserInfo(), "uinfo"));
        COMMANDS.put("steam", new Command.Data(Command.Category.INFO, new Steam()));
        COMMANDS.put("chad", new Command.Data(Command.Category.INFO, new Chad()));
        COMMANDS.put("guildinfo", new Command.Data(Command.Category.INFO, new GuildInfo(), "ginfo"));
        COMMANDS.put("reddittop", new Command.Data(Command.Category.INFO, new RedditTop(), "rtop"));
        COMMANDS.put("redditnew", new Command.Data(Command.Category.INFO, new RedditNew(), "rnew"));
        COMMANDS.put("contributors", new Command.Data(Command.Category.INFO, new Contributors()));
        COMMANDS.put("changelog", new Data(Category.INFO, new ChangeLog()));
        COMMANDS.put("subcount", new Data(Category.INFO, new SubscriberCount()));
        COMMANDS.put("steamstatus", new Data(Category.INFO, new SteamStatus(), "steamst"));

        // PUNISHMENTS!
        COMMANDS.put("kick", new Command.Data(Command.Category.PUNISHMENTS, new Kick()));
        COMMANDS.put("ban", new Command.Data(Command.Category.PUNISHMENTS, new Ban()));

        // ADMINISTRATOR!
        COMMANDS.put("prefix", new Command.Data(Command.Category.ADMINISTRATOR, new Prefix()));
        COMMANDS.put("logging", new Command.Data(Command.Category.ADMINISTRATOR, new Logging()));
        COMMANDS.put("purge", new Command.Data(Command.Category.ADMINISTRATOR, new Purge()));
        COMMANDS.put("instantmessage", new Command.Data(Command.Category.ADMINISTRATOR, new Message(), "im"));
        COMMANDS.put("autorole", new Command.Data(Command.Category.ADMINISTRATOR, new AutoRole(), "ar"));
        COMMANDS.put("permissions", new Command.Data(Command.Category.ADMINISTRATOR, new Permissions(), "perms"));
        COMMANDS.put("guildsettings", new Data(Category.ADMINISTRATOR, new GuildSettings(), "gset"));
        COMMANDS.put("nsfw", new Command.Data(Command.Category.ADMINISTRATOR, new Nsfw()));
        COMMANDS.put("swearfilter", new Command.Data(Category.ADMINISTRATOR, new Swearing(), "sf"));

        // Nsfw !
        COMMANDS.put("porn", new Command.Data(Command.Category.NSFW, new NB4K(), "4k"));
        COMMANDS.put("lewdneko", new Command.Data(Command.Category.NSFW, new NBLewdNeko(), "neko"));

        // DEVELOPER!
        COMMANDS.put("threads", new Command.Data(Command.Category.DEVELOPER, new CurrentThreads()));
        COMMANDS.put("modpresence", new Command.Data(Command.Category.DEVELOPER, new ModifyPresence()));
        COMMANDS.put("systeminfo", new Command.Data(Command.Category.DEVELOPER, new SystemInfo()));
        COMMANDS.put("shutdown", new Command.Data(Command.Category.DEVELOPER,new Shutdown()));
        COMMANDS.put("setbalance", new Command.Data(Command.Category.DEVELOPER, new SetBalance(), "setbal"));
        COMMANDS.put("devcfg", new Command.Data(Category.DEVELOPER, new DeveloperSettings(), "cfg"));

        // GAMBLING!
        COMMANDS.put("coinflip", new Command.Data(Command.Category.GAMBLING, new CoinFlip(), "cf"));
        COMMANDS.put("balance", new Command.Data(Command.Category.GAMBLING, new Balance(), "bal"));
        COMMANDS.put("dailyreward", new Data(Category.GAMBLING, new DailyReward(), "drw"));

        // MUSIC
        COMMANDS.put("play", new Data(Category.MUSIC, new Play()));
        COMMANDS.put("pause", new Data(Category.MUSIC, new Pause()));
        COMMANDS.put("leave", new Data(Category.MUSIC, new Leave()));
        COMMANDS.put("skip", new Data(Category.MUSIC, new Skip()));
        COMMANDS.put("queue", new Data(Category.MUSIC, new Queue()));
        COMMANDS.put("volume", new Data(Category.MUSIC, new Volume()));
    }
}
