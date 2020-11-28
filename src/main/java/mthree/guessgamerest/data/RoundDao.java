/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.guessgamerest.data;

import java.util.List;
import mthree.guessgamerest.models.Round;

/**
 *
 * @author utkua
 */
public interface RoundDao {
    
    public List<Round> getRoundsForGame(int gameId);
    
    public Round addRound(Round round);
    public Round getRoundById(int id);
    public void deleteAll();
    
}
