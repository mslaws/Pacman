// The MIT License (MIT)
// 
// Copyright (c) 2014 Fredy Wijaya
// 
// Permission is hereby granted, free of charge, to any person obtaining a copy of
// this software and associated documentation files (the "Software"), to deal in
// the Software without restriction, including without limitation the rights to
// use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
// the Software, and to permit persons to whom the Software is furnished to do so,
// subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
// FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
// COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
// IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
// CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package org.projektpwr.pacman;

/**
 * @author fredy
 */
public class Player {
    public enum Position {
        UP, DOWN, LEFT, RIGHT
    }
    
    public static Position Reverse(Position pos) {
    	if(pos == Position.UP) return Position.DOWN;
    	else if(pos == Position.DOWN) return Position.UP;
    	else if(pos == Position.LEFT) return Position.RIGHT;
    	else return Position.LEFT;
    }

    public enum PlayerType {
        PACMAN, GHOST
    }
    
    public enum GhostType {
    	BLINKY, PINKY, CLYDE
    }

    public enum PlayerStatus {
        NORMAL, POWERED_UP, SCARED
    }
    
    private PlayerType playerType;
    private GhostType ghostType;
    private PlayerStatus playerStatus;
    private int currentRow;
    private int currentColumn;
    private Position position;
    private int numDotsEaten;
    private boolean dead;

    public Player(PlayerType playerType) {
        this.playerType = playerType;
    }

    public int getNumDotsEaten() {
        return numDotsEaten;
    }

    public void setNumDotsEaten(int numDotsEaten) {
        this.numDotsEaten = numDotsEaten;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }
    
    public GhostType getGhostType() {
        return ghostType;
    }

    public void setGhostType(GhostType ghostType) {
        this.ghostType = ghostType;
    }

    public int getCurrentColumn() {
        return currentColumn;
    }

    public void setCurrentColumn(int currentColumn) {
        this.currentColumn = currentColumn;
    }

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
