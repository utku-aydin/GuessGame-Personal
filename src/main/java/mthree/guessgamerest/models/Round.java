/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.guessgamerest.models;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author utkua
 */
public class Round {
    
    private int id;
    private LocalDateTime time;
    private final int guess;
    private final String result;
    private Game game;
    
    public Round(int guess, String result) {
        this.time = LocalDateTime.now().withNano(0);
        
        this.guess = guess;
        this.result = result;
    }
    
    public int getId() {
        return id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public int getGuess() {
        return guess;
    }

    public String getResult() {
        return result;
    }
    
    public Game getGame() {
        return game;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setGame(Game game) {
        this.game = game;
    }
    
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.id;
        hash = 89 * hash + Objects.hashCode(this.time);
        hash = 89 * hash + this.guess;
        hash = 89 * hash + Objects.hashCode(this.result);
        hash = 89 * hash + Objects.hashCode(this.game);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Round other = (Round) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.guess != other.guess) {
            return false;
        }
        if (!Objects.equals(this.result, other.result)) {
            return false;
        }
        if (!Objects.equals(this.time, other.time)) {
            return false;
        }
        if (!Objects.equals(this.game, other.game)) {
            return false;
        }
        return true;
    }
    
}
