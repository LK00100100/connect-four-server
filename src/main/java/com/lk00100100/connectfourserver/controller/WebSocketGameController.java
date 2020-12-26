package com.lk00100100.connectfourserver.controller;

import com.lk00100100.connectfourserver.data.GameInstanceCache;
import com.lk00100100.connectfourserver.data.GameMoveResult;
import com.lk00100100.connectfourserver.data.GameMove;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

/**
 * Websocket Game Controller.
 * Connect to this and get actual game data moved to/from the server.
 */
@Controller
public class WebSocketGameController {

    //TODO: handle game disconnects. send victory

    //TODO: authentication, authorization
    /**
     * Attempt to grab a game seat (player number) from a game.
     * @param gameId target game id
     * @return a positive player number. -1 if bad.
     */
    @MessageMapping("/game/{gameId}/seat/user/{userId}")
    @SendTo("/topic/game/{gameId}/seat/user/{userId}")
    public int grabGameSeat(@DestinationVariable String gameId, @DestinationVariable String userId){
        return GameInstanceCache.getSeat(gameId, userId);
    }

    /**
     * A player attempts to make a move for their game.
     * @param move The desired move.
     * @return true
     */
    @MessageMapping("/game")
    @SendTo("/topic/game/{gameId}")
    public GameMoveResult attemptGameMove(GameMove move)  {
        GameMove gameMove = new GameMove();

        GameMoveResult res = new GameMoveResult();
        return res;

    }

}
