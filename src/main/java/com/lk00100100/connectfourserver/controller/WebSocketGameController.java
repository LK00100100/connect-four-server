package com.lk00100100.connectfourserver.controller;

import com.lk00100100.connectfourserver.GameInstance;
import com.lk00100100.connectfourserver.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Websocket Game Controller.
 * Connect to this and get actual game data moved to/from the server.
 */
@Controller
public class WebSocketGameController {

    //TODO: handle game disconnects. send victory

    //TODO: auth/z

    @Autowired
    private SimpMessagingTemplate simpTemplate;


    /**
     * Attempt to grab a game seat (player number) from a game.
     *
     * @param gameId target game id
     * @return a positive player number. -1 if bad.
     */
    @MessageMapping("/game/{gameId}/seat/user/{userId}")
    @SendTo("/topic/game/{gameId}/seat")
    public SeatTakenMessage grabGameSeat(@DestinationVariable String gameId, @DestinationVariable String userId) {
        SeatTakenMessage seatMessage = GameInstanceCache.getSeat(gameId, userId);

        int numPlayers = GameInstanceCache.getNumPlayers(gameId);

        //if we're ready, tell players.
        if (numPlayers == GameInstance.MAX_PLAYERS) {
            GameInstanceCache.setGameInstanceState(gameId, GameState.PLAY);
            getGameState(gameId);
        }

        return seatMessage;
    }

    //todo: auth/z

    /**
     * Send a target GameInstance's game state.
     *
     * @param gameId target gameId
     * @return the game Instance's game state.
     */
    //@MessageMapping("/game/{gameId}/state")
    //@SendTo("/topic/game/{gameId}/state")
    public void getGameState(@DestinationVariable String gameId) {
        GameState gameState = GameInstanceCache.getGameState(gameId);
        System.out.println("Fire");
        String destination = String.format("/topic/game/%s/state", gameId);
        simpTemplate.convertAndSend(destination, gameState);
    }

    //TODO: need auth/z

    /**
     * A player attempts to make a move for their game.
     *
     * @param move The desired move.
     * @return game moves. or attempted moves, which should be ignored or never
     * sent in the first place.
     */
    @MessageMapping("/game/{gameId}/move")
    @SendTo("/topic/game/{gameId}/move")
    public GameMoveResult makeMove(@DestinationVariable String gameId, GameMove move) {
        return GameInstanceCache.makeMove(gameId, move);
    }

}
