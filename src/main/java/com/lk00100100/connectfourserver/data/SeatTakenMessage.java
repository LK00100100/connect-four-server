package com.lk00100100.connectfourserver.data;

/**
 * Whenever someone sits down, or tries to, in a game,
 * this message is sent to people of that chat room.
 */
public class SeatTakenMessage {

    private String userId;  //the current game's id
    private int playerNum;  //the player doing the move. < 0, means no seat.
    private String message; //if no seat was taken, this will be filled.

    public SeatTakenMessage(String userId, int playerNum) {
        this.userId = userId;
        this.playerNum = playerNum;
        this.message = "";
    }

    public SeatTakenMessage(String userId, int playerNum, String message) {
        this(userId, playerNum);
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public String getMessage() {
        return message;
    }
}
