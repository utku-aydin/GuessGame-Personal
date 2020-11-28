/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.guessgamerest.data;

import java.util.List;
import mthree.guessgamerest.models.Game;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
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
public class GameDaoDBTest {
    
    @Autowired
    private GameDao gameDao;
    
    @Autowired
    private RoundDao roundDao;
    
    public GameDaoDBTest() {
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
     * Test of getAllGames method, of class GameDaoDB.
     */
    @Test
    public void testGetAllGames() {
        
        assertEquals(0, gameDao.getAllGames().size());
        
        Game game1 = new Game(1234, true);
        Game game2 = new Game(1334, false);
        Game game3 = new Game(1534, false);
        
        gameDao.addGame(game1);
        gameDao.addGame(game2);
        
        List<Game> gameList = gameDao.getAllGames();
        
        assertEquals(2, gameList.size());
        assertTrue(gameList.contains(game2));
        assertFalse(gameList.contains(game3));
    }

    /**
     * Test of addGame and getGameById methods, of class GameDaoDB.
     */
    @Test
    public void testAddGetGame() {
        Game game = new Game(1234, true);
        
        game = gameDao.addGame(game);
        Game fromDao = gameDao.getGameById(game.getId());
        
        assertEquals(game, fromDao);
    }

    /**
     * Test of updateGame method, of class GameDaoDB.
     */
    @Test
    public void testUpdateGame() {
        Game game = new Game(4321, false);
        
        game = gameDao.addGame(game);
        
        game.finishGame();
        gameDao.updateGame(game);

        Game fromDao = gameDao.getGameById(game.getId());
        assertTrue(fromDao.isFinished());
    }
    
    /**
     * Test of deleteAll method, of class GameDaoDB.
     */
    @Test
    public void testDeleteAll() {
        Game game = new Game(1234, true);
        
        gameDao.addGame(game);
        assertEquals(1, gameDao.getAllGames().size());
        
        gameDao.deleteAll();
        assertEquals(0, gameDao.getAllGames().size());
    }
    
}
