package com.lk00100100.connectfourserver.data;

/**
 * Whenever someone sits down, or tries to, in a game,
 * this message is sent to people of that chat room.
 */
public class SeatTakenMessage {

    private String userId;   //the current game's id
    private int playerNum;   //the player doing the move

    public SeatTakenMessage(String userId, int playerNum){
        this.userId = userId;
        this.playerNum = playerNum;
    }

    public String getUserId() {
        return userId;
    }

    public int getPlayerNum() {
        return playerNum;
    }

}
