package snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;

import javax.swing.Timer;

import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.views.AbstractView;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Board extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int Height, Width;
	public final int SIZE_SQUARES = 40;
	private final Color BOARD_COLOR = new Color(0, 0, 0);
	private final Color SNAKE_COLOR = new Color(0, 255, 23);
	private final Color FOOD_COLOR = new Color(255, 0, 0);
	private final Color BORDER_COLOR = new Color(255, 255, 255);
	private final Color FONT_LOST_WIN_GAME = new Color(255, 0, 0);
	public String[] positions;
	public int[] deathPositionsRight;
	public int[] deathPositionsLeft;
	private Snake snake;
	private final int SPEED = 75;
	public char direction = 'r';
	public char newDirection = 'r';
	public int oldPosition;
	public int newPosition;
	public int[] foodPos;
	public String[] snakePos;
	public boolean running = false;
	public boolean gameLost = false;
	public boolean gameWin = false;
	public boolean foodAte = false;
	public final int SIZE_BOARD;

	public Board(int Height, int Width) {
		this.Width = Width;
		this.Height = Height;
		this.SIZE_BOARD = (this.Height / this.SIZE_SQUARES) * (this.Height / this.SIZE_SQUARES);
		this.positions = new String[this.SIZE_BOARD];
		this.snakePos = new String[this.SIZE_BOARD];
		this.fillPositions();
		this.snakePos[0] = "0";
		this.snakePos[1] = "1";
		this.snakePos[2] = "2";
		this.deathPositionsRight = new int[(this.Width / this.SIZE_SQUARES)];
		this.deathPositionsLeft = new int[(this.Width / this.SIZE_SQUARES)];
		this.snake = new Snake(this);
		this.foodPos = new Food(this.SIZE_BOARD, this).createFood();
		this.setPreferredSize(new Dimension(this.Height, this.Width));
		this.setVisible(true);
		this.setFocusable(true);
		this.addKeyListener(new Keys());
		this.start();
		this.fillPositionsDeath();
	}

	private void start() {
		Timer timer = new Timer(this.SPEED, this);
		timer.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int row = 0; row < this.Height / this.SIZE_SQUARES; row++) {
			for (int column = 0; column < this.Width / this.SIZE_SQUARES; column++) {
				g.setColor(this.BOARD_COLOR);
				g.fillRect(column * this.SIZE_SQUARES, row * this.SIZE_SQUARES, this.SIZE_SQUARES, this.SIZE_SQUARES);
				g.setColor(this.BORDER_COLOR);
				g.drawRect(column * this.SIZE_SQUARES, row * this.SIZE_SQUARES, this.SIZE_SQUARES, this.SIZE_SQUARES);
			}
		}

		drawSnake(g);
		drawFood(g);
		removefood(g);
		lostWinDisplay(g);
	}

	public void drawFood(Graphics g) {
		g.setColor(this.FOOD_COLOR);
		g.fillRect(this.foodPos[0], this.foodPos[1], this.SIZE_SQUARES, this.SIZE_SQUARES);
	}

	public void removefood(Graphics g) {
		if (this.foodAte) {
			g.setColor(this.SNAKE_COLOR);
			g.fillRect(this.foodPos[0], this.foodPos[1], this.SIZE_SQUARES, this.SIZE_SQUARES);
			this.foodPos[0] = 0;
			this.foodPos[1] = 0;
			this.foodAte = false;
			this.foodPos = new Food(this.SIZE_BOARD, this).createFood();
		}
	}

	public void lostWinDisplay(Graphics g) {
		if (this.gameLost || this.gameWin) {
			for (int row = 0; row < this.Height / this.SIZE_SQUARES; row++) {
				for (int column = 0; column < this.Width / this.SIZE_SQUARES; column++) {
					g.setColor(this.BOARD_COLOR);
					g.fillRect(column * this.SIZE_SQUARES, row * this.SIZE_SQUARES, this.SIZE_SQUARES, this.SIZE_SQUARES);
				}
			}
			
			g.setColor(this.FONT_LOST_WIN_GAME);
			Font font = new Font("Comic Sans MS", Font.PLAIN, 60);
			g.setFont(font);
			FontMetrics metrics = getFontMetrics(g.getFont());

			if(this.gameLost) {
				g.drawString("Game over", (this.Width - metrics.stringWidth("Game Over")) / 2, (this.Height / 2));				
			}else if(this.gameWin) {
				g.drawString("Game win", (this.Width - metrics.stringWidth("Game win")) / 2, (this.Height / 2));
			}
		}
	}

	public void drawSnake(Graphics g) {
		g.setColor(this.SNAKE_COLOR);
		for (String i : this.snakePos) {
			if (i != null) {
				int pos = Integer.parseInt(i);
				int x = Integer.parseInt(this.positions[pos].split(",")[0]);
				int y = Integer.parseInt(this.positions[pos].split(",")[1]);
				g.fillRect(x, y, this.SIZE_SQUARES, this.SIZE_SQUARES);
			}
		}
	}

	public void fillPositions() {
		int counter = 0;
		for (int row = 0; row < this.Height / this.SIZE_SQUARES; row++) {
			for (int column = 0; column < this.Width / this.SIZE_SQUARES; column++) {
				this.positions[counter] = column * this.SIZE_SQUARES + "," + row * this.SIZE_SQUARES;
				counter++;
			}
		}
	}

	// this will be in charge of filling the position where the snake can die on
	// horizontal
	public void fillPositionsDeath() {
		for (int i = 0; i < (this.Height / this.SIZE_SQUARES); i++) {
			this.deathPositionsRight[i] = (this.Height / this.SIZE_SQUARES) * i > 0 ? (this.Height / this.SIZE_SQUARES) * i : (this.Height / this.SIZE_SQUARES);
			this.deathPositionsLeft[i] = this.deathPositionsRight[i] - 1;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (running && !this.gameLost) {
			this.snake.move();
			repaint();
		}
	}
	
	public class Keys extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			// 38 top 40 bottom 37 left 39 right
			if (e.getKeyCode() == 38) {
				direction = newDirection;
				newDirection = 't';
				oldPosition = newPosition;
				newPosition = -(Height / SIZE_SQUARES);
				running = true;
			} else if (e.getKeyCode() == 40) {
				direction = newDirection;
				newDirection = 'b';
				oldPosition = newPosition;
				newPosition = (Height / SIZE_SQUARES);
				running = true;
			} else if (e.getKeyCode() == 37) {
				direction = newDirection;
				newDirection = 'l';
				oldPosition = newPosition;
				newPosition = -1;
				running = true;
			} else if (e.getKeyCode() == 39) {
				direction = newDirection;
				newDirection = 'r';
				oldPosition = newPosition;
				newPosition = 1;
				running = true;
			}
		}
	}

}
