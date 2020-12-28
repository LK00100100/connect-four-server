package com.lk00100100.connectfourserver.data;

/**
 * The server sends this to players of a game room.
 */
public class GameMoveResult {

    public int playerNum;   //the player doing the move
    public int col;         //target column
    public int rowPlaced;   //the piece was placed here.

    public boolean wasValid;
    public boolean wasWinning;   //this was the winning move
    public boolean isBoardFull;

    public GameMoveResult(int playerNum, int col) {
        this.playerNum = playerNum;
        this.col = col;
    }
}
