package chess;

import java.util.Arrays;

//moves[] for the make move method is as follows:
//moves{fromcolumn, fromrow, tocolumn, torow }
public abstract class Piece {
	boolean black = true;
	String name;
	boolean enpassant = false;
	boolean moved = false;
	//piece movement code under makemove method. return board after move is validated and completed
	Piece[][] makemove(Piece [][] board, int[] moves){
//		System.out.println("This is generic piece");
		return board;
	}
	
}

class Pawn extends Piece {
	boolean moved = false;
	boolean enpassant = false;
	{
		super.name = "p";
	}
	
	Piece[][] makemove(Piece [][] board, int[] moves){
//		System.out.println("This is a pawn piece " + black);
		
		if(Math.abs(moves[0] - moves[2]) == 1) {
//			check if its trying to capture
			if(Math.abs(moves[1] - moves[3]) == 1) {
//				enpassant logic here
				if(board[moves[3]][moves[2]] == null ) {
					if(moves[1] < moves[3]) {
//						up
						if(board[moves[3]-1][moves[2]] instanceof Pawn) {
							Piece en = board[moves[3]-1][moves[2]];
							if(en.enpassant && en.black !=  board[moves[1]][moves[0]].black) {
//								System.out.println("reach if");
//								do capture 
								Piece m = board[moves[1]][moves[0]];
//								System.out.println(m);
								board[moves[3]][moves[2]] = m;
								board[moves[1]][moves[0]] = null;
								board[moves[3]-1][moves[2]] = null;

								this.moved = true;
							}
						}
					}
					else if(moves[1] > moves[3]){
//						down
						if(board[moves[3]+1][moves[2]] instanceof Pawn) {
							Piece en = board[moves[3]+1][moves[2]];
							if(en.enpassant && en.black !=  board[moves[1]][moves[0]].black) {
//								System.out.println("reach if");
//								do capture 
								Piece m = board[moves[1]][moves[0]];
//								System.out.println(m);
								board[moves[3]][moves[2]] = m;
								board[moves[1]][moves[0]] = null;
								board[moves[3]+1][moves[2]] = null;

								this.moved = true;
							}
						}
					}
					else {
						System.out.println("Illegal move, try again");
					}
					
				}
				else if(Math.abs(moves[1] - moves[3]) == 1 && (board[moves[3]][moves[2]] instanceof Piece) && board[moves[3]][moves[2]].black != this.black) {
//					regular capture
					Piece m = board[moves[1]][moves[0]];
					board[moves[3]][moves[2]] = m;
					board[moves[1]][moves[0]] = null;
					this.moved = true;
				}
			}
		}
		else if(moves[0] == moves[2]) {
//			same column, moving forward
//			prevent white and black pieces from moving backward
			if(moves[1] < moves[3] && this.black) {
				System.out.println("Illegal move, try again");
				return board;
			}
			else if(moves[3] < moves[1] && !this.black) {
				System.out.println("Illegal move, try again");
				return board;
			}
			
			if(Math.abs(moves[1] - moves[3]) == 2 && !this.moved && !(board[moves[3]][moves[2]] instanceof Piece) /*&& !(board[moves[3] - 1][moves[2]] instanceof Piece)*/) {
//				move two spaces
				Piece m = board[moves[1]][moves[0]];
				board[moves[3]][moves[2]] = m;
				board[moves[1]][moves[0]] = null;
				this.moved = true;
				m.enpassant = true; 
			}
			else if(Math.abs(moves[1] - moves[3]) == 1) {
				if(!(board[moves[3]][moves[2]] instanceof Piece)) {
//					only goes in one direction

					Piece m = board[moves[1]][moves[0]];
					board[moves[3]][moves[2]] = m;
					board[moves[1]][moves[0]] = null;
					this.moved = true;
					
				}
			}
		}
		
//		System.out.println("Illegal move, try again");
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
//		System.out.println("This is a rook piece " + this.black);
		boolean valid = false;
		if(moves[0] != moves[2] && moves[1] != moves[3]) {
			//check here to make sure rook move is moving on same column or row		
			System.out.println("Illegal move, try again");
			return board;
		}
		int count = 0;
//		moving up or down
		if(moves[0] == moves[2]) {
			Piece[] check = new Piece[Math.abs(moves[3]-moves[1])];
//			System.out.println(check.length);
//			look at all the spaces between the rows
			if(moves[1] < moves[3]) {
//				rook is moving up
				for(int i = moves[3]; i > moves[1]; i--) {
//					System.out.println(board[i][moves[0]] + " " + count);
					check[count] = board[i][moves[0]];
					count++;
				}
			}
			else {
//				going down
				for(int i = moves[3]; i < moves[1]; i++) {
					check[count] = board[i][moves[0]];
					count++;
				}
			}
			
			//does not account for capturing pieces yet
//			System.out.println("b4 loop");
			for(int i = 0; i < check.length; i++) {
//				System.out.println("checkarr "+ check[i] + " " + i);
				if(check[i] instanceof Piece ) {
//					System.out.println(count);
					if(i == 0 && check[i].black != this.black) {
						continue;
					}
					else {
						System.out.println("Illegal move, try again");
						return board;
					}
				}
			}
			
//			if reached this point, valid move
//			check for capture
			valid = true;
		}
		
		else if(moves[1] == moves[3]) {
//			rows are the same
			Piece[] check = new Piece[Math.abs(moves[2]-moves[0])];
//			System.out.println(check.length);
//			look at all the spaces between the rows
			if(moves[0] < moves[2]) {
//				rook is moving right
				for(int i = moves[2]; i > moves[0]; i--) {
					check[count] = board[moves[1]][i];
					count++;
				}
			}
			else {
//				going left
				for(int i = moves[2]; i < moves[0]; i++) {
					check[count] = board[moves[1]][i];
					count++;
				}
			}
			
			//does not account for capturing pieces yet
			for(int i = 0; i < check.length; i++) {
//				System.out.println("checkarr "+ check[i] + " " + i);
				if(check[i] instanceof Piece ) {
//					System.out.println(count);
					if(i == 0 && check[i].black != this.black) {
						continue;
					}
					else {
						System.out.println("Illegal move, try again");
						return board;
					}
				}
			}
			
//			if reached this point, valid move

			valid = true;
		}
		if(valid && (board[moves[3]][moves[2]] == null || (board[moves[3]][moves[2]] instanceof Piece && board[moves[3]][moves[2]].black != this.black))) {
			Piece m = board[moves[1]][moves[0]];
			board[moves[3]][moves[2]] = m;
			board[moves[1]][moves[0]] = null;
			this.moved = true;
			return board;
		}
		else {
			System.out.println("Illegal move, try again");
			return board;
		}
	}

}

class Bishop extends Piece {
	{
		super.name = "B";
	}

	Piece[][] makemove(Piece [][] board, int[] moves){
//		System.out.println("This is a bishop piece");
//		System.out.println(Arrays.toString(moves));
		if(Math.abs(moves[0] - moves[2]) != Math.abs(moves[1] - moves[3])) {
			System.out.println("Illegal move, try again");
			return board;
		}
		else {
			Piece[] check = new Piece[Math.abs(moves[3]-moves[1])];
			int count = 0;
			if(Math.abs(moves[2]-moves[0]) == 1) {
//				System.out.println("1 col apart");
				if(board[moves[3]][moves[2]] instanceof Piece && this.black == board[moves[3]][moves[2]].black) {
					System.out.println("Illegal move, try again");
					return board;
				}
				
			}
			
			if(moves[0] < moves[2]) {
				if(moves[1] < moves[3]) {
	//				moving top right
//					System.out.println("top right");
					for(int i = 1; i < check.length; i++) {
						check[count] = board[moves[1] + i][moves[0] + i];
						count++;
					}
				}
				else {
	//				moving bottom right
//					System.out.println("bot right");
					for(int i = 1; i < check.length - 1; i++) {
						check[count] = board[moves[1] - i][moves[0] + i];
						count++;
					}
				}
			}
			else {
				if(moves[1] < moves[3]) {
	//				moving top left
//					System.out.println("top left");
					for(int i = 1; i < check.length; i++) {
						check[count] = board[moves[1] + i][moves[0] - i];
						count++;
					}
				}
				else {
	//				moving bottom left
//					System.out.println("bot left");
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
					System.out.println("Illegal move, try again");
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
//		System.out.println("This is a knight piece");
		boolean valid = false;
		if(moves[0] < moves[2]) {

			if(moves[1] < moves[3]) {
//				moving upper right
//				System.out.println("upright");
				if((moves[1]+2 == moves[3] && moves[0]+1 == moves[2]) || (moves[1]+1 == moves[3] && moves[0]+2 == moves[2])) {
					valid = true;
				}
			}
			else {
//				moving bottom right
//				System.out.println("botright");
				if((moves[1]-2 == moves[3] && moves[0]+1 == moves[2]) || (moves[1]-1 == moves[3] && moves[0]+2 == moves[2])) {
					valid = true;	
				}
			}
		}
		else {
			if(moves[1] < moves[3]) {
//				moving upper left
//				System.out.println("upleft");
				if((moves[1]+2 == moves[3] && moves[0]-1 == moves[2]) || (moves[1]+1 == moves[3] && moves[0]-2 == moves[2])) {
					valid = true;
				}
			}
			else {
//				moving bottom left
//				System.out.println("botleft");
				if((moves[1]-1 == moves[3] && moves[0]-2 == moves[2]) || (moves[1]-2 == moves[3] && moves[0]-1 == moves[2])) {
					valid = true;
				}
			}
		}
		
		
		if(valid && (board[moves[3]][moves[2]] == null || (board[moves[3]][moves[2]] instanceof Piece && board[moves[3]][moves[2]].black != this.black))) {
			
			Piece m = board[moves[1]][moves[0]];
			board[moves[3]][moves[2]] = m;
			board[moves[1]][moves[0]] = null;
			return board;
		}
		System.out.println("Illegal move, try again");
		return board;
	}

}

class Queen extends Piece {
	{
		super.name = "Q";
	}
	Piece[][] makemove(Piece [][] board, int[] moves){
//		System.out.println("This is a queen piece");
// rook stuff
		if(moves[0] == moves[2] || moves[1] == moves[3]) {
			//moving in rook pattern	
					int count = 0;
//		moving up or down
			if(moves[0] == moves[2]) {
				Piece[] check = new Piece[Math.abs(moves[3]-moves[1])];
//				System.out.println(check.length);
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
						System.out.println("Illegal move, try again");
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
//				System.out.println(check.length);
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
						System.out.println("Illegal move, try again");
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
		}

// bishop stuff
		else if(Math.abs(moves[0] - moves[2]) == Math.abs(moves[1] - moves[3])) {
			// moving diagonal
						Piece[] check = new Piece[Math.abs(moves[3]-moves[1])];
			int count = 0;
			if(Math.abs(moves[2]-moves[0]) == 1) {
//				System.out.println("1 col apart");
				if(board[moves[3]][moves[2]] instanceof Piece && this.black == board[moves[3]][moves[2]].black) {
					System.out.println("Illegal move, try again");
					return board;
				}
				
			}
			
			if(moves[0] < moves[2]) {
				if(moves[1] < moves[3]) {
	//				moving top right
//					System.out.println("top right");
					for(int i = 1; i < check.length; i++) {
						check[count] = board[moves[1] + i][moves[0] + i];
						count++;
					}
				}
				else {
	//				moving bottom right
//					System.out.println("bot right");
					for(int i = 1; i < check.length - 1; i++) {
						check[count] = board[moves[1] - i][moves[0] + i];
						count++;
					}
				}
			}
			else {
				if(moves[1] < moves[3]) {
	//				moving top left
//					System.out.println("top left");
					for(int i = 1; i < check.length; i++) {
						check[count] = board[moves[1] + i][moves[0] - i];
						count++;
					}
				}
				else {
	//				moving bottom left
//					System.out.println("bot left");
					for(int i = 1; i < check.length; i++) {
						check[count] = board[moves[1] - i][moves[0] - i];
						count++;
					}
				}
			}
//			System.out.println(Arrays.toString(check));
			for(int i = 0; i < check.length; i++) {
				if(check[i] instanceof Piece) {
					System.out.println("Illegal move, try again");
					return board;
				}
			}
			Piece m = board[moves[1]][moves[0]];
			board[moves[3]][moves[2]] = m;
			board[moves[1]][moves[0]] = null;
			
			return board;

		}


		System.out.println("Illegal move, try again");
		return board;
	}


}

class King extends Piece {
	boolean moved = false;
	{
		super.name = "K";
	}
	Piece[][] makemove(Piece [][] board, int[] moves){
//		System.out.println("This is a king piece");
		boolean valid = false;
		int[] kingoffset = {(moves[0] - moves[2]),(moves[1]-moves[3])};
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
	    
//	    first check if its trying to castle
	    if(Math.abs(moves[0] - moves[2]) == 2) {
//	    	trying to move two spaces either side
	    	if(this.moved) {
	    		System.out.println("Illegal move, try again");
	    		return board;
	    	}
	    	else if(moves[0] < moves[2]) {
//	    		moving right 
	    		if(board[moves[1]][moves[0]+3] instanceof Rook && !board[moves[1]][moves[0]+3].moved && 
	    				board[moves[1]][moves[0]+1] == null && board[moves[1]][moves[0]+2] == null) {
//	    			check if rook in initial position and hasnt moved and space in between is empty
	    			Piece m = board[moves[1]][moves[0]];
	    			Piece r = board[moves[1]][moves[0]+3];
	    			board[moves[3]][moves[2]] = m;
	    			board[moves[3]][moves[2]-1] = r;
	    			board[moves[1]][moves[0]] = null;
	    			board[moves[1]][moves[0]+3] = null;
	    			this.moved = true;
	    			r.moved = true;
	    			return board;
	    		}
	    	}
    		else if(moves[0] > moves[2]){
//	    			going left
	    		if(board[moves[1]][moves[0]-4] instanceof Rook && !board[moves[1]][moves[0]-4].moved && 
	    				board[moves[1]][moves[0]-1] == null && board[moves[1]][moves[0]-2] == null &&
	    				board[moves[1]][moves[0]-3] == null) {
//		    			check if rook in initial position and hasnt moved and space in between is empty
	    			Piece m = board[moves[1]][moves[0]];
	    			Piece r = board[moves[1]][moves[0]-4];
	    			board[moves[3]][moves[2]] = m;
	    			board[moves[3]][moves[2]+1] = r;
	    			board[moves[1]][moves[0]] = null;
	    			board[moves[1]][moves[0]-4] = null;
	    			this.moved = true;
	    			r.moved = true;
	    			return board;
	    		}
    		}
	    	
	    }
	    
	    else {
//	    	check for if king did a regular move
		    for(int i = 0; i < offsets.length; i++) {
	//	    	System.out.println(offsets[i][0] +" " + offsets[i][1]  + ": " + kingoffset[0]+" " +kingoffset[1]);
		    	if(Arrays.equals(offsets[i], kingoffset))
		    		valid = true;
		    }
	    }
	    
	    if(valid && (board[moves[3]][moves[2]] == null || (board[moves[3]][moves[2]] instanceof Piece && board[moves[3]][moves[2]].black != this.black))) {
			Piece m = board[moves[1]][moves[0]];
			board[moves[3]][moves[2]] = m;
			board[moves[1]][moves[0]] = null;
			this.moved = true;
			return board;
	    }
		System.out.println("Illegal move, try again");
		return board;
	}

}