/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.guessgamerest.models;

/**
 * Object representing a game of Bulls and Cows.
 * 
 * @author utkua
 */
public class Game {
    
    private int id;
    private int answer;
    private boolean finished;
    
    public Game(int answer, boolean finished) {
        this.answer = answer;
        this.finished = finished;
    }
    
    public void finishGame() {
        finished = true;
    }
    
    public int getId() {
        return id;
    }

    public int getAnswer() {
        return answer;
    }

    public boolean isFinished() {
        return finished;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setAnswer(int answer) {
        this.answer = answer;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + this.id;
        hash = 11 * hash + this.answer;
        hash = 11 * hash + (this.finished ? 1 : 0);
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
        final Game other = (Game) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.answer != other.answer) {
            return false;
        }
        if (this.finished != other.finished) {
            return false;
        }
        return true;
    }
    
}
