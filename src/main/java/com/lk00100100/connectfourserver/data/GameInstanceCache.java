package com.lk00100100.connectfourserver.data;

import com.lk00100100.connectfourserver.GameInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
//todo: ensure concurrency when getting and modifying the object. may need to copy
//TODO: this will be replaced later for more machines.
//todo: flush out old completed games.

/**
 * Holds all the game instances.
 * Ensures concurrency.
 */
public class GameInstanceCache {

    //<unique game id, game instance>
    private final static Map<String, GameInstance> gameInstanceMap = new ConcurrentHashMap<>();

    private final static UniqueIdGenerator idGenerator = new UniqueIdGenerator();

    private GameInstanceCache() {
        //do nothing
    }

    /**
     * Returns a list of a copy of all the gameIds;
     *
     * @return List of gameIds.
     */
    public static List<String> getGameInstanceIds() {
        synchronized (gameInstanceMap) {
            return new ArrayList<>(gameInstanceMap.keySet());
        }
    }

    public static boolean gameInstanceExists(String gameId) {
        return gameInstanceMap.containsKey(gameId);
    }

    public static String createNewGameInstance() {
        String newGameId = "" + idGenerator.getNewId();

        GameInstance gameInstance = new GameInstance(newGameId);

        gameInstanceMap.put(newGameId, gameInstance);

        return newGameId;
    }

    /**
     * Returns the GameInstance or null if not found
     *
     * @param gameId the target GameInstance id;
     * @return GameInstance
     */
    public static GameInstance getGameInstance(String gameId) {
        return gameInstanceMap.get(gameId);
    }

    /**
     * Attempt to grab a seat.
     *
     * @param gameId target game id.
     * @param userId the user
     * @return a positive number representing the player number.
     * -1 if you can't get a seat.
     */
    public static SeatTakenMessage getSeat(String gameId, String userId) {
        GameInstance game = gameInstanceMap.get(gameId);

        if (game == null)
            return new SeatTakenMessage(userId, -1, "Game does not exist.");

        int playerNum;
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (game) {
            playerNum = game.grabSeat(userId);
        }

        if (playerNum == -1)
            return new SeatTakenMessage(userId, playerNum, "Game is full.");

        //is ok
        return new SeatTakenMessage(userId, playerNum, "");
    }

    /**
     * Returns the number of players in the target Game.
     * -1 if invalid.
     *
     * @param gameId Target game id.
     * @return 0 or more players. -1 if invalid.
     */
    public static int numPlayers(String gameId) {
        GameInstance game = gameInstanceMap.get(gameId);

        if (game == null)
            return -1;

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (game) {
            return game.getPlayers().size();
        }
    }

    /**
     * @param gameId unique game id
     * @return true if move was made. false if it was invalid.
     */
    public static boolean makeMove(String gameId, short col, int playerNum) {

        GameInstance game = gameInstanceMap.get(gameId);

        if (game == null)
            return false;

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (game) {
            int rowPlaced = game.placePiece(col, playerNum);
            return rowPlaced != -1;
        }

    }

}
