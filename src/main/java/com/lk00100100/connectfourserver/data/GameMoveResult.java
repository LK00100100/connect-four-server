package com.lk00100100.connectfourserver.data;

/**
 * The server sends this to players of a game room.
 */
public class GameMoveResult {

    public String gameId;   //the current game's id
    public int playerNum;   //the player doing the move
    public int col; //target column

    public boolean wasValid;
    public boolean isGameOver;
}
