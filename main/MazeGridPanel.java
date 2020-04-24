package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import generator.*;
import solver.*;
import util.Cell;

public class MazeGridPanel extends JPanel {

	private static final long serialVersionUID = 7237062514425122227L;
	private final List<Cell> grid = new ArrayList<>();
	private List<Cell> curCells = new ArrayList<>();

	public MazeGridPanel(int rows, int cols) {
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				grid.add(new Cell(x, y));
			}
		}
	}

	public void generate() {
		new EllersGen(grid, this);
	}

	public void solve() {
		new BiDFSSolve(grid, this);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(Maze.WIDTH + 1, Maze.HEIGHT + 1);
	}


	
	public void setCurrent(Cell current) {
		if(curCells.size() == 0) {
			curCells.add(current);
		} else {
			curCells.set(0, current);
		}
	}
	
	public void setCurCells(List<Cell> curCells) {
		this.curCells = curCells;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Cell c : grid) {
			c.draw(g);
		}
		for (Cell c : curCells) {
			if(c != null) c.displayAsColor(g, Color.ORANGE);
		}
		grid.get(0).displayAsColor(g, Color.GREEN);
		grid.get(grid.size() - 1).displayAsColor(g, Color.YELLOW);
	}
}
