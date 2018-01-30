package ai;

import game.GameState;

public class AIMain {

	private int numplys;

	/**
	 * standard constructor, if you wish to change the amount of moves the ai looks
	 * ahead edit the numplys field (must be factor of 2)
	 */
	public AIMain() {
		numplys = 2;
	}

	/**
	 * analyzes and scores a specific shape, taking into account player (this or
	 * player)
	 * 
	 * @param consec
	 *            number consecutive pieces
	 * @param openSpaces
	 *            number of open spaces on either end of the string
	 * @param black
	 *            true if blacks turn, false otherwise
	 * @return a float score of the shape
	 */
	private float analyzeShape(int consec, int openSpaces, boolean black) {
		if (openSpaces == 0)
			return 0;
		else {
			if (consec == 4) {
				if (openSpaces == 2) {
					if (black)
						return 100000000;
					else
						return 5000000;
				} else {
					if (black)
						return 100000000;
					else
						return 50;
				}
			} else if (consec == 3) {
				if (openSpaces == 2) {
					if (black)
						return 10000;
					else
						return 500;
				} else {
					if (black)
						return 7;
					else
						return 5;
				}
			} else if (consec == 2) {
				if (openSpaces == 2)
					return 5;
				else
					return 2;
			} else if (consec == 1) {
				if (openSpaces == 2)
					return 1;
				else
					return 0.5f;
			} else {
				if (black)
					return 200000000;
				else
					return 150000000;
			}
		}
	}

	/**
	 * analyze all horizontal shapes on the board
	 * 
	 * @param board
	 *            gameboard
	 * @param xi
	 *            initial x of cropped board
	 * @param xf
	 *            final x of cropped board
	 * @param yi
	 *            initial y of cropped board
	 * @param yf
	 *            final y of cropped board
	 * @param black
	 *            true if blacks turn, false otherwise
	 * @return
	 */
	private float analyzeBoardScoreHorizontal(int[][] board, int xi, int xf, int yi, int yf, boolean black) {
		float score = 0;

		int consec = 0;
		int numSpaces = 0;
		for (int i = yi; i <= yf; i++) {
			for (int j = xi; j <= xf; j++) {
				// read consecutive
				if (black && board[i][j] == GameState.BLACK || !black && board[i][j] == GameState.WHITE) {
					consec++;
				}

				// read empty space
				else if (board[i][j] == GameState.EMPTY) {
					if (consec == 0) {
						numSpaces = 1;
						continue;
					}
					numSpaces++;
					score += analyzeShape(consec, numSpaces, black);

					consec = 0;
					numSpaces = 0;
				}

				// reach opposite color
				else if (!black && board[i][j] == GameState.BLACK || black && board[i][j] == GameState.WHITE) {
					if (consec == 0) {
						numSpaces = 0;
						continue;
					}

					score += analyzeShape(consec, numSpaces, black);

					consec = 0;
					numSpaces = 0;
				}
			}

			// reach end of board with series
			if (consec > 0) {
				score += analyzeShape(consec, numSpaces, black);

				consec = 0;
				numSpaces = 0;
			}
		}
		return score;
	}

	/**
	 * analyze all vertical shapes on the board
	 * 
	 * @param board
	 *            gameboard
	 * @param xi
	 *            initial x of cropped board
	 * @param xf
	 *            final x of cropped board
	 * @param yi
	 *            initial y of cropped board
	 * @param yf
	 *            final y of cropped board
	 * @param black
	 *            true if blacks turn, false otherwise
	 * @return
	 */
	private float analyzeBoardScoreVertical(int[][] board, int xi, int xf, int yi, int yf, boolean black) {
		float score = 0;

		int consec = 0;
		int numSpaces = 0;
		for (int j = yi; j < yf; j++) {
			for (int i = xi; i <= xf; i++) {
				// read consecutive
				if (black && board[i][j] == GameState.BLACK || !black && board[i][j] == GameState.WHITE)
					consec++;

				// read empty space
				else if (board[i][j] == GameState.EMPTY) {
					if (consec == 0) {
						numSpaces = 1;
						continue;
					}
					numSpaces++;
					score += analyzeShape(consec, numSpaces, black);

					consec = 0;
					numSpaces = 0;
				}

				// read opposite color
				else if (!black && board[i][j] == GameState.BLACK || black && board[i][j] == GameState.WHITE) {
					if (consec == 0) {
						numSpaces = 0;
						continue;
					}
					score += analyzeShape(consec, numSpaces, black);

					consec = 0;
					numSpaces = 0;
				}
			}
			// reach end of board with series
			if (consec > 0) {
				score += analyzeShape(consec, numSpaces, black);

				consec = 0;
				numSpaces = 0;
			}
		}
		return score;
	}

	/**
	 * analyze all diagonal (top left to bottom right) shapes on the board
	 * 
	 * @param board
	 *            gameboard
	 * @param xi
	 *            initial x of cropped board
	 * @param xf
	 *            final x of cropped board
	 * @param yi
	 *            initial y of cropped board
	 * @param yf
	 *            final y of cropped board
	 * @param black
	 *            true if blacks turn, false otherwise
	 * @return
	 */
	private float analyzeBoardScoreDiagonal1(int[][] board, int xi, int xf, int yi, int yf, boolean black) {
		float score = 0;

		int consec = 0;
		int numSpaces = 0;
		for (int k = 0; k < (yf - yi) + (xf - xi); k++) {
			for (int j = 0; j <= k; j++) {
				int i = k - j;
				if (i < yf - yi && j < xf - xi) {
					int x = xi + j;
					int y = yi + i;

					// read consec
					if (black && board[y][x] == GameState.BLACK || !black && board[y][x] == GameState.WHITE)
						consec++;

					// read empty
					else if (board[y][x] == GameState.EMPTY) {
						if (consec == 0) {
							numSpaces = 1;
							continue;
						}

						numSpaces++;
						score += analyzeShape(consec, numSpaces, black);

						consec = 0;
						numSpaces = 0;
					}

					// read opposite
					else if (!black && board[y][x] == GameState.BLACK || black && board[y][x] == GameState.WHITE) {
						if (consec == 0) {
							numSpaces = 0;
							continue;
						}

						score += analyzeShape(consec, numSpaces, black);

						consec = 0;
						numSpaces = 0;
					}
				}
			}
			// reach end of board with series
			if (consec > 0) {
				score += analyzeShape(consec, numSpaces, black);

				consec = 0;
				numSpaces = 0;
			}
		}

		return score;
	}

	/**
	 * analyze all diagonal (top right to bottom left) shapes on the board
	 * 
	 * @param board
	 *            gameboard
	 * @param xi
	 *            initial x of cropped board
	 * @param xf
	 *            final x of cropped board
	 * @param yi
	 *            initial y of cropped board
	 * @param yf
	 *            final y of cropped board
	 * @param black
	 *            true if blacks turn, false otherwise
	 * @return
	 */
	private float analyzeBoardScoreDiagonal2(int[][] board, int xi, int xf, int yi, int yf, boolean black) {
		float score = 0;

		int consec = 0;
		int numSpaces = 0;
		for (int k = 0; k < (yf - yi) + (xf - xi); k++) {
			for (int j = 0; j <= k; j++) {
				int i = k - j;
				if (i < yf - yi && j < xf - xi) {
					int x = xf - j;
					int y = yi + i;

					// read consec
					if (black && board[y][x] == GameState.BLACK || !black && board[y][x] == GameState.WHITE)
						consec++;

					// read empty
					else if (board[y][x] == GameState.EMPTY) {
						if (consec == 0) {
							numSpaces = 1;
							continue;
						}

						numSpaces++;
						score += analyzeShape(consec, numSpaces, black);

						consec = 0;
						numSpaces = 0;
					}

					// read opposite
					else if (!black && board[y][x] == GameState.BLACK || black && board[y][x] == GameState.WHITE) {
						if (consec == 0) {
							numSpaces = 0;
							continue;
						}

						score += analyzeShape(consec, numSpaces, black);

						consec = 0;
						numSpaces = 0;
					}
				}
			}
			// reach end of board with series
			if (consec > 0) {
				score += analyzeShape(consec, numSpaces, black);

				consec = 0;
				numSpaces = 0;
			}
		}

		return score;
	}

	/**
	 * adds the total score of the entire board using the above methods
	 * 
	 * @param board
	 *            gameboard
	 * @param xi
	 *            initial x of cropped board
	 * @param xf
	 *            final x of cropped board
	 * @param yi
	 *            initial y of cropped board
	 * @param yf
	 *            final y of cropped board
	 * @param black
	 *            true if blacks turn, false otherwise
	 * @return
	 */
	private float analyzeBoardScore(int[][] board, int xi, int xf, int yi, int yf, boolean black) {
		float score = analyzeBoardScoreHorizontal(board, xi, xf, yi, yf, black)
				+ analyzeBoardScoreVertical(board, xi, xf, yi, yf, black)
				+ analyzeBoardScoreDiagonal1(board, xi, xf, yi, yf, black)
				+ analyzeBoardScoreDiagonal2(board, xi, xf, yi, yf, black);
		return score;
	}

	/**
	 * performs a minimax algorithm on the current board state, looking numply moves
	 * into the future
	 * 
	 * @param board
	 *            gameboard
	 * @param xi
	 *            initial x of cropped board
	 * @param xf
	 *            final x of cropped board
	 * @param yi
	 *            initial y of cropped board
	 * @param yf
	 *            final y of cropped board
	 * @param black
	 *            true if blacks turn, false otherwise
	 * @return
	 */
	private int[] minimax(int[][] board, int xi, int xf, int yi, int yf, int depth, boolean black) {
		int bscore = black ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		int x = -1;
		int y = -1;

		if (depth == 0) {
			return new int[] { x, y, (int) (analyzeBoardScore(board, xi, xf, yi, yf, !black)
					- analyzeBoardScore(board, xi, xf, yi, yf, black)) };
		}
		for (int i = yi; i <= yf; i++) {
			for (int j = xi; j <= xf; j++) {
				if (board[i][j] != GameState.EMPTY) {
					continue;
				}

				board[i][j] = black ? GameState.BLACK : GameState.WHITE;

				int response[] = minimax(board, xi, xf, yi, yf, depth - 1, !black);
				float analysis = response[2];

				board[i][j] = GameState.EMPTY;

				if (black && analysis > bscore || !black && analysis < bscore) {
					x = j;
					y = i;
					bscore = (int) analysis;
				}
			}
		}
		return new int[] { x, y, bscore };
	}

	/**
	 * crops the board such that only the relevant pieces are contained. also
	 * includes local var offset num spaces in either direction.
	 * 
	 * @param board
	 *            gameboard
	 * @return variables corresponding with the cropped board
	 */
	private int[] splitBoard(int[][] board) {
		int xi = board.length - 1;
		int xf = 0;
		int yi = board.length - 1;
		int yf = 0;

		int offset = 2;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] != GameState.EMPTY) {
					if (i > yf)
						yf = i;
					if (i < yi)
						yi = i;

					if (j > xf)
						xf = j;
					if (j < xi)
						xi = j;
				}
			}
		}

		xi -= offset;
		xf += offset;
		yi -= offset;
		yf += offset;

		if (xi < 0)
			xi = 0;
		if (xf > board.length - 1)
			xf = board.length - 1;
		if (yi < 0)
			yi = 0;
		if (yf > board.length - 1)
			yf = board.length - 1;

		return new int[] { xi, xf, yi, yf };
	}

	/**
	 * uses minimax algorithm to decide the best move
	 * 
	 * @param board
	 *            gameboard
	 */
	public void makeMove(int[][] board) {
		int[] croppedBoard = splitBoard(board);
		int[] move = minimax(board, croppedBoard[0], croppedBoard[1], croppedBoard[2], croppedBoard[3], numplys, false);
		GameState.getInstance().placePiece(move[0], move[1]);
	}
}
