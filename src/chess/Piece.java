package chess;

import java.util.ArrayList;
import java.util.Arrays;

//moves[] for the make move method is as follows:
//moves{fromcolumn, fromrow, tocolumn, torow }
public abstract class Piece {
	boolean black = true;
	String name;
	boolean enpassant = false;
	boolean moved = false;

	// piece movement code under makemove method. return board after move is
	// validated and completed
	Board makemove(Board board, int[] moves) {
		// System.out.println("This is generic piece");
		return board;
	}

	public ArrayList<boardLoc> attackPoints(Board board, boardLoc loc) {
		return null;
	}

}

class Pawn extends Piece {
	boolean moved = false;
	boolean enpassant = false;
	{
		super.name = "p";
	}

	Board makemove(Board board, int[] moves) {
		// System.out.println("This is a pawn piece " + black);

		if (Math.abs(moves[0] - moves[2]) == 1) {
			// check if its trying to capture
			if (Math.abs(moves[1] - moves[3]) == 1) {
				// enpassant logic here
				if (board.pieces[moves[3]][moves[2]] == null) {
					if (moves[1] < moves[3]) {
						// up
						if (board.pieces[moves[3] - 1][moves[2]] instanceof Pawn) {
							Piece en = board.pieces[moves[3] - 1][moves[2]];
							if (en.enpassant && en.black != board.pieces[moves[1]][moves[0]].black) {
								// System.out.println("reach if");
								// do capture
								Piece m = board.pieces[moves[1]][moves[0]];
								// System.out.println(m);
								board.pieces[moves[3]][moves[2]] = m;
								board.pieces[moves[1]][moves[0]] = null;
								board.pieces[moves[3] - 1][moves[2]] = null;

								this.moved = true;
							}
						}
					} else if (moves[1] > moves[3]) {
						// down
						if (board.pieces[moves[3] + 1][moves[2]] instanceof Pawn) {
							Piece en = board.pieces[moves[3] + 1][moves[2]];
							if (en.enpassant && en.black != board.pieces[moves[1]][moves[0]].black) {
								// System.out.println("reach if");
								// do capture
								Piece m = board.pieces[moves[1]][moves[0]];
								// System.out.println(m);
								board.pieces[moves[3]][moves[2]] = m;
								board.pieces[moves[1]][moves[0]] = null;
								board.pieces[moves[3] + 1][moves[2]] = null;

								this.moved = true;
							}
						}
					} else {
						// System.out.println("Illegal move, try again");
					}

				} else if (Math.abs(moves[1] - moves[3]) == 1 && (board.pieces[moves[3]][moves[2]] instanceof Piece)
						&& board.pieces[moves[3]][moves[2]].black != this.black) {
					// regular capture
					Piece m = board.pieces[moves[1]][moves[0]];
					board.pieces[moves[3]][moves[2]] = m;
					board.pieces[moves[1]][moves[0]] = null;
					this.moved = true;
				}
			}
		} else if (moves[0] == moves[2]) {
			// same column, moving forward
			// prevent white and black pieces from moving backward
			if (moves[1] < moves[3] && this.black) {
				// System.out.println("Illegal move, try again");
				return board;
			} else if (moves[3] < moves[1] && !this.black) {
				// System.out.println("Illegal move, try again");
				return board;
			}

			if (Math.abs(moves[1] - moves[3]) == 2 && !this.moved
					&& !(board.pieces[moves[3]][moves[2]] instanceof Piece)
			/*
			 * && !(board.pieces[moves[3] - 1][moves[2]] instanceof Piece)
			 */) {
				// move two spaces
				Piece m = board.pieces[moves[1]][moves[0]];
				board.pieces[moves[3]][moves[2]] = m;
				board.pieces[moves[1]][moves[0]] = null;
				this.moved = true;
				m.enpassant = true;
			} else if (Math.abs(moves[1] - moves[3]) == 1) {
				if (!(board.pieces[moves[3]][moves[2]] instanceof Piece)) {
					// only goes in one direction

					Piece m = board.pieces[moves[1]][moves[0]];
					board.pieces[moves[3]][moves[2]] = m;
					board.pieces[moves[1]][moves[0]] = null;
					this.moved = true;

				}
			}
		}

		//// System.out.println("Illegal move, try again");
		return board;
	}

	public boolean canIAttackHere(Board board, int[] moves) {
		if (Math.abs(moves[0] - moves[2]) == 1) {
			// check if its trying to capture
			if (Math.abs(moves[1] - moves[3]) == 1) {
				// enpassant logic here
				if (board.pieces[moves[3]][moves[2]] == null) {
					if (moves[1] < moves[3]) {
						// up
						if (board.pieces[moves[3] - 1][moves[2]] instanceof Pawn) {
							Piece en = board.pieces[moves[3] - 1][moves[2]];
							if (en.enpassant && en.black != board.pieces[moves[1]][moves[0]].black) {
								return true;
							}
						}
					} else if (moves[1] > moves[3]) {
						// down
						if (board.pieces[moves[3] + 1][moves[2]] instanceof Pawn) {
							Piece en = board.pieces[moves[3] + 1][moves[2]];
							if (en.enpassant && en.black != board.pieces[moves[1]][moves[0]].black) {
								return true;
							}
						}
					}
				} else if (Math.abs(moves[1] - moves[3]) == 1 && (board.pieces[moves[3]][moves[2]] != null)
						&& board.pieces[moves[3]][moves[2]].black != this.black) {
					// regular capture
					return true;
				}
			}
		}
		return false;
	}

	public ArrayList<boardLoc> attackPoints(Board board, boardLoc loc) {
		ArrayList<boardLoc> points = new ArrayList<boardLoc>();
		Piece currpiece = board.pieces[loc.row][loc.col];
		// black
		if (currpiece.black && loc.row - 1 >= 0) {
			if (loc.col - 1 >= 0) {
				if (canIAttackHere(board, new int[] { loc.col, loc.row, loc.col - 1, loc.row - 1 })) {
					points.add(new boardLoc(loc.row - 1, loc.col - 1));
				}
			}
			if (loc.col + 1 <= 7) {
				if (canIAttackHere(board, new int[] { loc.col, loc.row, loc.col + 1, loc.row - 1 })) {
					points.add(new boardLoc(loc.row - 1, loc.col + 1));
				}
			}
		}
		// white
		else if (!currpiece.black && loc.row + 1 <= 7) {
			if (loc.col - 1 >= 0) {
				if (canIAttackHere(board, new int[] { loc.col, loc.row, loc.col - 1, loc.row + 1 })) {
					points.add(new boardLoc(loc.row + 1, loc.col - 1));
				}
			}
			if (loc.col + 1 <= 7) {
				if (canIAttackHere(board, new int[] { loc.col, loc.row, loc.col + 1, loc.row + 1 })) {
					points.add(new boardLoc(loc.row + 1, loc.col + 1));
				}
			}
		}

		return points;
	}

}

class Rook extends Piece {
	boolean moved = false;
	{
		super.name = "R";
	}

	Board makemove(Board board, int[] moves) {
		// at the moment, can 'capture' pieces if they are 1 tile away
		// System.out.println("This is a rook piece " + this.black);
		boolean valid = false;
		if (moves[0] != moves[2] && moves[1] != moves[3]) {
			// check here to make sure rook move is moving on same column or row
			// System.out.println("Illegal move, try again");
			return board;
		}
		int count = 0;
		// moving up or down
		if (moves[0] == moves[2]) {
			Piece[] check = new Piece[Math.abs(moves[3] - moves[1])];
			// System.out.println(check.length);
			// look at all the spaces between the rows
			if (moves[1] < moves[3]) {
				// rook is moving up
				for (int i = moves[3]; i > moves[1]; i--) {
					// System.out.println(board.pieces[i][moves[0]] + " " + count);
					check[count] = board.pieces[i][moves[0]];
					count++;
				}
			} else {
				// going down
				for (int i = moves[3]; i < moves[1]; i++) {
					check[count] = board.pieces[i][moves[0]];
					count++;
				}
			}

			// does not account for capturing pieces yet
			// System.out.println("b4 loop");
			for (int i = 0; i < check.length; i++) {
				// System.out.println("checkarr "+ check[i] + " " + i);
				if (check[i] instanceof Piece) {
					// System.out.println(count);
					if (i == 0 && check[i].black != this.black) {
						continue;
					} else {
						// System.out.println("Illegal move, try again");
						return board;
					}
				}
			}

			// if reached this point, valid move
			// check for capture
			valid = true;
		}

		else if (moves[1] == moves[3]) {
			// rows are the same
			Piece[] check = new Piece[Math.abs(moves[2] - moves[0])];
			// System.out.println(check.length);
			// look at all the spaces between the rows
			if (moves[0] < moves[2]) {
				// rook is moving left
				for (int i = moves[2]; i > moves[0]; i--) {
					check[count] = board.pieces[moves[1]][i];
					count++;
				}
			} else {
				// going right
				for (int i = moves[2]; i > moves[0]; i++) {
					check[count] = board.pieces[moves[1]][i];
					count++;
				}
			}

			// does not account for capturing pieces yet
			for (int i = 0; i < check.length; i++) {
				// System.out.print(check[i] + " ");
				if (check[i] instanceof Piece) {
					// System.out.println(count);
					if (i == 0 && check[i].black != this.black) {
						continue;
					} else {
						// System.out.println("Illegal move, try again");
						return board;
					}
				}
			}

			// if reached this point, valid move

			valid = true;
		}
		if (valid && (board.pieces[moves[3]][moves[2]] == null || (board.pieces[moves[3]][moves[2]] instanceof Piece
				&& board.pieces[moves[3]][moves[2]].black != this.black))) {
			Piece m = board.pieces[moves[1]][moves[0]];
			board.pieces[moves[3]][moves[2]] = m;
			board.pieces[moves[1]][moves[0]] = null;
			this.moved = true;
			return board;
		} else {
			// System.out.println("Illegal move, try again");
			return board;
		}
	}

	public boolean canIAttackHere(Board board, int[] moves) {
		boolean valid = false;
		if (moves[0] != moves[2] && moves[1] != moves[3]) {
			return false;
		}
		int count = 0;
		if (moves[0] == moves[2]) {
			Piece[] check = new Piece[Math.abs(moves[3] - moves[1])];
			if (moves[1] < moves[3]) {
				for (int i = moves[3]; i > moves[1]; i--) {
					check[count] = board.pieces[i][moves[0]];
					count++;
				}
			} else {
				for (int i = moves[3]; i < moves[1]; i++) {
					check[count] = board.pieces[i][moves[0]];
					count++;
				}
			}
			for (int i = 0; i < check.length; i++) {
				if (check[i] instanceof Piece) {
					if (i == 0 && check[i].black != this.black) {
						continue;
					} else {
						return false;
					}
				}
			}
			valid = true;
		} else if (moves[1] == moves[3]) {
			Piece[] check = new Piece[Math.abs(moves[2] - moves[0])];
			if (moves[0] < moves[2]) {
				for (int i = moves[2]; i > moves[0]; i--) {
					check[count] = board.pieces[moves[1]][i];
					count++;
				}
			} else {
				for (int i = moves[2]; i > moves[0]; i++) {
					check[count] = board.pieces[moves[1]][i];
					count++;
				}
			}
			for (int i = 0; i < check.length; i++) {
				if (check[i] instanceof Piece) {
					if (i == 0 && check[i].black != this.black) {
						continue;
					} else {
						return false;
					}
				}
			}
			valid = true;
		}
		if (valid && (board.pieces[moves[3]][moves[2]] == null || (board.pieces[moves[3]][moves[2]] instanceof Piece
				&& board.pieces[moves[3]][moves[2]].black != this.black))) {
			return true;
		} else {
			return false;
		}

	}

	public ArrayList<boardLoc> attackPoints(Board board, boardLoc loc) {
		ArrayList<boardLoc> points = new ArrayList<boardLoc>();
		Piece currpiece = board.pieces[loc.row][loc.col];

		for (int row = 0; row < 8; row++) {
			if (canIAttackHere(board, new int[] { loc.col, loc.row, loc.col, row })) {
				points.add(new boardLoc(row, loc.col));
			}
		}
		for (int col = 0; col < 8; col++) {
			if (canIAttackHere(board, new int[] { loc.col, loc.row, col, loc.row })) {
				points.add(new boardLoc(loc.row, col));
			}
		}

		return points;
	}
}

class Bishop extends Piece {
	{
		super.name = "B";
	}

	Board makemove(Board board, int[] moves) {
		// System.out.println("This is a bishop piece");
		// System.out.println(Arrays.toString(moves));
		if (Math.abs(moves[0] - moves[2]) != Math.abs(moves[1] - moves[3])) {
			// System.out.println("Illegal move, try again");
			return board;
		} else if (board.pieces[moves[3]][moves[2]] != null && board.pieces[moves[3]][moves[2]] instanceof Piece
				&& board.pieces[moves[1]][moves[0]].black == board.pieces[moves[3]][moves[2]].black) {
			return board;
		} else {
			// Piece[] check = new Piece[Math.abs(moves[3] - moves[1])];
			ArrayList<Piece> check = new ArrayList<Piece>();

			if (moves[0] < moves[2]) {
				if (moves[1] < moves[3]) {
					// moving top right
					for (int i = 1; (moves[1] + i < moves[3] && moves[0] + i < moves[2]); i++) {
						if (board.pieces[moves[1] + i][moves[0] + i] instanceof Piece) {
							return board;
						}
					}
				} else {
					// moving bottom right
					for (int i = 1; (moves[1] - i > moves[3] && moves[0] + i < moves[2]); i++) {
						if (board.pieces[moves[1] - i][moves[0] + i] instanceof Piece) {
							return board;
						}
					}
				}
			} else {
				if (moves[1] < moves[3]) {
					// moving top left
					for (int i = 1; (moves[1] + i < moves[3] && moves[0] - i > moves[2]); i++) {
						if (board.pieces[moves[1] + i][moves[0] - i] instanceof Piece) {
							return board;
						}
					}
				} else {
					// moving bottom left
					for (int i = 1; (moves[1] - i > moves[3] && moves[0] - i > moves[2]); i++) {
						if (board.pieces[moves[1] - i][moves[0] - i] instanceof Piece) {
							return board;
						}
					}
				}
			}
			Piece m = board.pieces[moves[1]][moves[0]];
			board.pieces[moves[3]][moves[2]] = m;
			board.pieces[moves[1]][moves[0]] = null;

			return board;
		}

	}

	public boolean canIAttackHere(Board board, int[] moves) {
		if (Math.abs(moves[0] - moves[2]) != Math.abs(moves[1] - moves[3])) {
			return false;
		} else if (board.pieces[moves[3]][moves[2]] != null && board.pieces[moves[3]][moves[2]] instanceof Piece
				&& board.pieces[moves[1]][moves[0]].black == board.pieces[moves[3]][moves[2]].black) {
			return false;
		} else {
			ArrayList<Piece> check = new ArrayList<Piece>();
			if (moves[0] < moves[2]) {
				if (moves[1] < moves[3]) {
					// moving top right
					for (int i = 1; (moves[1] + i < moves[3] && moves[0] + i < moves[2]); i++) {
						if (board.pieces[moves[1] + i][moves[0] + i] instanceof Piece) {
							return false;
						}
					}
				} else {
					// moving bottom right
					for (int i = 1; (moves[1] - i > moves[3] && moves[0] + i < moves[2]); i++) {
						if (board.pieces[moves[1] - i][moves[0] + i] instanceof Piece) {
							return false;
						}
					}
				}
			} else {
				if (moves[1] < moves[3]) {
					// moving top left
					for (int i = 1; (moves[1] + i < moves[3] && moves[0] - i > moves[2]); i++) {
						if (board.pieces[moves[1] + i][moves[0] - i] instanceof Piece) {
							return false;
						}
					}
				} else {
					// moving bottom left
					for (int i = 1; (moves[1] - i > moves[3] && moves[0] - i > moves[2]); i++) {
						if (board.pieces[moves[1] - i][moves[0] - i] instanceof Piece) {
							return false;
						}
					}
				}
			}
			return true;
		}

	}

	public ArrayList<boardLoc> attackPoints(Board board, boardLoc loc) {
		ArrayList<boardLoc> points = new ArrayList<boardLoc>();
		Piece currpiece = board.pieces[loc.row][loc.col];

		// Left Up
		int newRow = loc.row + 1;
		int newCol = loc.col - 1;
		while (newRow <= 7 && newCol >= 0) {
			if (canIAttackHere(board, new int[] { loc.col, loc.row, newCol, newRow })) {
				points.add(new boardLoc(newRow, newCol));
			} else {
				break;
			}
			newRow++;
			newCol--;
		}
		// Left Down
		newRow = loc.row - 1;
		newCol = loc.col - 1;
		while (newRow >= 0 && newCol >= 0) {
			if (canIAttackHere(board, new int[] { loc.col, loc.row, newCol, newRow })) {
				points.add(new boardLoc(newRow, newCol));
			} else {
				break;
			}
			newRow--;
			newCol--;
		}
		// Right Up
		newRow = loc.row + 1;
		newCol = loc.col + 1;
		while (newRow <= 7 && newCol <= 7) {
			if (canIAttackHere(board, new int[] { loc.col, loc.row, newCol, newRow })) {
				points.add(new boardLoc(newRow, newCol));
			} else {
				break;
			}
			newRow++;
			newCol++;
		}
		// Right Down
		newRow = loc.row - 1;
		newCol = loc.col + 1;
		while (newRow >= 0 && newCol <= 7) {
			if (canIAttackHere(board, new int[] { loc.col, loc.row, newCol, newRow })) {
				points.add(new boardLoc(newRow, newCol));
			} else {
				break;
			}
			newRow--;
			newCol++;
		}

		return points;
	}
}

class Knight extends Piece {
	{
		super.name = "N";
	}

	Board makemove(Board board, int[] moves) {
		// System.out.println("This is a knight piece");
		boolean valid = false;
		if (moves[0] < moves[2]) {

			if (moves[1] < moves[3]) {
				// moving upper right
				// System.out.println("upright");
				if ((moves[1] + 2 == moves[3] && moves[0] + 1 == moves[2])
						|| (moves[1] + 1 == moves[3] && moves[0] + 2 == moves[2])) {
					valid = true;
				}
			} else {
				// moving bottom right
				// System.out.println("botright");
				if ((moves[1] - 2 == moves[3] && moves[0] + 1 == moves[2])
						|| (moves[1] - 1 == moves[3] && moves[0] + 2 == moves[2])) {
					valid = true;
				}
			}
		} else {
			if (moves[1] < moves[3]) {
				// moving upper left
				// System.out.println("upleft");
				if ((moves[1] + 2 == moves[3] && moves[0] - 1 == moves[2])
						|| (moves[1] + 1 == moves[3] && moves[0] - 2 == moves[2])) {
					valid = true;
				}
			} else {
				// moving bottom left
				// System.out.println("botleft");
				if ((moves[1] - 1 == moves[3] && moves[0] - 2 == moves[2])
						|| (moves[1] - 2 == moves[3] && moves[0] - 1 == moves[2])) {
					valid = true;
				}
			}
		}

		if (valid && (board.pieces[moves[3]][moves[2]] == null || (board.pieces[moves[3]][moves[2]] instanceof Piece
				&& board.pieces[moves[3]][moves[2]].black != this.black))) {

			Piece m = board.pieces[moves[1]][moves[0]];
			board.pieces[moves[3]][moves[2]] = m;
			board.pieces[moves[1]][moves[0]] = null;
			return board;
		}
		// System.out.println("Illegal move, try again");
		return board;
	}

	public boolean canIAttackHere(Board board, int[] moves) {
		boolean valid = false;
		if (moves[0] < moves[2]) {

			if (moves[1] < moves[3]) {
				// moving upper right
				if ((moves[1] + 2 == moves[3] && moves[0] + 1 == moves[2])
						|| (moves[1] + 1 == moves[3] && moves[0] + 2 == moves[2])) {
					valid = true;
				}
			} else {
				// moving bottom right
				if ((moves[1] - 2 == moves[3] && moves[0] + 1 == moves[2])
						|| (moves[1] - 1 == moves[3] && moves[0] + 2 == moves[2])) {
					valid = true;
				}
			}
		} else {
			if (moves[1] < moves[3]) {
				// moving upper left
				if ((moves[1] + 2 == moves[3] && moves[0] - 1 == moves[2])
						|| (moves[1] + 1 == moves[3] && moves[0] - 2 == moves[2])) {
					valid = true;
				}
			} else {
				// moving bottom left
				if ((moves[1] - 1 == moves[3] && moves[0] - 2 == moves[2])
						|| (moves[1] - 2 == moves[3] && moves[0] - 1 == moves[2])) {
					valid = true;
				}
			}
		}

		if (valid && (board.pieces[moves[3]][moves[2]] == null || (board.pieces[moves[3]][moves[2]] instanceof Piece
				&& board.pieces[moves[3]][moves[2]].black != this.black))) {
			return true;
		}
		return false;
	}

	public ArrayList<boardLoc> attackPoints(Board board, boardLoc loc) {
		ArrayList<boardLoc> points = new ArrayList<boardLoc>();
		// up rights
		int newRow = loc.row + 2;
		int newCol = loc.col + 1;
		if (newRow <= 7 && newCol <= 7 && canIAttackHere(board, new int[] { loc.row, loc.col, newRow, newCol })) {
			points.add(new boardLoc(newRow, newCol));
		}
		newRow = loc.row + 1;
		newCol = loc.col + 2;
		if (newRow <= 7 && newCol <= 7 && canIAttackHere(board, new int[] { loc.row, loc.col, newRow, newCol })) {
			points.add(new boardLoc(newRow, newCol));
		}
		// down rights
		newRow = loc.row - 2;
		newCol = loc.col + 1;
		if (newRow >= 0 && newCol <= 7 && canIAttackHere(board, new int[] { loc.row, loc.col, newRow, newCol })) {
			points.add(new boardLoc(newRow, newCol));
		}
		newRow = loc.row - 1;
		newCol = loc.col + 2;
		if (newRow >= 0 && newCol <= 7 && canIAttackHere(board, new int[] { loc.row, loc.col, newRow, newCol })) {
			points.add(new boardLoc(newRow, newCol));
		}
		// up left
		newRow = loc.row + 2;
		newCol = loc.col - 1;
		if (newRow <= 7 && newCol >= 0 && canIAttackHere(board, new int[] { loc.row, loc.col, newRow, newCol })) {
			points.add(new boardLoc(newRow, newCol));
		}
		newRow = loc.row + 1;
		newCol = loc.col - 2;
		if (newRow <= 7 && newCol >= 0 && canIAttackHere(board, new int[] { loc.row, loc.col, newRow, newCol })) {
			points.add(new boardLoc(newRow, newCol));
		}
		// down left
		newRow = loc.row - 2;
		newCol = loc.col - 1;
		if (newRow >= 0 && newCol >= 0 && canIAttackHere(board, new int[] { loc.row, loc.col, newRow, newCol })) {
			points.add(new boardLoc(newRow, newCol));
		}
		newRow = loc.row - 1;
		newCol = loc.col - 2;
		if (newRow >= 0 && newCol >= 0 && canIAttackHere(board, new int[] { loc.row, loc.col, newRow, newCol })) {
			points.add(new boardLoc(newRow, newCol));
		}
		return points;
	}
}

class Queen extends Piece {
	{
		super.name = "Q";
	}

	Board makemove(Board board, int[] moves) {
		// System.out.println("This is a queen piece");
		// rook stuff
		if (moves[0] == moves[2] || moves[1] == moves[3]) {
			// moving in rook pattern
			int count = 0;
			// moving up or down
			if (moves[0] == moves[2]) {
				Piece[] check = new Piece[Math.abs(moves[3] - moves[1])];
				// System.out.println(check.length);
				// look at all the spaces between the rows
				if (moves[1] < moves[3]) {
					// rook is moving up
					for (int i = moves[3]; i > moves[1] + 1; i--) {
						check[count] = board.pieces[i][moves[0]];
						count++;
					}
				} else {
					for (int i = moves[1] - 1; i > moves[3]; i--) {
						check[count] = board.pieces[i][moves[0]];
						count++;
					}
				}

				// does not account for capturing pieces yet
				for (int i = 0; i < check.length - 1; i++) {
					if (check[i] instanceof Piece) {
						// System.out.println(count);
						// System.out.println("Illegal move, try again");
						return board;
					}
				}

				// if reached this point, valid move
				// check for capture
				Piece m = board.pieces[moves[1]][moves[0]];
				board.pieces[moves[3]][moves[2]] = m;
				board.pieces[moves[1]][moves[0]] = null;

				return board;
			} else if (moves[1] == moves[3]) {
				Piece[] check = new Piece[Math.abs(moves[2] - moves[0])];
				// System.out.println(check.length);
				// look at all the spaces between the rows
				if (moves[0] < moves[2]) {
					// rook is moving up
					for (int i = moves[2]; i > moves[0] + 1; i--) {
						check[count] = board.pieces[i][moves[1]];
						count++;
					}
				} else {
					for (int i = moves[0] - 1; i > moves[2]; i--) {
						check[count] = board.pieces[i][moves[1]];
						count++;
					}
				}

				// does not account for capturing pieces yet
				for (int i = 0; i < check.length - 1; i++) {
					// System.out.print(check[i] + " ");
					if (check[i] instanceof Piece) {
						// System.out.println(count);
						// System.out.println("Illegal move, try again");
						return board;
					}
				}

				// if reached this point, valid move
				// check for capture
				Piece m = board.pieces[moves[1]][moves[0]];
				board.pieces[moves[3]][moves[2]] = m;
				board.pieces[moves[1]][moves[0]] = null;

				return board;
			}
		}

		// bishop stuff
		else if (Math.abs(moves[0] - moves[2]) == Math.abs(moves[1] - moves[3])) {
			// System.out.println("This is a bishop piece");
			// System.out.println(Arrays.toString(moves));
			if (board.pieces[moves[3]][moves[2]] != null && board.pieces[moves[3]][moves[2]] instanceof Piece
					&& board.pieces[moves[1]][moves[0]].black == board.pieces[moves[3]][moves[2]].black) {
				return board;
			} else {
				// Piece[] check = new Piece[Math.abs(moves[3] - moves[1])];
				ArrayList<Piece> check = new ArrayList<Piece>();

				if (moves[0] < moves[2]) {
					if (moves[1] < moves[3]) {
						// moving top right
						for (int i = 1; (moves[1] + i < moves[3] && moves[0] + i < moves[2]); i++) {
							if (board.pieces[moves[1] + i][moves[0] + i] instanceof Piece) {
								return board;
							}
						}
					} else {
						// moving bottom right
						for (int i = 1; (moves[1] - i > moves[3] && moves[0] + i < moves[2]); i++) {
							if (board.pieces[moves[1] - i][moves[0] + i] instanceof Piece) {
								return board;
							}
						}
					}
				} else {
					if (moves[1] < moves[3]) {
						// moving top left
						for (int i = 1; (moves[1] + i < moves[3] && moves[0] - i > moves[2]); i++) {
							if (board.pieces[moves[1] + i][moves[0] - i] instanceof Piece) {
								return board;
							}
						}
					} else {
						// moving bottom left
						for (int i = 1; (moves[1] - i > moves[3] && moves[0] - i > moves[2]); i++) {
							if (board.pieces[moves[1] - i][moves[0] - i] instanceof Piece) {
								return board;
							}
						}
					}
				}
				Piece m = board.pieces[moves[1]][moves[0]];
				board.pieces[moves[3]][moves[2]] = m;
				board.pieces[moves[1]][moves[0]] = null;

				return board;
			}
		}

		// System.out.println("Illegal move, try again");
		return board;
	}

	public boolean canIAttackHere(Board board, int[] moves) {

		// System.out.println("This is a queen piece");
		// rook stuff
		if (moves[0] == moves[2] || moves[1] == moves[3]) {
			// moving in rook pattern
			int count = 0;
			// moving up or down
			if (moves[0] == moves[2]) {
				Piece[] check = new Piece[Math.abs(moves[3] - moves[1])];
				// System.out.println(check.length);
				// look at all the spaces between the rows
				if (moves[1] < moves[3]) {
					// rook is moving up
					for (int i = moves[3]; i > moves[1] + 1; i--) {
						check[count] = board.pieces[i][moves[0]];
						count++;
					}
				} else {
					for (int i = moves[1] - 1; i > moves[3]; i--) {
						check[count] = board.pieces[i][moves[0]];
						count++;
					}
				}

				// does not account for capturing pieces yet
				for (int i = 0; i < check.length - 1; i++) {
					if (check[i] instanceof Piece) {
						// System.out.println(count);
						// System.out.println("Illegal move, try again");
						return false;
					}
				}

				return true;
			} else if (moves[1] == moves[3]) {
				Piece[] check = new Piece[Math.abs(moves[2] - moves[0])];
				// System.out.println(check.length);
				// look at all the spaces between the rows
				if (moves[0] < moves[2]) {
					// rook is moving up
					for (int i = moves[2]; i > moves[0] + 1; i--) {
						check[count] = board.pieces[i][moves[1]];
						count++;
					}
				} else {
					for (int i = moves[0] - 1; i > moves[2]; i--) {
						check[count] = board.pieces[i][moves[1]];
						count++;
					}
				}

				// does not account for capturing pieces yet
				for (int i = 0; i < check.length - 1; i++) {
					// System.out.print(check[i] + " ");
					if (check[i] instanceof Piece) {
						// System.out.println(count);
						// System.out.println("Illegal move, try again");
						return false;
					}
				}

				// if reached this point, valid move
				// check for capture
				return true;
			}
		}

		// bishop stuff
		else if (Math.abs(moves[0] - moves[2]) == Math.abs(moves[1] - moves[3])) {
			// System.out.println("This is a bishop piece");
			// System.out.println(Arrays.toString(moves));
			if (board.pieces[moves[3]][moves[2]] != null && board.pieces[moves[3]][moves[2]] instanceof Piece
					&& board.pieces[moves[1]][moves[0]].black == board.pieces[moves[3]][moves[2]].black) {
				return false;
			} else {
				// Piece[] check = new Piece[Math.abs(moves[3] - moves[1])];
				ArrayList<Piece> check = new ArrayList<Piece>();

				if (moves[0] < moves[2]) {
					if (moves[1] < moves[3]) {
						// moving top right
						for (int i = 1; (moves[1] + i < moves[3] && moves[0] + i < moves[2]); i++) {
							if (board.pieces[moves[1] + i][moves[0] + i] instanceof Piece) {
								return false;
							}
						}
					} else {
						// moving bottom right
						for (int i = 1; (moves[1] - i > moves[3] && moves[0] + i < moves[2]); i++) {
							if (board.pieces[moves[1] - i][moves[0] + i] instanceof Piece) {
								return false;
							}
						}
					}
				} else {
					if (moves[1] < moves[3]) {
						// moving top left
						for (int i = 1; (moves[1] + i < moves[3] && moves[0] - i > moves[2]); i++) {
							if (board.pieces[moves[1] + i][moves[0] - i] instanceof Piece) {
								return false;
							}
						}
					} else {
						// moving bottom left
						for (int i = 1; (moves[1] - i > moves[3] && moves[0] - i > moves[2]); i++) {
							if (board.pieces[moves[1] - i][moves[0] - i] instanceof Piece) {
								return false;
							}
						}
					}
				}
				return true;
			}
		}
		// System.out.println("Illegal move, try again");
		return false;
	}

	public ArrayList<boardLoc> attackPoints(Board board, boardLoc loc) {
		ArrayList<boardLoc> points = new ArrayList<boardLoc>();
		// Bishop Stuff
		// Left Up
		int newRow = loc.row + 1;
		int newCol = loc.col - 1;
		while (newRow <= 7 && newCol >= 0) {
			if (canIAttackHere(board, new int[] { loc.col, loc.row, newCol, newRow })) {
				points.add(new boardLoc(newRow, newCol));
			} else {
				break;
			}
			newRow++;
			newCol--;
		}
		// Left Down
		newRow = loc.row - 1;
		newCol = loc.col - 1;
		while (newRow >= 0 && newCol >= 0) {
			if (canIAttackHere(board, new int[] { loc.col, loc.row, newCol, newRow })) {
				points.add(new boardLoc(newRow, newCol));
			} else {
				break;
			}
			newRow--;
			newCol--;
		}
		// Right Up
		newRow = loc.row + 1;
		newCol = loc.col + 1;
		while (newRow <= 7 && newCol <= 7) {
			if (canIAttackHere(board, new int[] { loc.col, loc.row, newCol, newRow })) {
				points.add(new boardLoc(newRow, newCol));
			} else {
				break;
			}
			newRow++;
			newCol++;
		}
		// Right Down
		newRow = loc.row - 1;
		newCol = loc.col + 1;
		while (newRow >= 0 && newCol <= 7) {
			if (canIAttackHere(board, new int[] { loc.col, loc.row, newCol, newRow })) {
				points.add(new boardLoc(newRow, newCol));
			} else {
				break;
			}
			newRow--;
			newCol++;
		}

		// Rook Stuff
		for (int row = 0; row < 8; row++) {
			if (canIAttackHere(board, new int[] { loc.col, loc.row, loc.col, row })) {
				points.add(new boardLoc(row, loc.col));
			}
		}
		for (int col = 0; col < 8; col++) {
			if (canIAttackHere(board, new int[] { loc.col, loc.row, col, loc.row })) {
				points.add(new boardLoc(loc.row, col));
			}
		}

		return points;
	}
}

class King extends Piece {
	boolean moved = false;
	{
		super.name = "K";
	}

	Board makemove(Board board, int[] moves) {
		// System.out.println("This is a king piece");
		boolean valid = false;
		int[] kingoffset = { (moves[0] - moves[2]), (moves[1] - moves[3]) };
		int[][] offsets = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 1 }, { -1, 1 }, { -1, -1 }, { 1, -1 } };

		// first check if its trying to castle
		if (Math.abs(moves[0] - moves[2]) == 2) {
			// trying to move two spaces either side
			if (this.moved) {
				// System.out.println("Illegal move, try again");
				return board;
			} else if (moves[0] < moves[2]) {
				// moving right
				if (board.pieces[moves[1]][moves[0] + 3] instanceof Rook && !board.pieces[moves[1]][moves[0] + 3].moved
						&& board.pieces[moves[1]][moves[0] + 1] == null
						&& board.pieces[moves[1]][moves[0] + 2] == null) {
					// check if rook in initial position and hasnt moved and space in between is
					// empty
					Piece m = board.pieces[moves[1]][moves[0]];
					Piece r = board.pieces[moves[1]][moves[0] + 3];
					board.pieces[moves[3]][moves[2]] = m;
					board.pieces[moves[3]][moves[2] - 1] = r;
					board.pieces[moves[1]][moves[0]] = null;
					board.pieces[moves[1]][moves[0] + 3] = null;
					this.moved = true;
					r.moved = true;
					return board;
				}
			} else if (moves[0] > moves[2]) {
				// going left
				if (board.pieces[moves[1]][moves[0] - 4] instanceof Rook && !board.pieces[moves[1]][moves[0] - 4].moved
						&& board.pieces[moves[1]][moves[0] - 1] == null && board.pieces[moves[1]][moves[0] - 2] == null
						&& board.pieces[moves[1]][moves[0] - 3] == null) {
					// check if rook in initial position and hasnt moved and space in between is
					// empty
					Piece m = board.pieces[moves[1]][moves[0]];
					Piece r = board.pieces[moves[1]][moves[0] - 4];
					board.pieces[moves[3]][moves[2]] = m;
					board.pieces[moves[3]][moves[2] + 1] = r;
					board.pieces[moves[1]][moves[0]] = null;
					board.pieces[moves[1]][moves[0] - 4] = null;
					this.moved = true;
					r.moved = true;
					return board;
				}
			}

		}

		else {
			// check for if king did a regular move
			for (int i = 0; i < offsets.length; i++) {
				// System.out.println(offsets[i][0] +" " + offsets[i][1] + ": " +
				// kingoffset[0]+" " +kingoffset[1]);
				if (Arrays.equals(offsets[i], kingoffset))
					valid = true;
			}
		}

		if (valid && (board.pieces[moves[3]][moves[2]] == null || (board.pieces[moves[3]][moves[2]] instanceof Piece
				&& board.pieces[moves[3]][moves[2]].black != this.black))) {
			Piece m = board.pieces[moves[1]][moves[0]];
			board.pieces[moves[3]][moves[2]] = m;
			board.pieces[moves[1]][moves[0]] = null;
			this.moved = true;
			return board;
		}
		// System.out.println("Illegal move, try again");
		return board;
	}

	public boolean canIAttackHere(Board board, int[] moves) {
		// System.out.println("This is a king piece");
		boolean valid = false;
		int[] kingoffset = { (moves[0] - moves[2]), (moves[1] - moves[3]) };
		int[][] offsets = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 1 }, { -1, 1 }, { -1, -1 }, { 1, -1 } };
		for (int i = 0; i < offsets.length; i++) {
			if (Arrays.equals(offsets[i], kingoffset))
				valid = true;
		}
		if (valid && (board.pieces[moves[3]][moves[2]] == null || (board.pieces[moves[3]][moves[2]] instanceof Piece
				&& board.pieces[moves[3]][moves[2]].black != this.black))) {
			return true;
		}
		return false;
	}

	public ArrayList<boardLoc> attackPoints(Board board, boardLoc loc) {
		ArrayList<boardLoc> points = new ArrayList<boardLoc>();
		int[][] offsets = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 1 }, { -1, 1 }, { -1, -1 }, { 1, -1 } };
		int newRow = 0;
		int newCol = 0;
		
		for (int[] off : offsets) {
			newRow = off[0] + loc.row;
			newCol = off[1] + loc.col;
			if(newRow <=7 && newRow >=0 && newCol <=7 && newCol >=0) {
				if(canIAttackHere(board, new int[] {loc.col, loc.row, newCol, newRow})) {
					points.add(new boardLoc(newRow, newCol));
				}
			}
		}
		return points;
	}
}