package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import ai.AIMain;
import component.Window;

public class GameState {

	private static GameState instance;

	public static GameState getInstance() {
		if (instance == null) {
			instance = new GameState();
		}
		return instance;
	}

	public static final int WHITE = -1;
	public static final int BLACK = 1;
	public static final int EMPTY = 0;

	private int playerX, playerY;
	private int newX, newY;
	private boolean playerTurn;
	private int[][] gameboard;
	private boolean run;
	private Color boardbg;
	private AIMain ai;

	/**
	 * sets up field variables and creates gameboard
	 */
	public GameState() {
		ai = new AIMain();

		newX = -1;
		newY = -1;

		playerX = 9;
		playerY = 9;

		playerTurn = true;

		gameboard = new int[18][18];
		boardbg = new Color(166, 100, 39);

		run = true;
	}

	/**
	 * places a piece on the board
	 * 
	 * @param x
	 *            x position of piece to place
	 * @param y
	 *            y position of piece to place
	 * @return true if valid move (makes move), otherwise false (doesnt make move)
	 */
	public boolean placePiece(int x, int y) {
		if (gameboard[y][x] != 0)
			return false;
		gameboard[y][x] = playerTurn ? BLACK : WHITE;
		newX = x;
		newY = y;
		return true;
	}

	/**
	 * called whenever a key is pressed
	 * 
	 * @param e
	 *            keyevent relating to the key press
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			playerX++;
		else if (e.getKeyCode() == KeyEvent.VK_LEFT)
			playerX--;
		else if (e.getKeyCode() == KeyEvent.VK_UP)
			playerY--;
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
			playerY++;
		else if (e.getKeyCode() == KeyEvent.VK_R)
			instance = new GameState();
		else if (e.getKeyChar() == KeyEvent.VK_ENTER && playerTurn && run) {
			if (placePiece(playerX, playerY)) {
				playerTurn = !playerTurn;

				if (checkWin())
					run = false;
				else {
					ai.makeMove(gameboard.clone());

					if (checkWin()) {
						run = false;
					}

					playerTurn = !playerTurn;
				}
			}
		}

		if (playerX > 17)
			playerX = 0;
		else if (playerX < 0)
			playerX = 17;

		if (playerY > 17)
			playerY = 0;
		else if (playerY < 0)
			playerY = 17;
	}

	/**
	 * uses below algorithm to check if a set of 5 pieces exists
	 * 
	 * @return true if win exists, false otherwise
	 */
	private boolean checkWin() {
		return winHorizontal() || winVertical() || winDiagonal1() || winDiagonal2();
	}

	/**
	 * checks for a win along the rows
	 * 
	 * @return true if win present, false otherwise
	 */
	private boolean winHorizontal() {
		int consec = 0;
		int color = EMPTY;
		for (int i = 0; i < gameboard.length; i++) {
			for (int j = 0; j < gameboard[i].length; j++) {
				// read consecutive
				if (gameboard[i][j] != EMPTY) {
					if (color == gameboard[i][j]) {
						consec++;
						if (consec == 5)
							return true;
					} else {
						consec = 1;
						color = gameboard[i][j];
					}
				}

				// read empty space
				else
					consec = 0;
			}

			// reach end of board with series
			consec = 0;
		}
		return false;
	}

	/**
	 * checks for a win along the columns
	 * 
	 * @return true if win present, false otherwise
	 */
	private boolean winVertical() {
		int consec = 0;
		int color = EMPTY;
		for (int j = 0; j < gameboard[0].length; j++) {
			for (int i = 0; i < gameboard.length; i++) {
				// read consecutive
				if (gameboard[i][j] != EMPTY) {
					if (color == gameboard[i][j]) {
						consec++;
						if (consec == 5)
							return true;
					} else {
						consec = 1;
						color = gameboard[i][j];
					}
				}

				// read empty space
				else
					consec = 0;
			}

			// reach end of board with series
			consec = 0;
		}
		return false;
	}

	/**
	 * checks for a win along the diagonal (top left to bottom right)
	 * 
	 * @return true if win present, false otherwise
	 */
	private boolean winDiagonal1() {
		int consec = 0;
		int color = EMPTY;
		for (int k = 0; k < 2 * gameboard.length; k++) {
			for (int j = 0; j <= k; j++) {
				int i = k - j;
				if (i < gameboard.length && j < gameboard.length) {
					// read consecutive
					if (gameboard[i][j] != EMPTY) {
						if (color == gameboard[i][j]) {
							consec++;
							if (consec == 5)
								return true;
						} else {
							consec = 1;
							color = gameboard[i][j];
						}
					}

					// read empty space
					else
						consec = 0;
				}
			}
			// reach end of board with series
			consec = 0;
		}

		return false;
	}

	/**
	 * checks for a win along the diagonal (top right to bottom left)
	 * 
	 * @return true if win present, false otherwise
	 */
	private boolean winDiagonal2() {
		int consec = 0;
		int color = EMPTY;
		for (int k = 0; k < 2 * gameboard.length; k++) {
			for (int j = 0; j <= k; j++) {
				int i = k - j;
				if (i < gameboard.length && j < gameboard.length) {
					int x = gameboard.length - 1 - j;
					int y = i;
					// read consecutive
					if (gameboard[y][x] != EMPTY) {
						if (color == gameboard[y][x]) {
							consec++;
							if (consec == 5)
								return true;
						} else {
							consec = 1;
							color = gameboard[y][x];
						}
					}

					// read empty space
					else
						consec = 0;
				}
			}
			// reach end of board with series
			consec = 0;
		}

		return false;
	}

	/**
	 * renders the background of the window
	 * 
	 * @param g
	 *            graphics object relating to the window
	 */
	private void renderBG(Graphics g) {
		g.setColor(boardbg);
		g.fillRect(0, 0, Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);
	}

	/**
	 * renders the game board
	 * 
	 * @param g
	 *            graphics object relating to the window
	 */
	private void renderBoard(Graphics g) {
		g.setColor(Color.BLACK);
		float buffer = ((Window.WINDOW_WIDTH * 1.0f) / 18) / 2;
		float x = buffer;
		for (int i = 0; i < 19; i++) {
			g.drawLine((int) x, (int) buffer, (int) x, Window.WINDOW_WIDTH - (int) buffer);
			g.drawLine((int) buffer, (int) x, Window.WINDOW_WIDTH - (int) buffer, (int) x);
			x += (Window.WINDOW_WIDTH * 1.0f) / 18;
		}
	}

	/**
	 * renders the pieces of the game
	 * 
	 * @param g
	 *            graphics object relating to the window
	 */
	private void renderPieces(Graphics g) {
		for (int i = 0; i < 18; i++) {
			for (int j = 0; j < 18; j++) {
				if (gameboard[i][j] != 0) {
					if (gameboard[i][j] == BLACK)
						g.setColor(Color.BLACK);
					else
						g.setColor(Color.WHITE);

					g.fillOval(j * Window.WINDOW_HEIGHT / 18, i * Window.WINDOW_WIDTH / 18, Window.WINDOW_WIDTH / 18,
							Window.WINDOW_HEIGHT / 18);
				}
			}
		}
	}

	/**
	 * main render method, called upon board update
	 * 
	 * @param g
	 *            graphics object relating to the window
	 */
	public void render(Graphics g) {
		renderBG(g);
		renderBoard(g);
		renderPieces(g);

		if (newX != -1) {
			g.setColor(Color.BLUE);
			g.drawRect(newX * Window.WINDOW_WIDTH / 18, newY * Window.WINDOW_HEIGHT / 18, Window.WINDOW_HEIGHT / 18,
					Window.WINDOW_HEIGHT / 18);
		}

		g.setColor(Color.RED);
		g.drawRect(playerX * Window.WINDOW_WIDTH / 18, playerY * Window.WINDOW_HEIGHT / 18, Window.WINDOW_HEIGHT / 18,
				Window.WINDOW_HEIGHT / 18);

		if (run == false) {
			g.setColor(Color.RED);
			g.drawString("GAME COMPLETE!", 100, 100);
		}
	}

}
