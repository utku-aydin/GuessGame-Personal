/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.guessgamerest.service;

import java.util.List;
import java.util.Map;
import mthree.guessgamerest.models.Game;
import mthree.guessgamerest.models.Round;

/**
 *
 * @author utkua
 */
public interface GuessGameService {
    
    public Game beginGame();
    public Game getGameById(int id);
    
    public List<Game> getAllGames();
    
    public List<Round> getRoundsForGame(int gameId);
    
    public Round makeGuess(Map<String, Integer> guessData);
    
    public Game addGame(Game game);
    
}
