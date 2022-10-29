package zeroxgame;
import java.awt.*;
//functional interface to switch between win or draw depending on the first move of the game
public interface Strategy {
	public Point makeMove(String[][] board, int step);
}
