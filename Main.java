package zeroxgame;
import javax.swing.*;

public class Main extends JFrame{
	
	public Main() {
		setTitle("X-O Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(new Board());
		pack();
		setLocationRelativeTo(null);
		
		setVisible(true);
	}
		
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Main();
			}
		});
		
	}

}
