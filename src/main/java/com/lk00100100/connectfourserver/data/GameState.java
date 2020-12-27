package com.lk00100100.connectfourserver.data;

/**
 * The current state of the GameInstance.
 * Waiting for others, playing, or ended.
 */
public enum GameState {
    WAIT,   //waiting for the other player
    PLAY,   //playing
    END     //game over
}
