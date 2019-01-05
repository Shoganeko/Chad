package org.woahoverflow.chad.framework.handle;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.woahoverflow.chad.framework.handle.database.DatabaseManager;
import org.woahoverflow.chad.framework.obj.Player;
import org.woahoverflow.chad.framework.obj.Player.DataType;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages Player instances
 *
 * @author sho, codebasepw
 */
public class PlayerHandler {
    /**
     * The public instance
     */
    public static final PlayerHandler handle = new PlayerHandler();

    /**
     * The local cached players
     */
    private final ConcurrentHashMap<Long, Player> players = new ConcurrentHashMap<>();

    /**
     * If a player exists within the hash-map
     *
     * @param player The player to check with
     * @return If it contains it
     */
    public boolean playerExists(long player) {
        return players.containsKey(player);
    }

    /**
     * Refreshes a player into the hashmap
     *
     * @param user The user to refresh
     */
    public void refreshPlayer(long user) {
        if (players.keySet().contains(user))
            players.put(user, getPlayer(user));
    }

    /**
     * Removes a player dataset from the database
     *
     * @param user The user to remove
     */
    public void removePlayer(long user) {
        // Removes it from the hashmap
        players.remove(user);

        // Removes it from the database
        MongoCollection<Document> col = DatabaseManager.USER_DATA.collection;

        Document get = col.find(new Document("id", user)).first();

        if (get == null)
            return;
        col.deleteOne(get);
    }

    /**
     * Creates a player
     *
     * @param user The user's ID to register
     */
    private Player createPlayer(long user) {
        Document playerDocument = new Document();

        // The user's ID
        playerDocument.put("id", user);

        // The user's balance
        playerDocument.put("balance", 0L);

        // Other default user data
        playerDocument.put("marry_data", "none&none");
        playerDocument.put("profile_description", "No description!");
        playerDocument.put("profile_title", "none");
        playerDocument.put("profile_downvote", 0L);
        playerDocument.put("profile_upvote", 0L);

        playerDocument.put("guild_data", new ArrayList<>());
        playerDocument.put("vote_data", new ArrayList<>());

        playerDocument.put("last_daily_reward", "none");

        // Insert the new player=
        DatabaseManager.USER_DATA.collection.insertOne(playerDocument);

        // The player
        Player player = parsePlayer(playerDocument, user);

        // Add it into the hash map
        players.put(user, player);

        // Return the new player
        return player;
    }

    /**
     * Gets a player's data from the database
     *
     * @param playerDataDocument The player to get
     * @return The player retrieved
     */
    private Player parsePlayer(Document playerDataDocument, long user) {
        final ConcurrentHashMap<DataType, Object> playerData = new ConcurrentHashMap<>();

        // Sets the data
        for (DataType type : DataType.values()) {
            if (playerDataDocument.get(type.toString().toLowerCase()) == null)
            System.out.println(type.toString().toLowerCase());
            playerData.put(type, playerDataDocument.get(type.toString().toLowerCase()));
        }

        return new Player(playerData, user);
    }

    /**
     * Gets a player instance
     *
     * @return The user's Player instance
     */
    public Player getPlayer(long user) {
        // If the user's in in the hash map, return it
        if (players.containsKey(user)) {
            return players.get(user);
        }

        if (userDataExists(user)) {
            Document get = DatabaseManager.USER_DATA.collection.find(new Document("id", user)).first();

            if (get == null)
                return null;

            Player player = parsePlayer(get, user);

            players.put(user, player);

            return player;
        }

        Player player = createPlayer(user);

        players.put(user, player);

        // If it's not in there, create one
        return player;
    }

    /**
     * Checks if a user's data already exists within the database
     *
     * @param user The user to check
     * @return If it exists
     */
    private boolean userDataExists(long user) {
        Document get = DatabaseManager.USER_DATA.collection.find(new Document("id", user)).first();
        return get != null;
    }
}
