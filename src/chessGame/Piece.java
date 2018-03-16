package chessGame;

import java.util.Arrays;

//moves[] for the make move method is as follows:
//moves{fromcolumn, fromrow, tocolumn, torow }
public abstract class Piece {
	boolean black = true;
	String name;
	boolean captured = false;
	//piece movement code under makemove method. return board after move is validated and completed
	Piece[][] makemove(Piece [][] board, int[] moves){
		System.out.println("This is generic piece");
		return board;
	}
	
}

class Pawn extends Piece {
	boolean moved = false;
	boolean doublestep = false;
	boolean enpassant = false;
	{
		super.name = "p";
	}
	
	Piece[][] makemove(Piece [][] board, int[] moves){
		System.out.println("This is a pawn piece " + black);
		
		
		
		return board;
	}

}

class Rook extends Piece {
	boolean moved = false;
	{
		super.name = "R";
	}
	Piece[][] makemove(Piece [][] board, int[] moves){
//		at the moment, can 'capture' pieces if they are 1 tile away
		System.out.println("This is a rook piece " + this.black);
		if(moves[0] != moves[2] && moves[1] != moves[3]) {
			//check here to make sure rook move is moving on same column or row		
			System.out.println("illegal move for rook");
			return board;
		}
		int count = 0;
//		moving up or down
		if(moves[0] == moves[2]) {
			Piece[] check = new Piece[Math.abs(moves[3]-moves[1])];
			System.out.println(check.length);
//			look at all the spaces between the rows
			if(moves[1] < moves[3]) {
//				rook is moving up
				for(int i = moves[3]; i > moves[1]+1; i--) {
					check[count] = board[i][moves[0]];
					count++;
				}
			}
			else {
				for(int i = moves[1]-1; i > moves[3]; i--) {
					check[count] = board[i][moves[0]];
					count++;
				}
			}
			
			//does not account for capturing pieces yet
			for(int i = 0; i < check.length - 1; i++) {
				if(check[i] instanceof Piece) {
//					System.out.println(count);
					System.out.println("illegal move. Piece is in the way.");
					return board;
				}
			}
			
//			if reached this point, valid move
//			check for capture
			Piece m = board[moves[1]][moves[0]];
			board[moves[3]][moves[2]] = m;
			board[moves[1]][moves[0]] = null;
			
			return board;
		}
		else if(moves[1] == moves[3]) {
			Piece[] check = new Piece[Math.abs(moves[2]-moves[0])];
			System.out.println(check.length);
//			look at all the spaces between the rows
			if(moves[0] < moves[2]) {
//				rook is moving up
				for(int i = moves[2]; i > moves[0]+1; i--) {
					check[count] = board[i][moves[1]];
					count++;
				}
			}
			else {
				for(int i = moves[0]-1; i > moves[2]; i--) {
					check[count] = board[i][moves[1]];
					count++;
				}
			}
			
			//does not account for capturing pieces yet
			for(int i = 0; i < check.length - 1; i++) {
//				System.out.print(check[i] + " ");
				if(check[i] instanceof Piece) {
//					System.out.println(count);
					System.out.println("illegal move. Piece is in the way.");
					return board;
				}
			}
			
//			if reached this point, valid move
//			check for capture
			Piece m = board[moves[1]][moves[0]];
			board[moves[3]][moves[2]] = m;
			board[moves[1]][moves[0]] = null;
			
			return board;
		}
		
		return board;
	}

}

class Bishop extends Piece {
	{
		super.name = "B";
	}

	Piece[][] makemove(Piece [][] board, int[] moves){
		System.out.println("This is a bishop piece");
//		System.out.println(Arrays.toString(moves));
		if(Math.abs(moves[0] - moves[2]) != Math.abs(moves[1] - moves[3])) {
			System.out.println("invalid bishop moves");
			return board;
		}
		else {
			Piece[] check = new Piece[Math.abs(moves[3]-moves[1])];
			int count = 0;
			if(Math.abs(moves[2]-moves[0]) == 1) {
//				System.out.println("1 col apart");
				if(board[moves[3]][moves[2]] instanceof Piece && this.black == board[moves[3]][moves[2]].black) {
					System.out.println("invalid move. Piece in the way");
					return board;
				}
				
			}
			
			if(moves[0] < moves[2]) {
				if(moves[1] < moves[3]) {
	//				moving top right
					System.out.println("top right");
					for(int i = 1; i < check.length; i++) {
						check[count] = board[moves[1] + i][moves[0] + i];
						count++;
					}
				}
				else {
	//				moving bottom right
					System.out.println("bot right");
					for(int i = 1; i < check.length - 1; i++) {
						check[count] = board[moves[1] - i][moves[0] + i];
						count++;
					}
				}
			}
			else {
				if(moves[1] < moves[3]) {
	//				moving top left
					System.out.println("top left");
					for(int i = 1; i < check.length; i++) {
						check[count] = board[moves[1] + i][moves[0] - i];
						count++;
					}
				}
				else {
	//				moving bottom left
					System.out.println("bot left");
					for(int i = 1; i < check.length; i++) {
						check[count] = board[moves[1] - i][moves[0] - i];
						count++;
					}
				}
			}
//			System.out.println(Arrays.toString(check));
			for(int i = 0; i < check.length; i++) {
				if(check[i] instanceof Piece) {
	//				System.out.println(count);
					System.out.println("illegal move. Piece is in the way.");
					return board;
				}
			}
			Piece m = board[moves[1]][moves[0]];
			board[moves[3]][moves[2]] = m;
			board[moves[1]][moves[0]] = null;
			
			return board;
		}

	}

}

class Knight extends Piece {
	{
		super.name = "N";
	}
	Piece[][] makemove(Piece [][] board, int[] moves){
		System.out.println("This is a knight piece");
		return board;
	}

}

class Queen extends Piece {
	{
		super.name = "Q";
	}
	Piece[][] makemove(Piece [][] board, int[] moves){
		System.out.println("This is a queen piece");
		return board;
	}

}

class King extends Piece {
	boolean moved = false;
	{
		super.name = "K";
	}
	Piece[][] makemove(Piece [][] board, int[] moves){
		System.out.println("This is a king piece");
	    int[][] offsets = {
	            {1, 0},
	            {0, 1},
	            {-1, 0},
	            {0, -1},
	            {1, 1},
	            {-1, 1},
	            {-1, -1},
	            {1, -1}
	        };
		return board;
	}

}