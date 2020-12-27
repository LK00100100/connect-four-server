package com.lk00100100.connectfourserver.controller;

import com.lk00100100.connectfourserver.data.GameInstanceCache;
import com.lk00100100.connectfourserver.data.GameMoveResult;
import com.lk00100100.connectfourserver.data.GameMove;
import com.lk00100100.connectfourserver.data.SeatTakenMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Websocket Game Controller.
 * Connect to this and get actual game data moved to/from the server.
 */
@Controller
public class WebSocketGameController {

    //TODO: handle game disconnects. send victory

    //TODO: auth/z
    /**
     * Attempt to grab a game seat (player number) from a game.
     * @param gameId target game id
     * @return a positive player number. -1 if bad.
     */
    @MessageMapping("/game/{gameId}/seat/user/{userId}")
    @SendTo("/topic/game/{gameId}/seat")
    public SeatTakenMessage grabGameSeat(@DestinationVariable String gameId, @DestinationVariable String userId){
        return GameInstanceCache.getSeat(gameId, userId);
    }

    //todo: auth/z
    /***
     * @param move a player attempted move.
     * @return game move result.
     */
    @MessageMapping("/game/{gameId}/status")
    @SendTo("/topic/game/{gameId}/status")
    public GameMoveResult attemptGameMove(GameMove move)  {
        GameMove gameMove = new GameMove();

        GameMoveResult res = new GameMoveResult();
        return res;

    }

    //TODO: need auth/z
    /**
     * A player attempts to make a move for their game.
     * @param move The desired move.
     * @return game moves. or attempted moves, which should be ignored or never
     * sent in the first place.
     */
    @MessageMapping("/game/{gameId}")
    @SendTo("/topic/game/{gameId}/move")
    public GameMoveResult attemptGameMove(@DestinationVariable String gameId, GameMove move)  {
        GameMove gameMove = new GameMove();

        GameMoveResult res = new GameMoveResult();
        return res;

    }

}
