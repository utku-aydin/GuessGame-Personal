/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.guessgamerest.controllers;

import java.util.List;
import java.util.Map;
import mthree.guessgamerest.models.Game;
import mthree.guessgamerest.models.Round;
import mthree.guessgamerest.service.GuessGameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author utkua
 */
@RestController
// /api is a preference in this case
@CrossOrigin(origins="*")
@RequestMapping("/api")
public class GuessGameController {
    
    private final GuessGameService service;

    public GuessGameController(GuessGameService service) {
        this.service = service;
    }
    
    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public int beginGame() {
        return service.beginGame().getId();
    }
    
    @PostMapping("/guess")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Round> makeGuess(@RequestBody Map<String, Integer> guessData) {
        Round result = service.makeGuess(guessData);
        
        result.setGame(null);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/game/{id}")
    public ResponseEntity<Game> findById(@PathVariable int id) {
        Game result = service.getGameById(id);
        if (result == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/game")
    public ResponseEntity<List<Game>> getAllGames() {
        List<Game> games = service.getAllGames();
        if (games.isEmpty()) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        
        return ResponseEntity.ok(games);
    }
    
    @GetMapping("/rounds/{gameId}")
    public ResponseEntity<List<Round>> getRoundsForGame(@PathVariable int gameId) {
        List<Round> rounds = service.getRoundsForGame(gameId);
        if (rounds == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        
        return ResponseEntity.ok(rounds);
    }
    
}
