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

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;

/**
 * This class contains all the logic for Pac-Man game.
 * 
 * @author fredy
 */
public class PacManGame {
    private static final int SCORE = 10;
    private Player pacMan;
    private List<Player> ghosts = new ArrayList<Player>();
    private MovementEngine movementEngine;
    private int nDots;
    private Game game;
    private int totalScore;
    private NodeType[][] maze;
    
    public PacManGame(Game game, String mapFileName) {
        this.game = game;

        LoadMaze(mapFileName);
        pacMan = new Player(Player.PlayerType.PACMAN);
        pacMan.setCurrentColumn(0);
        pacMan.setCurrentRow(maze.length - 1);
        pacMan.setPosition(Player.Position.RIGHT);
        pacMan.setNumDotsEaten(0);

        movementEngine = new MovementEngine(pacMan, maze);

        start();
    }
    
    public void LoadMaze(String fileName) {
    	try {
    		Scanner sc = new Scanner(new File(fileName));
    		int size_x = sc.nextInt();
    		int size_y = sc.nextInt();
    		maze = new NodeType[size_x][size_y];
    		int num_x = sc.nextInt();
    		int num_y = sc.nextInt();
    		MazeBuilder.setNumBlock(num_x,num_y);
    		for(int i = 0; i < size_x; ++i)
    		{
    			for(int j = 0; j < size_y; ++j)
    			{
    				if(!sc.hasNextInt()) {  }
    				int temp = sc.nextInt();
    				if(temp == 1)
    				{
    					maze[i][j] = NodeType.DOT;
    				}
    				else if(temp == 2)
    				{
    					maze[i][j] = NodeType.WALL;
    				}
    				else if(temp == 3)
    				{
    					maze[i][j] = NodeType.PACMAN;
    				}
    				else if(temp == 4)
    				{
    					maze[i][j] = NodeType.GHOST;
    				}
    				else if(temp == 5)
    				{
    					maze[i][j] = NodeType.BLANK;
    				}
    				else if(temp == 6)
    				{
    					maze[i][j] = NodeType.POWER_UP;
    				}
    			}
    		}
    	} catch (Exception ex) {
            // Do nothing.
        }
    }
    
    public void start() {
    	int ghostnum = 0;
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == NodeType.DOT) {
                    nDots++;
                } else if (maze[i][j] == NodeType.GHOST) {
                    Player ghost = new Player(Player.PlayerType.GHOST);
                    ghost.setCurrentRow(i);
                    ghost.setCurrentColumn(j);
                    ghost.setPosition(Player.Position.RIGHT);
                    int maxnum = 3;
                    if((ghostnum%maxnum)==0) ghost.setGhostType(Player.GhostType.BLINKY);
                    if((ghostnum%maxnum)==1) ghost.setGhostType(Player.GhostType.PINKY);
                    if((ghostnum%maxnum)==2) ghost.setGhostType(Player.GhostType.CLYDE);
                    ghosts.add(ghost);
                    ++ghostnum;
                }
            }
        }
    }

    public void movePacMan() {
        move(pacMan);
        totalScore = pacMan.getNumDotsEaten() * SCORE;
        if (pacMan.getNumDotsEaten() == nDots) {
            game.win();
        } else {
            if (pacMan.isDead()) {
                game.gameOver();
            }
        }
    }

    public void moveGhost() {
        List<Thread> threadList = new ArrayList<>();
    	for (Player ghost : ghosts) {
    		move(ghost);
    		Thread thread = new Thread( () -> {
        	ghost.setPosition(GhostAI.getGhostNextPosition(pacMan, ghost, maze));
            });
    		threadList.add(thread);
            threadList.get(threadList.size()-1).run();
        }
    	for(Thread thread : threadList)
    	{
    		try { thread.join(); }
    		catch (Exception ex) {
                // Do nothing.
            }
    	}
    }

    private void move(Player player) {
        if (player.getPosition() == Player.Position.LEFT) {
            movementEngine.left(player);
        } else if (player.getPosition() == Player.Position.RIGHT) {
            movementEngine.right(player);
        } else if (player.getPosition() == Player.Position.UP) {
            movementEngine.up(player);
        } else if (player.getPosition() == Player.Position.DOWN) {
            movementEngine.down(player);
        }
    }

    public Player getPacMan() {
        return pacMan;
    }

    public NodeType[][] getMaze() {
        return maze;
    }

    public List<Player> getGhosts() {
        return ghosts;
    }

    public int getTotalScore() {
        return totalScore;
    }
}
