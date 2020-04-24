package solver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import javax.swing.Timer;

import main.*;
import util.Cell;

public class BiDFSSolve {

	private final Stack<Cell> path1 = new Stack<Cell>();
	private final Stack<Cell> path2 = new Stack<Cell>();
	private Cell curr1, curr2;
	private final List<Cell> grid;

	public BiDFSSolve(List<Cell> grid, MazeGridPanel panel) {
		this.grid = grid;
		curr1 = grid.get(0);
		curr2 = grid.get(grid.size() - 1);
		final Timer timer = new Timer(Maze.speed, null);
		timer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!pathFound()) {	
					pathfromEndingpoint();
					pathfromStartingpoint();
				} else {
					curr1 = null;
					curr2 = null;
					Maze.solved = true;
					FinalPath();
					timer.stop();
				}
				panel.setCurCells(Arrays.asList(curr1, curr2));
				panel.repaint();
				timer.setDelay(Maze.speed);
			}
		});
		timer.start();
	}

	private void pathfromStartingpoint() {
		curr1.setNoWay(true);
		Cell next = curr1.getPathNeighbour(grid);
		if (next != null) {
			path1.push(curr1);
			curr1 = next;
		} else if (!path1.isEmpty()) {
			try {
				curr1 = path1.pop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void pathfromEndingpoint() {
		curr2.setNoWay(true);
		Cell next = curr2.getPathNeighbour(grid);
		if (next != null) {
			path2.push(curr2);
			curr2 = next;
		} else if (!path2.isEmpty()) {
			try {
				curr2 = path2.pop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean pathFound() {
		List<Cell> neighs1 = curr1.getValidMoveNeighbours(grid);
		List<Cell> neighs2 = curr2.getValidMoveNeighbours(grid);
		for (Cell c : neighs1) {
			if (path2.contains(c)) {
				path1.push(curr1);
				path1.push(c);
				joinPaths(c, path2, curr2);
				return true;
			}
		}
		for (Cell c : neighs2) {
			if (path1.contains(c)) {
				path2.push(curr2);
				path2.push(c);
				joinPaths(c, path1, curr1);
				return true;
			}
		}
		return false;
	}
	
	private void FinalPath() {
		while (!path1.isEmpty()) {
			try {
				path1.pop().setPath(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void joinPaths(Cell c, Stack<Cell> path, Cell current) {
		while (!path.isEmpty() && !current.equals(c)) {
			try {
				current = path.pop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		path1.addAll(path2);
	}
}
