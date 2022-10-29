package zeroxgame;

import java.awt.Point;

import java.util.*;
import java.util.stream.Stream;

public class Strategies {

	private VictorySolutions[] solutions = VictorySolutions.values();
	private Map<Integer, Point> steps;
	private Random random = new Random();
	private String userMove = "X";
	private String myMove = "O";

	public Strategies() {
		steps = new HashMap<>();
	}

	private int oppositeCorner(int side) {
		return side == 2 ? 0 : 2;
	}

	private Point checkPossibleWin(String[][] board, String symbol) {

		Point winningCoord = null;

		for (int i = 0; i < solutions.length; i++) {
			int emptyCells = 0;
			int symbolsInLine = 0;

			Point[] solution = solutions[i].getSolution();

			for (Point coord : solution) {
				int x = coord.x;
				int y = coord.y;

				if (board[x][y].equals(symbol)) symbolsInLine++;
				else if (board[x][y].equals("Empty")) {
					emptyCells++;
					winningCoord = new Point(x, y);
				}
			}

			if (symbolsInLine == 2 && emptyCells == 1) return winningCoord;
			else winningCoord = null;
		}
		return winningCoord;
	}

	private Point randomCorner() {
		
		int xrandom = random.nextBoolean() ? 2 : 0;
		int yrandom = random.nextBoolean() ? 2 : 0;
		
		return new Point(xrandom, yrandom);
	}
	
	private int goOppositeSideConst(int corner) {
		return corner == 2 ? -1 : 1;//to trough the opposite side
	}

	private void buildingWinStrategy(String[][] board, int step) {

		if (step == 1) steps.put(1, randomCorner());//chose a corner is necessary to win the game
		else if (step == 2) {
			// in the second step we have to make a triangle in order to win the game in the
			// opposite site that the player has put his X, but if the user has put the x in
			// the center
			// there is a small chance to win the game
			Point firstMove = steps.get(1);

			int x = firstMove.x;
			int y = firstMove.y;

			/*
			 * the edge was chosen randomly this is the cause because we have to check if we
			 * increase or decrease in the x and y coordinates
			 */
			int xVary = goOppositeSideConst(x); int yVary = goOppositeSideConst(y);
			int xOpposite = oppositeCorner(x); int yOpposite = oppositeCorner(y);
			
		
			while(x != xOpposite && y != yOpposite) {
				x += xVary; y += yVary;
				if (board[firstMove.x][y].equals(userMove)) steps.put(2, new Point(xOpposite, firstMove.y));
				else if(board[x][firstMove.y].equals(userMove)) steps.put(2, new Point(firstMove.x, yOpposite));
				
				if(steps.get(2) != null) steps.put(3, new Point(xOpposite, yOpposite));//if 2 steps was put 
			}

			if (board[firstMove.x + xVary][yOpposite].equals(userMove) || 
				board[xOpposite][yOpposite].equals(userMove)           || 
				board[xOpposite][firstMove.y + yVary].equals(userMove)) {

				steps.put(2, new Point(xOpposite, firstMove.y));
				steps.put(3, new Point(firstMove.x, yOpposite));
			} else if (board[1][1].equals(userMove)) {
				// if user pick center in his move there is a small chance to win
				steps.put(2, new Point(xOpposite, yOpposite));
			}

		}

	}

	public Point winStrategy(String[][] board, int step) {

		if (step <= 2) {
			buildingWinStrategy(board, step);
			return steps.get(step);
		}

		Point winningCoord = checkPossibleWin(board, myMove);// check possible machine win

		if (winningCoord != null) return winningCoord;

		Point userPossibleWin = checkPossibleWin(board, userMove);// check possible user win to defend

		if (userPossibleWin != null) return userPossibleWin;

		return steps.get(step);//if there was no intended to make three in line of the user, keep the steps
	}
	
	public Point drawStrategy(String[][] board, int step) {

		Point winningCoord = checkPossibleWin(board, myMove);// check possible machine win

		if (winningCoord != null) return winningCoord;

		Point userPossibleWin = checkPossibleWin(board, userMove);// check possible user win to defend

		if (userPossibleWin != null) return userPossibleWin;

		if (step <= 2) {
			buildingDefenseStrategy(board, step);
			return steps.get(step);
		}
		
		return randomMovement(board);

	}

	public Point randomMovement(String[][] board) {
		
		for (int y = 0; y <= 2; y++) 
			for (int x = 0; x <= 2; x++) 
				if (board[x][y].equals("Empty")) return new Point(x, y);

		return null;
	}

	private boolean isCorner(Point point) {
		boolean isPointCorner = false;

		int x = point.x;
		int y = point.y;

		if ((x == 0 || x == 2) && (y == 0 || y == 2)) isPointCorner = true;

		return isPointCorner;
	}
	
	private int getCoordExtreme(int coord1, int coord2) {
		return (coord1 == 0 || coord2 == 0) ? 0 : 2;
	}
	
	private Point oppositeExtremeDefense(String board[][]) {
		Point defense = null;
		if (board[0][1].equals(userMove) && board[2][1].equals(userMove)) {
			defense = random.nextBoolean() ? new Point(1, 0) : new Point(1, 2);
		} else if(board[1][0].equals(userMove) && board[1][2].equals(userMove)) {
			defense = random.nextBoolean() ? new Point(0, 1) : new Point(2, 1);
		}
		
		return defense;
	}
	
														// |   |1,0|   |
	private Point defenseCenterMoves(String board[][]) {// |0,1|   |2,1|//attacks in the middle from the user
		Point defense = null;							// |   |1,2|   |
		int[][] centerAttacks = {{1, 0}, {2, 1}, {1, 2}, {0, 1}, {1, 0}};
 
		for(int i = 1; i < centerAttacks.length; i++) {
			int xone = centerAttacks[i - 1][0]; int xtwo = centerAttacks[i][0]; 
			int yone = centerAttacks[i - 1][1]; int ytwo = centerAttacks[i][1]; 
			
			if(board[xone][yone].equals(userMove) && board[xtwo][ytwo].equals(userMove)) {
				defense = new Point(getCoordExtreme(xone, xtwo), getCoordExtreme(yone, ytwo));
			}
		}
		
		if(defense == null) defense = oppositeExtremeDefense(board);

		return defense;
	}

	private Point defenseDiagonalMoves(String board[][], Point point) {
		Point defense = null;
		
		int xfromMyFirstMove = point.x;
		int yfromMyFirstMove = point.y;

		if (board[1][1].equals(userMove) && //center (1, 1,) and the opposite corner
			board[oppositeCorner(xfromMyFirstMove)][oppositeCorner(yfromMyFirstMove)].equals(userMove)) {
			defense = new Point(oppositeCorner(xfromMyFirstMove), yfromMyFirstMove);
		}

		return defense;
	}

	private void buildingDefenseStrategy(String board[][], int step) {
		if (step == 1) {
			if (board[1][1].equals("Empty")) steps.put(1, new Point(1, 1));// check if center is free
			else steps.put(1, randomCorner());//if center is not available chose any corner
		} else if (step == 2) {
			Point myFirstMove = steps.get(1);

			int x = myFirstMove.x;
			int y = myFirstMove.y;

			List<Point> userMoves = new ArrayList<>();

			for (int yUser = 0; yUser <= 2; yUser++) {
				for (int xUser = 0; xUser <= 2; xUser++) {
					if (board[xUser][yUser].equals(userMove))
						userMoves.add(new Point(xUser, yUser));
				}
			}

			List<Point> cornerMoves = userMoves.stream().filter(move -> isCorner(move)).toList();

			if (x == 1 && y == 1) {// if last move was put in the center
				Point defense = switch(cornerMoves.size()) {
					case 2 -> new Point(1,0); // two moves in corner == user trying to make a triangle
					case 1 -> {
						Point corner = cornerMoves.get(0);
						int oppositeX = oppositeCorner(corner.x); int oppositeY = oppositeCorner(corner.y);

						Point defending = null;
						if (board[oppositeX][1].equals(userMove)) defending = new Point(oppositeX, corner.y);
						else if (board[1][oppositeY].equals(userMove)) defending = new Point(corner.x, oppositeY);
						
						yield defending;
					}
					default -> defenseCenterMoves(board);
				};
				
				steps.put(2, defense);
				
			} else {
				steps.put(2, defenseDiagonalMoves(board, myFirstMove));
			}

		}
	}
}
