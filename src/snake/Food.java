package snake;

import java.util.Random;

public class Food {

	private int sizeBoard;
	private Board board;

	public Food(int sizeBoard, Board board) {
		this.sizeBoard = sizeBoard;
		this.board = board;
	}

	public int[] createFood() {
		int foodPos = new Random().nextInt(this.sizeBoard);
		String position = this.board.positions[foodPos];
		boolean snakePosition = false;
		int[] position2 = { Integer.parseInt(position.split(",")[0]), Integer.parseInt(position.split(",")[1]) };

		for (int i = 0; i < this.board.snakePos.length; i++) {
			if (this.board.snakePos[i] != null) {
				int tempPos = Integer.parseInt(this.board.snakePos[i]);
				int pos1 = Integer.parseInt(this.board.positions[tempPos].split(",")[0]);
				int pos2 = Integer.parseInt(this.board.positions[tempPos].split(",")[1]);
				if (pos1 == position2[0] && pos2 == position2[1]) {
					snakePosition = true;
					break;
				}
			}
		}

		if (snakePosition)
			createFood();
		
		return position2;
	}
}
