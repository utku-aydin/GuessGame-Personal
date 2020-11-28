/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.guessgamerest.data;

import java.util.List;
import mthree.guessgamerest.models.Game;

/**
 *
 * @author utkua
 */
public interface GameDao {
    int num = 2;
    public List<Game> getAllGames();
    
    public Game addGame(Game game);
    public boolean updateGame(Game game);
    public Game getGameById(int id);
    public void deleteAll();
    
}
