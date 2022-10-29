package zeroxgame;
import javax.swing.*;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;


public class Board extends JPanel{
	
	private final VictorySolutions solutions[] = VictorySolutions.values();
	private boolean inPlay;
	private String gameBoard[][];
	private JButton buttonAt[][];
	private Opponent opponent;
	private final int MAX_ATTACKS = 9;
	private final int WIDTH_BOARD = 300;
	private final int HEIGHT_BOARD = 300;
	private int attacksPerformed;
	private Random random = new Random();
	
	public Board() {
		initBoard();
		initGame();
	}
	
	private void initBoard() {
		setFocusable(true);
		addKeyListener(new KeyTyped());
		setPreferredSize(new Dimension(WIDTH_BOARD, HEIGHT_BOARD));
		setLayout(new GridLayout(3, 3));
	}
	
	private void initGame() {
		inPlay = true;
		attacksPerformed = 0;
		opponent = new Opponent();
		gameBoard = new String[3][3];//this is use to keep track of the previous attacks
		buttonAt = new JButton[3][3];//buttons to get coord of button touched and draw the symbol
		initGameBoard();
	}
	
	public void determineFirstPlayer() {
		String options[] = {"Tail", "Head"};
		
		int selection = JOptionPane.showOptionDialog(this, "This determine the first player", 
				"Select An Option", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
			
		String firstAttack = random.nextBoolean() ? "Tail" : "Head";
		
		if(selection == -1 || !firstAttack.equals(options[selection])) {//-1 when user press exit
			opponent.setStrategy(true);
			opponentAttack();
		}
	}
	
	public void opponentAttack() {
		Point opponentCoordAttack = opponent.attack(gameBoard);
		int x = opponentCoordAttack.x;
		int y = opponentCoordAttack.y;
		gameBoard[x][y] = "O";
		buttonAt[x][y].putClientProperty("NoughtOrCross", "O");
		increaseAttacksPerformed();
		
		if(isOpponentWin()) {
			showResultDialog("The user has been humilliated");
		} else if(isGameDraw()) {
			showResultDialog("This has been a draw, well played");
		}
	}
	
	public void increaseAttacksPerformed() {
		attacksPerformed++;
		repaint();
	}
	
	public void showResultDialog(String dialogInfo) {
		JOptionPane.showMessageDialog(this, dialogInfo, "Result", JOptionPane.INFORMATION_MESSAGE);
		clearBoard();
		initGame();
	}
	
	public void clearBoard() {
		removeAll();
	}
	
	public boolean isGameDraw() {
		return attacksPerformed == MAX_ATTACKS;
	}
	
	public boolean isOpponentWin() {
		//we only check for opponent victory because its impossible to beat the machine
		for(int i = 0; i < solutions.length; i++) {
			
			int symbolsInLine = 0;
			
			Point[] solution = solutions[i].getSolution();
			
			for(Point coord : solution) {
				int x = coord.x;
				int y = coord.y;
				
				if(gameBoard[x][y] == "O") symbolsInLine++;
			}
			
			if(symbolsInLine == 3) return true;
		}
		
		return false;
	}
	
	public void userAttack(ActionEvent ae) {
		JButton attackSource = (JButton) ae.getSource();
		Point coordAttack = (Point) attackSource.getClientProperty("coordinates");
		
		int x = coordAttack.x;
		int y = coordAttack.y;
		
		if(gameBoard[x][y].equals("Empty")) {
			gameBoard[x][y] = "X";		
			attackSource.putClientProperty("NoughtOrCross", "X");	
			increaseAttacksPerformed();
			
			if(isGameDraw()) {
				showResultDialog("This has been a draw, well played");
			} else {
				opponentAttack();
			}

		}
			
	}
	
	private void initGameBoard() {
		for(int y = 0; y < 3; y++) {
			for(int x = 0; x < 3; x++) {
				JButton button = new JButton();
				button.putClientProperty("coordinates", new Point(x, y));
				button.setUI(new CustomButton());
				button.addActionListener(e -> userAttack(e));
				gameBoard[x][y] = "Empty";
				buttonAt[x][y] = button;
				add(button);
			}
		}
		
		validate();
		repaint();
		
		determineFirstPlayer();
	}
	
	
	class KeyTyped extends KeyAdapter {
		public void keyPressed(KeyEvent ae) {
			int key = ae.getKeyCode();
			
			if(key == KeyEvent.VK_ESCAPE) {
				clearBoard();
				initGame();//esc is used to restart
			}
		}
	}
}
