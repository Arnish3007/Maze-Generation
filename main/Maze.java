package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Maze {

	public static final int WIDTH = 800;
	public static final int HEIGHT = WIDTH; // best to keep these the same. variable is only created for readability.
	public static final int W = 20;
	
	public static int speed = 1;
	public static boolean generated, solved;
	
	private static final String[] GENERATION_METHODS = {"Eller's"};
	private static final String[] SOLVING_METHODS = {"Bi-directional DFS"};

	private int cols, rows;

	public static void main(String[] args) {
		new Maze();
	}

	public Maze() {
		cols = Math.floorDiv(WIDTH, W);
		rows = cols;

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException ex) {
					ex.printStackTrace();
				}
				createAndShowGUI();
			}
		});
	}

	private void createAndShowGUI() {
		JFrame frame = new JFrame("Generating Maze using Eller's Algorithm");

		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		frame.setContentPane(container);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		MazeGridPanel grid = new MazeGridPanel(rows, cols);
		grid.setBackground(Color.BLACK);
		
		JPanel mazeBorder = new JPanel();
		final int BORDER_SIZE = 20;
		mazeBorder.setBounds(0, 0, WIDTH + BORDER_SIZE, HEIGHT + BORDER_SIZE);
		mazeBorder.setBackground(Color.BLACK);
		mazeBorder.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
		
		mazeBorder.add(grid);
		
		container.add(mazeBorder);
		
		CardLayout cardLayout = new CardLayout();

		JButton runButton = new JButton("Run");
		JButton solveButton = new JButton("Solve");

		// Create the card panels.
		JPanel card1 = new JPanel();
		JPanel card2 = new JPanel();
		card1.setLayout(new GridBagLayout());
		card2.setLayout(new GridBagLayout());
		
	    GridBagConstraints c = new GridBagConstraints();;
	 
	    c.insets = new Insets(5, 0, 5, 18);
	    c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.7;
	    c.gridx = 0;
		c.gridy = 0;

		c.gridheight = 2;
		c.weightx = 0.3;
		c.gridx = 1;
		c.gridy = 0;
		card1.add(runButton, c);
		card2.add(solveButton, c);
		
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 1;

		c.gridx = 0;
		c.gridy = 0;
		c.gridx = 0;
		c.gridy = 1;
		c.gridx = 0;
		c.gridy = 2;

		// Create the panel that contains the cards.
		JPanel cards = new JPanel(cardLayout);
		cards.setBorder(new EmptyBorder(0, 20, 0, 0));
		cards.setOpaque(false);
		cards.add(card1, "gen");
		cards.add(card2, "solve");
		
		container.add(cards);

		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				speed = 5;
				generated = false;
				solved = false;
				grid.generate();
				cardLayout.next(cards);
			}
		});

		solveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (generated) {
					frame.setTitle("Solving Maze using BiDFS");
					grid.solve();
					cardLayout.last(cards);
				} else {
					JOptionPane.showMessageDialog(frame, "Sit tight, The maze is being generated");
				}
			}
		});

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
