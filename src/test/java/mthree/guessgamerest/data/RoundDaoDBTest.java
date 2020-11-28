/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.guessgamerest.data;

import java.time.LocalDateTime;
import java.util.List;
import mthree.guessgamerest.models.Game;
import mthree.guessgamerest.models.Round;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
public class RoundDaoDBTest {
    
    @Autowired
    private GameDao gameDao;
    
    @Autowired
    private RoundDao roundDao;
    
    public RoundDaoDBTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        roundDao.deleteAll();
        gameDao.deleteAll();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getRoundsForGame method, of class RoundDaoDB.
     */
    @Test
    public void testGetRoundsForGame() {
        Game game1 = createAddGame();
        Game game2 = createAddGame();
        
        Round round1 = new Round(1234, "e:0:p:0");
        round1.setGame(game1);
        Round round2 = new Round(1232, "e:0:p:0");
        round2.setGame(game1);
        Round round3 = new Round(1231, "e:0:p:0");
        round3.setGame(game2);
        
        roundDao.addRound(round1);
        roundDao.addRound(round2);
        roundDao.addRound(round3);
        
        List<Round> roundsForGame1 = roundDao.getRoundsForGame(game1.getId());
        assertEquals(2, roundsForGame1.size());
        assertFalse(roundsForGame1.contains(round3));
    }

    /**
     * Test of addRound and getRoundById methods, of class RoundDaoDB.
     */
    @Test
    public void testAddGetRoundById() {
        Round round = new Round(1234, "e:0:p:0");
        Game game = createAddGame();
        round.setGame(game);
        
        round = roundDao.addRound(round);
        // Time must be set to the same value for both Round objects as the creation times will be different regardless
        //round.setTime(LocalDateTime.MAX);
        
        Round fromDao = roundDao.getRoundById(round.getId());
        //fromDao.setTime(LocalDateTime.MAX);
        
        //assertEquals(round, fromDao);
        assertEquals(round.getId(), fromDao.getId());
        assertEquals(round.getGuess(), fromDao.getGuess());
        assertEquals(round.getResult(), fromDao.getResult());
        assertEquals(round.getTime(), fromDao.getTime());
        assertEquals(round.getGame(), fromDao.getGame());
    }

    /**
     * Test of deleteAll method, of class RoundDaoDB.
     */
    @Test
    public void testDeleteAll() {
        Round round = new Round(1234, "e:0:p:0");
        Game game = createAddGame();
        round.setGame(game);
        
        roundDao.addRound(round);
        assertEquals(1, roundDao.getRoundsForGame(game.getId()).size());
        
        roundDao.deleteAll();
        assertEquals(0, roundDao.getRoundsForGame(game.getId()).size());
    }
    
    private Game createAddGame() {
        Game game = new Game(1234, true);
        game = gameDao.addGame(game);
        
        return game;
    }
    
}
