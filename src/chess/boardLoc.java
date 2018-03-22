package chess;

public class boardLoc {
	int row;
	int col;
	public boardLoc() {
		this.row = 0;
		this.col = 0;
	}
	public boardLoc(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	@Override
    public boolean equals(Object object)
    {
        boolean isSame = false;

        if (object != null && object instanceof boardLoc)
        {
            isSame = (this.row == ((boardLoc) object).row) && this.col == ((boardLoc) object).col;
        }

        return isSame;
    }
	
	@Override
    public String toString() {
		return "row: " + this.row + " col: " + this.col;
	}

}
