package util;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.Maze;

public class Cell {

	private int x, y, distance, id;


	private boolean path = false;
	private boolean NoWay = false;
	private boolean visited = false;

	
	private boolean[] walls = {true, true, true, true};

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
		this.distance = -1;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public boolean isVisited() {
		return visited;
	}
	
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	public void setNoWay(boolean noWay) {
		this.NoWay = noWay;
	}

	public void setPath(boolean path) {
		this.path = path;
	}
	
	public void draw(Graphics g) {
		int x2 = x * Maze.W;
	    int y2 = y * Maze.W;

	    // TODO: Change the 3 colors below
	    if (visited) {
	    	g.setColor(Color.LIGHT_GRAY);
	    	g.fillRect(x2, y2, Maze.W, Maze.W);
	    }
	   
	    if (path) {
	    	g.setColor(Color.BLUE);
	    	g.fillRect(x2, y2, Maze.W, Maze.W);
	    } else if (NoWay) {
	    	g.setColor(Color.BLACK);
	    	g.fillRect(x2, y2, Maze.W, Maze.W);
	    }
	    
	    g.setColor(Color.WHITE);
	    if (walls[0]) {
	    	g.drawLine(x2, y2, x2+Maze.W, y2);
	    }
	    if (walls[1]) {
	    	g.drawLine(x2+Maze.W, y2, x2+Maze.W, y2+Maze.W);
	    }
	    if (walls[2]) {
	    	g.drawLine(x2+Maze.W, y2+Maze.W, x2, y2+Maze.W);
	    }
	    if (walls[3]) {
	    	g.drawLine(x2, y2+Maze.W, x2, y2);
	    } 
	}
	
	public void displayAsColor(Graphics g, Color color) {
		int x2 = x * Maze.W;
	    int y2 = y * Maze.W;
		g.setColor(color);
    	g.fillRect(x2, y2, Maze.W, Maze.W);
	}
	
	public void removeWalls(Cell next) {
		int x = this.x - next.x;
		 // top 0, right 1, bottom 2, left 3
		
		if(x == 1) {
			walls[3] = false;
			next.walls[1] = false;
		} else if (x == -1) {
			walls[1] = false;
			next.walls[3] = false;
		}
		
		int y = this.y - next.y;
		
		if(y == 1) {
			walls[0] = false;
			next.walls[2] = false;
		} else if (y == -1) {
			walls[2] = false;
			next.walls[0] = false;
		}
	}


	private Cell checkNeighbourInGridBounds(List<Cell> grid, Cell neighbour) {
		if (grid.contains(neighbour)) {
			return grid.get(grid.indexOf(neighbour));
		} else {
			return null;
		}
	}

	private Cell randomNeighbour(List<Cell> neighbours) {
		if (neighbours.size() > 0) {
			return neighbours.get(new Random().nextInt(neighbours.size()));
		} else {
			return null;
		}
	}
	
	public List<Cell> getValidMoveNeighbours(List<Cell> grid) {
		List<Cell> neighbours = new ArrayList<Cell>(4);
		
		Cell top = checkNeighbourInGridBounds(grid, new Cell(x, y - 1));
		Cell right = checkNeighbourInGridBounds(grid, new Cell(x + 1, y));
		Cell bottom = checkNeighbourInGridBounds(grid, new Cell(x, y + 1));
		Cell left = checkNeighbourInGridBounds(grid, new Cell(x - 1, y));
		
		if (top != null) {
			if (!walls[0]) neighbours.add(top);
		}	
		
		if (right != null) {
			if (!walls[1]) neighbours.add(right);
		}
		
		if (bottom != null) {
			if (!walls[2]) neighbours.add(bottom);
		}
		
		if (left != null) {
			if (!walls[3]) neighbours.add(left);
		}
		
		return neighbours;
	}

	public Cell getPathNeighbour(List<Cell> grid) {
		List<Cell> neighbours = new ArrayList<Cell>();
		
		Cell top = checkNeighbourInGridBounds(grid, new Cell(x, y - 1));
		Cell right = checkNeighbourInGridBounds(grid, new Cell(x + 1, y));
		Cell bottom = checkNeighbourInGridBounds(grid, new Cell(x, y + 1));
		Cell left = checkNeighbourInGridBounds(grid, new Cell(x - 1, y));
		
		if (top != null) {
			if (!top.NoWay) {
				if (!walls[0]) neighbours.add(top);
			}
		}

		if (bottom != null) {
			if (!bottom.NoWay) {
				if (!walls[2]) neighbours.add(bottom);
			}
		}

		if (right != null) {
			if (!right.NoWay) {
				if (!walls[1]) neighbours.add(right);
			}
		}
		
		if (left != null) {
			if (!left.NoWay) {
				if (!walls[3]) neighbours.add(left);
			}
		}
		
		if (neighbours.size() ==  1) {
			return neighbours.get(0);
		}
		
		return randomNeighbour(neighbours);
	}

	public Cell getRightNeighbour(List<Cell> grid) {
		return checkNeighbourInGridBounds(grid, new Cell(x + 1, y));
	}
	
	public Cell getBottomNeighbour(List<Cell> grid) {
		return checkNeighbourInGridBounds(grid, new Cell(x, y + 1));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}