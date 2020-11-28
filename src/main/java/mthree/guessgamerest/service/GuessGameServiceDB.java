/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.guessgamerest.service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import mthree.guessgamerest.data.GameDao;
import mthree.guessgamerest.data.RoundDao;
import mthree.guessgamerest.models.Game;
import mthree.guessgamerest.models.Round;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 *
 * @author utkua
 */
@Repository
@Profile("database")
public class GuessGameServiceDB implements GuessGameService {
    
    private final GameDao gameDao;
    private final RoundDao roundDao;

    public GuessGameServiceDB(GameDao gameDao, RoundDao roundDao) {
        this.gameDao = gameDao;
        this.roundDao = roundDao;
    }

    @Override
    public Game beginGame() {
        Random r = new Random();
        
        int digit1 = r.nextInt(9) + 1;
        int digit2 = r.nextInt(9) + 1;
        while (digit2 == digit1) {            
            digit2 = r.nextInt(9) + 1;
        }
        
        int digit3 = r.nextInt(9) + 1;
        while (digit3 == digit2 || digit3 == digit1) {            
            digit3 = r.nextInt(9) + 1;
        }
        
        int digit4 = r.nextInt(9) + 1;
        while (digit4 == digit3 || digit4 == digit2 || digit4 == digit1) {            
            digit4 = r.nextInt(9) + 1;
        }
        
        String answerString = digit1 + "" + digit2 + "" + digit3 + "" + digit4;
        int answer = Integer.parseInt(answerString);
        
        Game madeGame = new Game(answer, false);
        
        return gameDao.addGame(madeGame);
    }

    @Override
    public Game getGameById(int id) {
        Game game = gameDao.getGameById(id);
        if (!game.isFinished()) {
            game.setAnswer(0);
        }
        
        return game;
    }

    @Override
    public List<Game> getAllGames() {
        List<Game> gameList = gameDao.getAllGames();
        gameList.stream().filter((game) -> (!game.isFinished())).forEachOrdered((game) -> {
            game.setAnswer(0);
        });
        
        return gameList;
    }
    
    @Override
    public List<Round> getRoundsForGame(int gameId) {
        List<Round> roundsForGame = roundDao.getRoundsForGame(gameId);
        if (gameDao.getGameById(gameId) == null) {
            return null;
        }
        
        for (Round round : roundsForGame) {
            if (!round.getGame().isFinished()) {
                round.setGame(null);
            }
        }
        
        return roundsForGame;
    }

    @Override
    public Round makeGuess(Map<String, Integer> guessData) {
        int guess = guessData.get("guess");
        String guessString = String.valueOf(guess);
        int length = guessString.length();
        
        int gameId = guessData.get("gameId");
        Game game = gameDao.getGameById(gameId);
        int answer = game.getAnswer();
        
        String result;
        
        if (game.isFinished()) {
            return makeRound("Finished");
        } else if (length != 4) {
            return makeRound("BadLength");
        } else if (isNotUnique(guessString)) {
            return makeRound("NotUnique");
        } else {
            result = getResult(guess, answer);
        }
        
        if (guess == answer) {
            game.finishGame();
            gameDao.updateGame(game);
        }
        
        Round round = new Round(guess, result);
        round.setGame(game);
        round.setTime(LocalDateTime.now().withNano(0));

        roundDao.addRound(round);
        
        return round;
    }
    
    @Override
    public Game addGame(Game game) {
        return gameDao.addGame(game);
    }
    
    private String getResult(int guess, int answer) {
        String guessString = Integer.toString(guess);
        String answerString = Integer.toString(answer);
        
        int exactMatches = 0;
        int partialMatches = 0;
        
        List<String> matchedChars = new LinkedList<>();
        List<String> partialChars = new LinkedList<>();
        
        for (int i = 0; i < guessString.length(); i++) {
            String guessChar = String.valueOf(guessString.charAt(i));
            if (guessChar.equals(String.valueOf(answerString.charAt(i)))) {
                exactMatches++;
                if (!matchedChars.contains(guessChar)) {
                    matchedChars.add(guessChar);
                }
                
                if (partialChars.contains(guessChar)) {
                    partialMatches--;
                }
                
            } else if (answerString.contains(String.valueOf(guessChar)) && !matchedChars.contains(guessChar)) {
                partialMatches++;
                partialChars.add(guessChar);
            }
        }
        
        String result = "e:" + exactMatches + ":p:" + partialMatches;
        return result;
    }
    
    private boolean isNotUnique(String is) {
        int guessLength = 4;
        for (int i = 0; i < guessLength - 1; i++) {
            for (int j = i + 1; j < guessLength; j++) {
                if (is.charAt(i) == is.charAt(j)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private Round makeRound(String result) {
        Round round = new Round(0, result);
        round.setTime(LocalDateTime.now().withNano(0));
        return round;
    }
}
