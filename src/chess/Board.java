package chess;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
	Piece[][] pieces;

	public Board() {
		pieces = new Piece[8][8];

		// Board setup
		Piece[] white = new Piece[] { new Pawn(), new Pawn(), new Pawn(), new Pawn(), new Pawn(), new Pawn(),
				new Pawn(), new Pawn(), new Rook(), new Knight(), new Bishop(), new Queen(), new King(), new Bishop(),
				new Knight(), new Rook(), };
		Piece[] black = new Piece[] { new Pawn(), new Pawn(), new Pawn(), new Pawn(), new Pawn(), new Pawn(),
				new Pawn(), new Pawn(), new Rook(), new Knight(), new Bishop(), new Queen(), new King(), new Bishop(),
				new Knight(), new Rook(), };

		for (int i = 0; i < white.length; i++) {
			white[i].black = false;
		}

		pieces[7] = Arrays.copyOfRange(black, 8, 16);
		pieces[6] = Arrays.copyOfRange(black, 0, 8);
		pieces[0] = Arrays.copyOfRange(white, 8, 16);
		pieces[1] = Arrays.copyOfRange(white, 0, 8);
	}

	public void drawBoard() {
		drawBoard(this.pieces);
	}

	public static void drawBoard(Piece[][] board) {
		// return board as string
		for (int i = 7; i >= 0; i--) {
			System.out.println(row(board[i], i));
		}
		System.out.println(" a  b  c  d  e  f  g  h");
	}

	// print each row of the board
	public static String row(Piece[] row, int rowNumber) {
		String r = "";
		for (int i = 0; i < row.length; i++) {
			if (row[i] instanceof Piece) {

				if (row[i].black) {
					r += "b" + row[i].name + " ";
				} else
					r += "w" + row[i].name + " ";
			} else {
				if (rowNumber % 2 == 0) {
					if (i % 2 == 0) {
						r += "   ";
					} else
						r += "## ";
				} else {
					if (i % 2 == 1) {
						r += "   ";
					} else
						r += "## ";
				}
			}
		}

		return r + "" + (rowNumber + 1);
	}

	//
	public static boolean isItCheck(Board board, boolean isBlack) {
		boolean underAttack = false;
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Piece currpiece = board.pieces[row][col];
				if (currpiece != null && currpiece instanceof King && currpiece.black == isBlack) {
					// FOUND KING, CHECK IF UNDER ATTACK
					boardLoc KingLoc = new boardLoc(row, col);
					// System.out.println("Found king at " + KingLoc);
					////
					for (int row2 = 0; row2 < 8; row2++) {
						for (int col2 = 0; col2 < 8; col2++) {
							// System.out.println(row2 + " " + col2);
							Piece attacker = board.pieces[row2][col2];
							if (attacker != null && attacker instanceof Piece && attacker.black != isBlack) {
								// System.out.println("ok");
								boardLoc aLoc = new boardLoc(row2, col2);
								ArrayList<boardLoc> attcks = attacker.attackPoints(board, aLoc);
								// System.out.print(attcks.toString());
								if (attcks != null && attcks.contains(KingLoc)) {
									underAttack = true;
									// System.out.println("UNDER ATTACK");
									return true;
								}

							}
						}
					}
					////
				}
			}
		}
		return false;
	}

	public static boolean isItCheckMate(Board board, boolean isBlack) {
		// Move every piece and see if it allows it to exit check if not check mate
		Board tempBoard = new Board();
		tempBoard.pieces = Chess.copy(board.pieces);
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Piece currpiece = board.pieces[row][col];
				if (currpiece != null && currpiece instanceof Piece && currpiece.black != isBlack) {
					int[][] offsets = {};
					int newRow = 0;
					int newCol = 0;
					// ========== King ==========
					if (currpiece instanceof King) {
						offsets = new int[][] { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 1 }, { -1, 1 },
								{ -1, -1 }, { 1, -1 } };
					}
					// ========== Pawn ==========
					if (currpiece instanceof Pawn) {
						int[][] blackPawnOffs = { { -1, 0 }, { -2, 0 }, { -1, -1 }, { -1, 1 } };
						int[][] whitePawnOffs = { { 1, 0 }, { 2, 0 }, { 1, -1 }, { 1, 1 } };
						offsets = blackPawnOffs;
						if (isBlack)
							offsets = whitePawnOffs;
					}
					// ========== Knight ==========
					if (currpiece instanceof Knight) {
						offsets = new int[][] { { 1, 2 }, { 2, 1 }, { -1, 2 }, { -2, 1 }, { 1, -2 }, { 2, -1 },
								{ -1, -2 }, { -2, -1 } };
					}
					// ========== Rook or Queen Hor&Ver ==========
					if (currpiece instanceof Rook || currpiece instanceof Queen) {

					}
					// ========== Bishop or Queen Diag ==========
					if (currpiece instanceof Bishop || currpiece instanceof Queen) {

					}
					// ========== Check Moves ========== //
					for (int[] off : offsets) {
						newRow = off[0] + row;
						newCol = off[1] + col;
						if (newRow <= 7 && newRow >= 0 && newCol <= 7 && newCol >= 0) {
							int[] moves = new int[] { col, row, newCol, newRow };
							tempBoard = tempBoard.pieces[moves[1]][moves[0]].makemove(tempBoard, moves);
							if (!Arrays.deepEquals(tempBoard.pieces, board.pieces) && !isItCheck(tempBoard, !isBlack)) {
								return false;
							} else {
								tempBoard.pieces = Chess.copy(board.pieces);
							}
						}
					}
				}
			}
		}
		return true;
	}
}
