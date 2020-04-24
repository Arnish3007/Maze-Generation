package generator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import javax.swing.Timer;

import main.Maze;
import main.MazeGridPanel;
import util.Cell;
import util.DisjointSets;



public class EllersGen {
	
	private final List<Cell> grid;
	private List<Cell> initialColumn;
	
	private final DisjointSets disjointSet = new DisjointSets();
	
	private static final int COLS = Math.floorDiv(Maze.WIDTH, Maze.W);
	private int fromIndex, toIndex;
	
	private boolean genNextCol = true;

	public EllersGen(List<Cell> grid, MazeGridPanel panel) {
		this.grid = grid;
		
		fromIndex = 0;
		toIndex = COLS;
		
		for (int i = 0; i < grid.size(); i++) {
			grid.get(i).setId(i);
			disjointSet.SetCreation(grid.get(i).getId());
		}
		
		final Timer timer = new Timer(Maze.speed, null);
		timer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (genNextCol) {
					initialColumn = grid.subList(fromIndex, toIndex);
					fromIndex = toIndex;
					toIndex += COLS;
					new ColumnFormation(initialColumn, panel);
				} else if (grid.parallelStream().allMatch(c -> c.isVisited())) {
					Maze.generated = true;
					timer.stop();
				}
			}
		});
		timer.start();
	}
	
	private class ColumnFormation {

		private final Queue<Cell> IterateRightQueue = new LinkedList<Cell>();
		private final Queue<Cell> IterateDownQueue = new LinkedList<Cell>();
		private final List<Cell> col;
		private final Random r = new Random();
		private Cell current;
		
		private ColumnFormation(List<Cell> col, MazeGridPanel panel) {
			genNextCol = false;
			this.col = col;
			IterateDownQueue.addAll(col);
			IterateRightQueue.addAll(col);
			
			final Timer timer = new Timer(Maze.speed, null);
			timer.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!IterateDownQueue.isEmpty()) {
						IterateDown();
					} else if (!IterateRightQueue.isEmpty()) {
						IterateRight();
					} else {
						current = null;
						genNextCol = true;
						timer.stop();
					}
					panel.setCurrent(current);
					panel.repaint();
					timer.setDelay(Maze.speed);
				}
			});
			timer.start();
		}



		private void IterateRight() {
			Cell c = IterateRightQueue.poll();

			List<Cell> cells = new ArrayList<>();
			for (Cell c2 : col) {
				if (disjointSet.SetFinding(c.getId()) == disjointSet.SetFinding(c2.getId())) {
					cells.add(c2);
				}
			}
			Collections.shuffle(cells);
			Cell c3 = cells.get(0);
			Cell right = c3.getRightNeighbour(grid);
			if (right != null) {
				current = right;
				right.setVisited(true);
				c3.removeWalls(right);
				disjointSet.union(c3.getId(), right.getId());
			}
		}


		private void IterateDown() {
			current = IterateDownQueue.poll();
			current.setVisited(true);
			
			if (r.nextBoolean() || col.contains(grid.get(grid.size() - 1))) {
				Cell bottom = current.getBottomNeighbour(grid);
				if (bottom != null) {
					if (disjointSet.SetFinding(current.getId()) != disjointSet.SetFinding(bottom.getId())) {
						current.removeWalls(bottom);
						disjointSet.union(current.getId(), bottom.getId());
					}
				}
			}
		}	
	}
}