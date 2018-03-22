package chess;

import java.util.Arrays;
import java.util.Scanner;

public class Chess {

	public static void main(String[] args) {
		// set up board and player pieces
		boolean gameOn = true;
		boolean blackTurn = false;
		// Piece[][] board = new Piece[8][8];
		Board board = new Board();
		String[] names = { "Q", "R", "N", "B" };

		// temp deleting pieces for movement testing
		//board.pieces[3][4] = board.pieces[0][4];
		//board.pieces[0][4] = null;
		board.pieces[0][5] = null;
		board.pieces[0][6] = null;
		board.pieces[6][0] = null;
		board.pieces[4][1] = board.pieces[7][2];
		board.pieces[7][2] = null;
		
		board.pieces[4][4] = board.pieces[7][3]; 
		board.pieces[7][3] = null;
		// board.pieces[6][0] = board.pieces[1][1];
		// board.pieces[1][1] = null;

		board.drawBoard();
		// printBoard(board);
		String whiteMove = "", blackMove = "", mov = "";
		// System.out.println("New Game. White moves first."); Assignment want specific
		// outputs or penalty
		Scanner sc = new Scanner(System.in);

		boolean drawAsked = false;
		while (gameOn) {
			// fromTo will hold input as an array split into from and to move
			String[] fromTo;

			boolean currTurn = blackTurn;

			if (blackTurn) {
				System.out.print("\nBlack's move: ");
				blackMove = sc.nextLine();
				System.out.println();
				mov = blackMove;
				fromTo = blackMove.split(" ");
			} else {
				System.out.print("\nWhite's move: ");
				whiteMove = sc.nextLine();
				System.out.println();
				mov = whiteMove;
				fromTo = whiteMove.split(" ");
			}
			// end game if forfeit
			if (whiteMove.equals("resign")) {
				gameOn = false;
				System.out.println("Black wins");
				break;
			} else if (blackMove.equals("resign")) {
				gameOn = false;
				System.out.println("White wins");
				break;
			}

			// Handling Draw
			if (drawAsked && (whiteMove.equals("draw") || blackMove.equals("draw"))) {
				System.out.println("draw");
				break;
			} else {
				drawAsked = false;
			}

			if (fromTo.length > 2 && fromTo[fromTo.length - 1].equals("draw?")) {
				drawAsked = true;
				String[] tmp = fromTo.clone();
				fromTo = new String[tmp.length - 1];
				for (int i = 0; i < tmp.length - 1; i++) {
					fromTo[i] = tmp[i];
				}
			}

			// System.out.println(fromTo[0] + " " + inputToInt(fromTo)[0] + " " +
			// inputToInt(fromTo)[1] + " " + inputToInt(fromTo)[2] + " "+
			// inputToInt(fromTo)[3]);
			int[] input = inputToInt(fromTo);

			if (fromTo.length == 2 && board.pieces[input[1]][input[0]] instanceof Pawn
					&& ((board.pieces[input[1]][input[0]].black && input[3] == 0)
							|| (!board.pieces[input[1]][input[0]].black && input[3] == 7))) {
				blackTurn = !blackTurn;
				boolean color = board.pieces[input[1]][input[0]].black;
				Piece[][] temp = copy(board.pieces);
				board.pieces = board.pieces[input[1]][input[0]].makemove(board, input).pieces;

				// ====== CHECK
				boolean blackInCheck = Board.isItCheck(board, true);
				boolean whiteInCheck = Board.isItCheck(board, false);
				if ((blackTurn && blackInCheck) || (!blackTurn && whiteInCheck)) {
					System.out.println("Check\n");
				}
				if ((!blackTurn && blackInCheck) || (blackTurn && whiteInCheck)) {
					// goes again until not check. Revert to old board
					board.pieces = temp;
				}
				// ===============

				if (Arrays.deepEquals(board.pieces, temp)) {
					blackTurn = !blackTurn;
				} else {
					Queen q = new Queen();
					q.black = color;
					board.pieces[input[3]][input[2]] = q;
				}
			}

			if (fromTo.length == 3) {
				// check for pawn promotion command
				if (Arrays.asList(names).contains(fromTo[2]) && board.pieces[input[1]][input[0]] instanceof Pawn) {
					// check if pawn piece is reaching 8th rank
					if ((board.pieces[input[1]][input[0]].black && input[3] == 0)
							|| (!board.pieces[input[1]][input[0]].black && input[3] == 7)) {
						blackTurn = !blackTurn;
						boolean color = board.pieces[input[1]][input[0]].black;
						Piece[][] temp = copy(board.pieces);
						board = board.pieces[input[1]][input[0]].makemove(board, input);

						// ====== CHECK
						boolean blackInCheck = Board.isItCheck(board, true);
						boolean whiteInCheck = Board.isItCheck(board, false);
						if ((blackTurn && blackInCheck) || (!blackTurn && whiteInCheck)) {
							System.out.println("Check\n");
						}
						if ((!blackTurn && blackInCheck) || (blackTurn && whiteInCheck)) {
							// goes again until not check. Revert to old board
							board.pieces = temp;
						}
						// ===============

						if (Arrays.deepEquals(board.pieces, temp)) {

							blackTurn = !blackTurn;
						} else {
							// promote here
							switch (fromTo[2]) {
							case "Q":
								Queen q = new Queen();
								q.black = color;
								board.pieces[input[3]][input[2]] = q;
								break;
							case "R":
								Rook r = new Rook();
								r.black = color;
								board.pieces[input[3]][input[2]] = r;
								break;
							case "N":
								Knight n = new Knight();
								n.black = color;
								board.pieces[input[3]][input[2]] = n;
								break;
							case "B":
								Bishop b = new Bishop();
								b.black = color;
								board.pieces[input[3]][input[2]] = b;
								break;
							}
						}
					}
				}
			}

			else if (board.pieces[input[1]][input[0]] instanceof Piece
					&& board.pieces[input[1]][input[0]].black == blackTurn) {

				blackTurn = !blackTurn;

				Piece[][] temp = copy(board.pieces);
				board = board.pieces[input[1]][input[0]].makemove(board, input);

				// ====== CHECK

				boolean blackInCheck = Board.isItCheck(board, true);
				boolean whiteInCheck = Board.isItCheck(board, false);
				if ((blackTurn && blackInCheck) || (!blackTurn && whiteInCheck)) {
					System.out.println("Check\n");
				}
				if ((!blackTurn && blackInCheck) || (blackTurn && whiteInCheck)) {
					// goes again until not check. Revert to old board
					board.pieces = temp;
				}
				// ===============

				if (Arrays.deepEquals(board.pieces, temp)) {

					blackTurn = !blackTurn;
				}

			} else {
				// System.out.println("instanceof nuttin ");
				// blackTurn = !blackTurn;
			}
			// System.out.println("black turn after moves: " + blackTurn);

			// take board and false all en passants on opposite turn
			board.pieces = falseEnpassant(board.pieces, blackTurn);

			if (currTurn != blackTurn)
				board.drawBoard();
			else {
				System.out.println("Illegal move, try again");
			}
		}

	}

	// false all enpassant
	public static Piece[][] falseEnpassant(Piece[][] board, boolean turn) {
		for (int y = 7; y >= 0; y--)
			for (int x = 7; x >= 0; x--)
				if (board[x][y] != null && board[x][y].black == turn) {
					board[x][y].enpassant = false;
				}
		return board;
	}

	// copy board onto temp
	public static Piece[][] copy(Piece[][] board) {
		Piece[][] copy = new Piece[8][8];
		for (int y = 7; y >= 0; y--)
			for (int x = 7; x >= 0; x--)
				copy[x][y] = board[x][y];

		return copy;
	}

	// convert input move to int array
	public static int[] inputToInt(String[] splitentry) {
		int fromx = splitentry[0].charAt(0) - 'a';
		int fromy = splitentry[0].charAt(1) - 49;
		int tox = splitentry[1].charAt(0) - 'a';
		int toy = splitentry[1].charAt(1) - 49;

		return new int[] { fromx, fromy, tox, toy };
	}

	public static void printBoard(Piece[][] board) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				System.out.print(board[i][j] + " ");
			}
		}
	}
}
