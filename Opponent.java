package zeroxgame;

import java.awt.*;

public class Opponent {

	private final String NOUGTH = "O";
	private boolean winStrategy;
	private int steps;
	private Strategies strategy;

	public Opponent() {
		steps = 1;
		strategy = new Strategies();
		winStrategy = false;
	}

	public void setStrategy(boolean win) {
		winStrategy = win;
	}

	public Point getCoordAttack(Strategy strategy, String[][] board) {
		return strategy.makeMove(board, steps);
	}

	public Point attack(String[][] board) {
		Point coord;

		if (winStrategy) {
			coord = getCoordAttack(strategy::winStrategy, board);
		} else {
			coord = getCoordAttack(strategy::drawStrategy, board);
		}

		steps++;

		return coord;
	}

}
