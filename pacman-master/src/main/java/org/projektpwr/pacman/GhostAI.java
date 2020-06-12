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

/**
 * An AI for the Ghosts based on A* path finding.
 * 
 * @author fredy
 */
public class GhostAI {
    private GhostAI() {
        // Prevent instantiation.
    }

    public static Player.Position getGhostNextPosition(Player pacMan, Player ghost,
        NodeType[][] maze) {
        Node startNode = new Node();
        Node targetNode = new Node();
        startNode.setColumn(ghost.getCurrentColumn());
        startNode.setRow(ghost.getCurrentRow());

        targetNode.setColumn(pacMan.getCurrentColumn());
        targetNode.setRow(pacMan.getCurrentRow());

        Player.Position position = Player.Position.UP;
        
        if(ghost.getGhostType() == Player.GhostType.PINKY)
        {
        	position = Pinky(startNode,targetNode,pacMan,maze);
        }
        else if(ghost.getGhostType() == Player.GhostType.CLYDE)
        {
        	position = Clyde(maze,ghost);
        }
        else
        {
        	position = Blinky(startNode,targetNode,maze);
        }
        
        return position;
    }

    private static Player.Position Blinky(Node startNode, Node targetNode,
        NodeType[][] maze) {
        List<Node> openNodeList = new ArrayList<Node>();
        List<Node> closeNodeList = new ArrayList<Node>();
        openNodeList.add(startNode);
        while (openNodeList.size() > 0 && !closeNodeList.contains(targetNode)) {
            Node currentNode = getLowestDistanceNode(openNodeList);
            closeNodeList.add(currentNode);
            openNodeList.remove(currentNode);

            List<Node> adjacentNodes = getAdjacentNodes(currentNode.getColumn(),
                currentNode.getRow(), maze);
            for (Node adjacentNode : adjacentNodes) {
                if (!closeNodeList.contains(adjacentNode)) {
                    adjacentNode.setParentColumn(currentNode.getColumn());
                    adjacentNode.setParentRow(currentNode.getRow());
                    if (!openNodeList.contains(adjacentNode)) // Open list
                    {
                        adjacentNode.setDistance(calculateDistance(
                            adjacentNode.getColumn(), adjacentNode.getRow(),
                            targetNode.getColumn(), targetNode.getRow()));
                        openNodeList.add(adjacentNode);
                    }
                }
            }
        }
        return getNextPosition(startNode,
            getNextMove(startNode, targetNode, closeNodeList));
    }
    
    private static Player.Position Pinky(Node startNode,Node targetNode, Player pacMan,
            NodeType[][] maze) {
            Node TargetNode = targetNode;
            Player.Position position = pacMan.getPosition();
            int row = pacMan.getCurrentRow();
        	int column = pacMan.getCurrentColumn();
            for( List<Player.Position> possibleMoves = getPossibleMoves(maze, row,column); 
            		possibleMoves.size() == 2;
            		possibleMoves = getPossibleMoves(maze, row,column) )
            {
    			possibleMoves.remove(Player.Reverse(position));
    			position = possibleMoves.get(0);
    			if(position == Player.Position.UP)
    			{
    				row--;
    			}
    			else if(position == Player.Position.DOWN)
    			{
    				row++;
    			}
    			else if(position == Player.Position.LEFT)
    			{
    				column--;
    			}
    			else if(position == Player.Position.RIGHT)
    			{
    				column++;
    			}
    			TargetNode.setRow(row);
    			TargetNode.setColumn(column);
            }
            return Blinky(startNode,TargetNode, maze);
        }
    
    private static Player.Position Clyde(NodeType[][] maze, Player ghost) {
    		Player.Position position = ghost.getPosition();
    		List<Player.Position> possibleMoves = getPossibleMoves(maze,ghost.getCurrentRow(),ghost.getCurrentColumn());
    		if(possibleMoves.size()>1)
			{
				possibleMoves.remove(Player.Reverse(position));
			}
    		int index = (int) (Math.random() * 7.1 *  (possibleMoves.size()) % possibleMoves.size());
    		position = possibleMoves.get(index);
    		return position;
        }
    
    private static List<Player.Position> getPossibleMoves(NodeType[][] maze, int row, int column) {
    		List<Player.Position> possibleMoves = new ArrayList<Player.Position>();
			if(row-1 >= 0)
			{
				if((maze[row-1][column] == NodeType.BLANK) || (maze[row-1][column] == NodeType.DOT) || (maze[row-1][column] == NodeType.PACMAN) )
				{
					possibleMoves.add(Player.Position.UP);
				}
			}
			if(row + 1 < maze.length)
			{
				if((maze[row+1][column] == NodeType.BLANK) || (maze[row+1][column] == NodeType.DOT) || (maze[row+1][column] == NodeType.PACMAN) )
				{
					possibleMoves.add(Player.Position.DOWN);
				}
			}
			if(column-1 >= 0)
			{
				if((maze[row][column-1] == NodeType.BLANK) || (maze[row][column-1] == NodeType.DOT) || (maze[row][column-1] == NodeType.PACMAN) )
				{
					possibleMoves.add(Player.Position.LEFT);
				}
			}
			if(column+1 < maze[row].length)
			{
				if((maze[row][column+1] == NodeType.BLANK) || (maze[row][column+1] == NodeType.DOT) || (maze[row][column+1] == NodeType.PACMAN) )
				{
					possibleMoves.add(Player.Position.RIGHT);
				}
			}
			return possibleMoves;
		}
    
    private static Player.Position getNextPosition(Node startNode, Node nextNode) {
        Player.Position position = null;
        if (nextNode != null) {
            if (startNode.getColumn() > nextNode.getColumn()) {
                position = Player.Position.LEFT;
            } else if (startNode.getColumn() < nextNode.getColumn()) {
                position = Player.Position.RIGHT;
            } else if (startNode.getRow() > nextNode.getRow()) {
                position = Player.Position.UP;
            } else if (startNode.getRow() < nextNode.getRow()) {
                position = Player.Position.DOWN;
            }
        }
        return position;
    }

    private static Node getNextMove(Node startNode, Node targetNode,
        List<Node> closeNodeList) {
        int column = targetNode.getColumn();
        int row = targetNode.getRow();
        Node node = null;
        while ((node = findNode(column, row, closeNodeList)) != null) {
            if (node.getParentColumn() == startNode.getColumn()
                && node.getParentRow() == startNode.getRow()) {
                break;
            } else {
                column = node.getParentColumn();
                row = node.getParentRow();
            }
        }
        return node;
    }

    private static Node findNode(int column, int row, List<Node> closeNodeList) {
        for (Node node : closeNodeList) {
            if (node.getColumn() == column && node.getRow() == row) {
                return node;
            }
        }
        return null;
    }

    private static List<Node> getAdjacentNodes(int column, int row, NodeType[][] maze) {
        List<Node> nodeList = new ArrayList<Node>();
        if (!isBlockedTerrain(column + 1, row, maze)) {
            Node node = new Node();
            node.setColumn(column + 1);
            node.setRow(row);
            nodeList.add(node);
        }

        if (!isBlockedTerrain(column - 1, row, maze)) {
            Node node = new Node();
            node.setColumn(column - 1);
            node.setRow(row);
            nodeList.add(node);
        }

        if (!isBlockedTerrain(column, row + 1, maze)) {
            Node node = new Node();
            node.setColumn(column);
            node.setRow(row + 1);
            nodeList.add(node);
        }

        if (!isBlockedTerrain(column, row - 1, maze)) {
            Node node = new Node();
            node.setColumn(column);
            node.setRow(row - 1);
            nodeList.add(node);
        }
        return nodeList;
    }

    private static boolean isBlockedTerrain(int column, int row, NodeType[][] maze) {
        boolean blocked = false;
        try {
            if (maze[row][column] == NodeType.WALL) {
                blocked = true;
            }
            /*if (maze[row][column] == NodeType.GHOST) {
                blocked = true;
            }*/
        } catch (Exception ex) {
            // Do nothing.
        }
        return blocked;
    }

    private static int calculateDistance(int srcColumn, int srcRow, int destColumn,
        int destRow) {
        return (Math.abs(srcColumn - destColumn) * 10)
            + (Math.abs(srcRow - destRow) * 10);
    }

    private static Node getLowestDistanceNode(List<Node> openNodeList) {
        Node lowestDistanceNode = null;
        if (openNodeList.size() > 0) {
            lowestDistanceNode = openNodeList.get(0);
            for (int i = 1; i < openNodeList.size(); i++) {
                Node n = openNodeList.get(i);
                if (n.getDistance() < lowestDistanceNode.getDistance()) {
                    lowestDistanceNode = n;
                }
            }
        }
        return lowestDistanceNode;
    }
}
