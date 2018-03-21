package chessGame;
import java.util.Arrays;
import java.util.Scanner;

public class ChessBoard {

	
	public static void main(String[] args) {
//		set up board and player pieces
		boolean gameOn = true;
		boolean blackTurn = false;
		Piece [][] board = new Piece[8][8];
		
		Piece [] white = new Piece[]{
				new Pawn(), new Pawn(),new Pawn(),new Pawn(),new Pawn(),new Pawn(),new Pawn(),new Pawn(),
				new Rook(), new Knight(), new Bishop(),  new Queen(), new King(), new Bishop(),  new Knight(),new Rook(), 
				};
		Piece [] black = new Piece[]{
				new Pawn(), new Pawn(),new Pawn(),new Pawn(),new Pawn(),new Pawn(),new Pawn(),new Pawn(),
				new Rook(), new Knight(), new Bishop(), new Queen(), new King(), new Bishop(), new Knight(), new Rook(), 
				};
		

		for(int i =0; i < white.length; i++ ) {
			white[i].black = false;
		}
		String[] names = {"Q", "R", "N", "B"};
				
				
		board[7] = Arrays.copyOfRange(black, 8, 16);
		board[6] = Arrays.copyOfRange(black, 0, 8);
		board[0] = Arrays.copyOfRange(white, 8, 16);
		board[1] = Arrays.copyOfRange(white, 0, 8);
		
//		temp deleting pieces for movement testing
//		board[6][0] = null;
//		board[1][0] = null;

		
		
		drawBoard(board);
//		printBoard(board);
		String whiteMove = "", blackMove = "", mov = "";
		//System.out.println("New Game. White moves first."); Assignment want specific outputs or penalty
		Scanner sc = new Scanner(System.in);
		
		boolean drawAsked = false;
		while(gameOn) {
			//fromTo will hold input as an array split into from and to move
			String [] fromTo;
			
			if(blackTurn) {
				System.out.print("\nBlack's move: ");
				blackMove = sc.nextLine();
				System.out.println();
				mov = blackMove;
				fromTo = blackMove.split(" ");
			}
			else {
				System.out.print("\nWhite's move: ");
				whiteMove = sc.nextLine();
				System.out.println();
				mov = whiteMove;
				fromTo = whiteMove.split(" ");
			}
//			end game if forfeit
			if(whiteMove.equals("resign")){
				gameOn = false;
				System.out.println("Black wins");
				break;
			}
			else if(blackMove.equals("resign")){
				gameOn = false;
				System.out.println("White wins");
				break;
			}
			
			// Handling Draw
			if(drawAsked && (whiteMove.equals("draw") || blackMove.equals("draw"))) {
				System.out.println("Draw");
				break;
			} else {
				drawAsked = false;
			}
			
			if (fromTo.length == 3 && fromTo[2].equals("draw?")) {
				drawAsked = true;
				fromTo = new String[] {fromTo[0], fromTo[1]};
			}
			
//			System.out.println(fromTo[0] + " " + inputToInt(fromTo)[0] + " " + inputToInt(fromTo)[1] + " " + inputToInt(fromTo)[2] + " "+ inputToInt(fromTo)[3]);
			int[] input = inputToInt(fromTo);
			
			if(fromTo.length == 2 && board[input[1]][input[0]] instanceof Pawn &&((board[input[1]][input[0]].black && input[3] == 0) ||  (!board[input[1]][input[0]].black && input[3] == 7))) {
				blackTurn = !blackTurn;
				boolean color = board[input[1]][input[0]].black;
				Piece[][] temp = copy(board);
				board = board[input[1]][input[0]].makemove(board, input);
				
				if(Arrays.deepEquals(board, temp)) {

					blackTurn = !blackTurn;
				}
				else {
					Queen q = new Queen();
					q.black = color;
					board[input[3]][input[2]] = q;
				}
			}
			
			if(fromTo.length == 3) {
//				check for pawn promotion command
				if(Arrays.asList(names).contains(fromTo[2]) && board[input[1]][input[0]] instanceof Pawn) {
//					check if pawn piece is reaching 8th rank
					if((board[input[1]][input[0]].black && input[3] == 0) ||  (!board[input[1]][input[0]].black && input[3] == 7)) {
						blackTurn = !blackTurn;
						boolean color = board[input[1]][input[0]].black;
						Piece[][] temp = copy(board);
						board = board[input[1]][input[0]].makemove(board, input);
						
						if(Arrays.deepEquals(board, temp)) {

							blackTurn = !blackTurn;
						}
						else {
//							promote here 
							switch (fromTo[2]) {
								case "Q": 
									Queen q = new Queen();
									q.black = color;
									board[input[3]][input[2]] = q;
									break;
								case "R":
									Rook r = new Rook();
									r.black = color;
									board[input[3]][input[2]] = r;
									break;
								case "N":
									Knight n = new Knight();
									n.black = color;
									board[input[3]][input[2]] = n;
									break;
								case "B":
									Bishop b = new Bishop();
									b.black = color;
									board[input[3]][input[2]] = b;
									break;
							}
						}
					}
				}
			}
			
			
			else if(board[input[1]][input[0]] instanceof Piece && board[input[1]][input[0]].black == blackTurn) {
				
				blackTurn = !blackTurn;
				
				Piece[][] temp = copy(board);
				board = board[input[1]][input[0]].makemove(board, input);

				
				if(Arrays.deepEquals(board, temp)) {

					blackTurn = !blackTurn;
				}
		

			}
			else {
//				System.out.println("instanceof nuttin ");
//				blackTurn = !blackTurn;
			}
//			System.out.println("black turn after moves: " + blackTurn);
			
//			take board and false all en passants on opposite turn
			board = falseEnpassant(board, blackTurn);
			drawBoard(board);
		}
		
	}
	
	
// false all enpassant
	public static Piece[][] falseEnpassant(Piece[][] board, boolean turn){
		for (int y = 7; y >= 0; y--)
		    for (int x = 7; x >= 0; x--)
		        if(board[x][y] != null && board[x][y].black == turn) {
		        	board[x][y].enpassant = false;
		        }	
		return board;
	}
//	copy board onto temp
	public static Piece[][] copy(Piece[][] board){
		Piece[][] copy = new Piece[8][8];
		for (int y = 7; y >= 0; y--)
		    for (int x = 7; x >= 0; x--)
		        copy[x][y] = board[x][y];

		return copy;
	}
	
	
// convert input move to int array
	public static int[] inputToInt(String[] splitentry) {
		int fromx = splitentry[0].charAt(0) - 'a' ;
		int fromy = splitentry[0].charAt(1) - 49;
		int tox = splitentry[1].charAt(0) - 'a' ;
		int toy = splitentry[1].charAt(1) - 49;
		
		return new int[] {fromx, fromy, tox, toy};
	}

	
//	print each row of the board
	public static String row(Piece[] row, int rowNumber) {
		String r = "";
		for(int i =0; i < row.length; i++) {
			if(row[i] instanceof Piece) {

				if(row[i].black) {
						r += "b"+row[i].name + " ";
					}
					else
						r += "w"+row[i].name + " ";
			}
			else {
				if(rowNumber%2 == 0) {
					if(i%2 == 0) {
						r+= "   ";
					}
					else 
						r+= "## ";
				}
				else {
					if(i%2 == 1) {
						r+= "   ";
					}
					else 
						r+= "## ";
				}
			}
		}
		
		return r + "" + (rowNumber+1);
	}
	
	public static void drawBoard(Piece[][] board) {
//		return board as string
		for(int i = 7; i >= 0; i--) {
			System.out.println(row(board[i], i));
		}
		System.out.println(" a  b  c  d  e  f  g  h");
	}


	public static void printBoard(Piece[][] board) {
		for(int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++){
				System.out.print(board[i][j] + " ");
			}
		}
	}
}
