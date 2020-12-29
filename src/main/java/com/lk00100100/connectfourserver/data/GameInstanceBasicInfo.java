package com.lk00100100.connectfourserver.data;

/**
 * Contains basic information about this game instance;
 */
public class GameInstanceBasicInfo {

    public String gameId;
    public int numPlayers;   //the player doing the move
    public GameState state;

    public GameInstanceBasicInfo(String gameId){
        this.gameId = gameId;
    }
}
