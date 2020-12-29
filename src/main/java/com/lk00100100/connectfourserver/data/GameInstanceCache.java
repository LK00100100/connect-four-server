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
     * @return List of GameInstanceBasicInfo.
     */
    public static List<GameInstanceBasicInfo> getGameInstanceBasicInfoList() {

        List<GameInstanceBasicInfo> infoList = new ArrayList<>();
        //note lkeh: this can't be efficient :-/ could use r/w locks
        synchronized (gameInstanceMap) {
            for (Map.Entry<String, GameInstance> pair : gameInstanceMap.entrySet()) {
                String gameId = pair.getKey();
                GameInstance game = pair.getValue();

                GameInstanceBasicInfo info = new GameInstanceBasicInfo(gameId);

                //noinspection SynchronizationOnLocalVariableOrMethodParameter
                synchronized (game) {
                    info.numPlayers = game.getPlayers().size();
                    info.state = game.getGameState();
                }

                infoList.add(info);
            }
        }

        return infoList;
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
        //todo: concurrency safe?
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
    public static int getNumPlayers(String gameId) {
        GameInstance game = gameInstanceMap.get(gameId);

        if (game == null)
            return -1;

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (game) {
            return game.getPlayers().size();
        }
    }

    /**
     * Get a target GameInstance's GameState
     *
     * @param gameId Target GameInstance id.
     * @return returns the GameState or null if no instance was found.
     */
    public static GameState getGameState(String gameId) {
        GameInstance game = gameInstanceMap.get(gameId);

        if (game == null)
            return null;

        GameState gameState;
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (game) {
            gameState = game.getGameState();
            gameState = GameState.valueOf(gameState.name());
            //note lkeh: copy for concurrency.
        }

        return gameState;
    }

    /**
     * Changes a target GameInstance's GameState.
     *
     * @param gameId   target gameId
     * @param newState new GameState to set to
     * @return true if changed. false otherwise (due to not found);
     */
    public static boolean setGameInstanceState(String gameId, GameState newState) {
        GameInstance game = gameInstanceMap.get(gameId);

        if (game == null)
            return false;

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (game) {
            game.setGameState(newState);
        }

        return true;
    }

    /**
     * Returns a GameMoveResult. If valid, it makes the move.
     * Sets result to invalid on bad or invalid move or invalid game.
     *
     * @param gameId unique game id
     * @return true if move was made. false if it was invalid.
     */
    public static GameMoveResult makeMove(String gameId, GameMove move) {
        //TODO: check request order? sync'd?
        GameInstance game = gameInstanceMap.get(gameId);

        GameMoveResult result = new GameMoveResult(move.playerNum, move.col);

        if (game == null)
            return result;

        int rowPlaced;
        boolean wasVictory, isBoardFull;

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (game) {
            rowPlaced = game.makeMove(move.playerNum, move.col);

            if (rowPlaced == -1)
                return result;

            wasVictory = game.checkVictoryQuick(rowPlaced, move.col);
            isBoardFull = game.isBoardFull();
        }

        result.rowPlaced = rowPlaced;
        result.wasValid = true;
        result.wasWinning = wasVictory;
        result.isBoardFull = isBoardFull;

        return result;
    }

}
