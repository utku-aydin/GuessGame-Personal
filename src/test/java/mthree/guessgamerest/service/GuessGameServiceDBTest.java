/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.guessgamerest.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mthree.guessgamerest.data.GameDao;
import mthree.guessgamerest.data.RoundDao;
import mthree.guessgamerest.models.Game;
import mthree.guessgamerest.models.Round;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author utkua
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GuessGameServiceDBTest {
    
    @Autowired
    private GameDao gameDao;
    
    @Autowired
    private RoundDao roundDao;
    
    @Autowired
    private GuessGameService service;
    
    @Before
    public void setUp() {
        roundDao.deleteAll();
        gameDao.deleteAll();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of beginGame method, of class GuessGameServiceDB.
     */
    @Test
    public void testBeginGame() {
        Game game1 = service.beginGame();
        Game game2 = service.beginGame();
        service.beginGame();
        
        String answer = String.valueOf(game1.getAnswer());
        assertEquals(4, answer.length());
        
        long count;
        for (int i = 0; i < 4; i++) {
            char current = answer.charAt(i);
            count = answer.chars().filter(ch -> ch == current).count();
            if (count != 1) {
                fail("Each digit of an answer must be unique.");
            }
        }
        
        Game game3 = new Game(1234, true);
        game3.setId(100);
        
        // Need to make guess prior to assertions so returned answer is not 0
        makeGuessForGame(game2, game2.getAnswer());
        // Must also finish game since a correct guess will set finished to true.
        game2.finishGame();
        List<Game> games = service.getAllGames();
        
        assertEquals(3, games.size());
        assertTrue(games.contains(game2));
        assertFalse(games.contains(game3));
    }

    /**
     * Test of getRoundsForGame method, of class GuessGameServiceDB.
     */
    @Test
    public void testGetRoundsForGame() {
        Game game1 = service.beginGame();
        Round round1 = makeGuessForGame(game1, 1234);
        // Finish game stored on first round - the retreived round in the retrieved list will have a finished game
        // (as the game is completed).
        round1.getGame().finishGame();
        // Complete game on second round so the game of the round is not set to null on return.
        Round round2 = makeGuessForGame(game1, game1.getAnswer());
        Round round3 = new Round(1234, "result");
        
        List<Round> roundList = service.getRoundsForGame(game1.getId());
        assertEquals(2, roundList.size());
        assertEquals(round1.getGame(), roundList.get(0).getGame());
        assertTrue(roundList.contains(round2));
        assertFalse(roundList.contains(round3));
    }

    /**
     * Test of makeGuess and getGameById methods, of class GuessGameServiceDB.
     */
    @Test
    public void testMakeGuess() {
        Game game1 = new Game(1234, false);
        
        game1 = service.addGame(game1);
        Map<String, Integer> guess = new HashMap();
        guess.put("guess", 1235);
        guess.put("gameId", game1.getId());
        
        Round resultRound1 = service.makeGuess(guess);
        assertEquals("e:3:p:0", resultRound1.getResult());
        
        guess.put("guess", 4321);
        Round resultRound3 = service.makeGuess(guess);
        assertEquals("e:0:p:4", resultRound3.getResult());
        
        guess.put("guess", 1234);
        Round resultRound2 = service.makeGuess(guess);
        assertEquals("e:4:p:0", resultRound2.getResult());
    }

    /**
     * Test of getGameById method, of class GuessGameServiceDB.
     */
    @Test
    public void testGetGameById() {
        Game game1 = service.beginGame();
        Game game2 = service.beginGame();
        
        makeGuessForGame(game1, game1.getAnswer());
        game1.finishGame();
        
        // If the game is finished, the answer should be returned.
        assertEquals(game1, service.getGameById(game1.getId()));
        // If not, answer should be set to 0
        assertEquals(0, service.getGameById(game2.getId()).getAnswer());
    }

    /**
     * Test of getAllGames method, of class GuessGameServiceDB.
     */
    @Test
    public void testGetAllGames() {
        // Implicitly tested in testBeginGame
    }
    
    private Round makeGuessForGame(Game game, int guess) {
        //game = service.addGame(game);
        Map<String, Integer> guessMap = new HashMap();
        guessMap.put("guess", guess);
        guessMap.put("gameId", game.getId());
        
        return service.makeGuess(guessMap);
    }
    
}
