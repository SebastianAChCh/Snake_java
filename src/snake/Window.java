package snake;

import javax.swing.JFrame;

public class Window extends JFrame {
	public Window(int Height, int Width) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.add(new Board(Height, Width));
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
}
