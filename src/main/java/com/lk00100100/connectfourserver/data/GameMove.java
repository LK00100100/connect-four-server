package com.lk00100100.connectfourserver.data;

/**
 * A player sends this "move" for the server to process.
 */
public class GameMove {

    public String gameId;   //the current game's id
    public int playerNum;   //the player doing the move
    public int col; //target column

}
