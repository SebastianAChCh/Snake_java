package snake;

public class Snake {
	private Board board;
	private int sizeBoard;

	public Snake(Board board) {
		this.board = board;
		this.sizeBoard = ((this.board.Height / this.board.SIZE_SQUARES) * (this.board.Width / this.board.SIZE_SQUARES));
	}

	public void move() {
		for (int i = 0; i < this.board.snakePos.length; i++) {
			if (this.board.snakePos[i] != null && i + 1 <= this.sizeBoard) {
				if (this.board.snakePos[i + 1] == null) {
					if ((Integer.parseInt(this.board.snakePos[i]) + this.board.newPosition) == Integer.parseInt(this.board.snakePos[i - 1]) && !this.board.gameLost) {
						this.board.newDirection = this.board.direction;
						this.board.newPosition = this.board.oldPosition;
					}
					collisions(Integer.parseInt(this.board.snakePos[i]) + this.board.newPosition);
					if (this.board.gameLost) {
						this.board.running = false;
						break;
					}					
					this.board.snakePos[i + 1] = Integer.parseInt(this.board.snakePos[i]) + this.board.newPosition + "";
					this.board.snakePos[0] = null;
					eatFood(this.board.snakePos[i + 1]);
					checkVictory();
					break;
				}
			}
		}

		this.orderArraySnake();
	}

	public void checkVictory() {
		for (int j = 0; j < this.board.snakePos.length; j++) {
			if(this.board.snakePos[j]==null ) break;
			if(this.board.SIZE_BOARD == j) {
				this.board.gameWin = true;
				this.board.running = false;
				break;
			}
		}
	}
	
	public void orderArraySnake() {
		for (int j = 0; j < this.board.snakePos.length; j++) {
			if (this.board.snakePos[j] == null && j + 1 <= this.sizeBoard) {
				if (!(this.board.snakePos[j + 1] != null))
					break;
				this.board.snakePos[j] = this.board.snakePos[j + 1];
				this.board.snakePos[j + 1] = null;
			}
		}
	}
	

	public void collisions(int lastPosition) {
		switch (this.board.newDirection) {
		case 'r': {
			for (int i = 0; i < this.board.deathPositionsRight.length; i++) {
				if (this.board.deathPositionsRight[i] == lastPosition) {
					this.board.gameLost = !this.board.gameLost;
					break;
				}
			}
			break;
		}
		case 'l': {
			for (int i = 0; i < this.board.deathPositionsLeft.length; i++) {
				if (this.board.deathPositionsLeft[i] == lastPosition) {
					this.board.gameLost = !this.board.gameLost;
					break;
				}
			}
			break;
		}
		case 't': {
			if (lastPosition < 0) {
				this.board.gameLost = !this.board.gameLost;
			}
			break;
		}
		case 'b': {
			if (lastPosition > this.sizeBoard - 1) {
				this.board.gameLost = !this.board.gameLost;
			}
			break;
		}
		default:
		}

		if (!this.board.gameLost) {
			for (String snakePosition : this.board.snakePos) {
				if (snakePosition == null)
					break;
				if (Integer.parseInt(snakePosition) == lastPosition) {
					this.board.gameLost = !this.board.gameLost;
					break;
				}
			}
		}
	}

	public void eatFood(String position) {
		int pos = Integer.parseInt(position);
		int foodPositionX = this.board.foodPos[0];
		int foodPositionY = this.board.foodPos[1];

		if (Integer.parseInt(this.board.positions[pos].split(",")[0]) == foodPositionX && Integer.parseInt(this.board.positions[pos].split(",")[1]) == foodPositionY) {
			for (int j = 0; j < this.board.snakePos.length; j++) {
				if (this.board.snakePos[j] != null && (j + 1) <= this.board.SIZE_BOARD) {
					if (this.board.snakePos[j + 1] == null) {
						this.board.snakePos[j + 1] = Integer.parseInt(this.board.snakePos[j]) + this.board.newPosition + "";
						this.board.foodAte = true;
						break;
					}
				}
			}
		}
	}

}
