package com.lk00100100.connectfourserver.data;

import com.lk00100100.connectfourserver.GameInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
//todo: ensure concurrency when getting and modifying the object. may need to copy
//TODO: this will be replaced later for more machines.

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

    public static int getSeat(String gameId, String userId) {
        GameInstance game = gameInstanceMap.get(gameId);

        if (game == null)
            return -1;

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (game) {
            return game.grabSeat(userId);
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
