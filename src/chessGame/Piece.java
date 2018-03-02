package chessGame;

public class Piece {
	String color;
	boolean active;
	char movePattern;
}

class Pawn extends Piece{
	char movePattern = ':';
}

class Rook extends Piece{
	char movePattern = '+';
}

class Bishop extends Piece{
	char movePattern = 'x';
}

class Knight extends Piece{
	char movePattern = 'L';
}

class Queen extends Piece{
	char movePattern = '*';
}

class King extends Piece{
	char movePattern = '.';
}