package com.lk00100100.connectfourserver.controller;

import com.lk00100100.connectfourserver.GameInstance;
import com.lk00100100.connectfourserver.GameNotFoundException;
import com.lk00100100.connectfourserver.data.GameInstanceCache;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST api for downloading game meta data.
 */
@RestController
@CrossOrigin(origins = {"http://localhost:8081"})
@RequestMapping("/game")  //all requests for here will be sent here
public class GameController {

    //todo: passwords for rooms

    public GameController() {
    }

    @GetMapping
    @RequestMapping("/hello")
    public String getHello() {
        return "hello there";
    }

    @GetMapping
    @RequestMapping("/list")
    public List<String> getGameInstanceBasicList() {
        //todo: list how full the games are.
        return GameInstanceCache.getGameInstanceIds();
    }

    @GetMapping
    @RequestMapping("/{gameId}")
    public GameInstance getGameInstance(@PathVariable String gameId) {
        //todo: need authorization

        if (!GameInstanceCache.gameInstanceExists(gameId))
            throw new GameNotFoundException("Game not found");

        return GameInstanceCache.getGameInstance(gameId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createNewGame() {
        return GameInstanceCache.createNewGameInstance();
    }

    @PostMapping
    @RequestMapping("/{gameId}/player/{playerNum}/colNum/{colNum}")
    public boolean processMove(short playerNum, short colNum) {
        //todo: needs authorization
        //todo: maybe return an object with more details.
        //TODO: do this

        return false;
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Game not found")
    @ExceptionHandler(GameNotFoundException.class)
    public GameNotFoundException gameWasNotFound(GameNotFoundException ex) {
        //todo: check the message

        return ex;
    }

}
