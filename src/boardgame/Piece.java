package boardgame;

public abstract class Piece {

	/*n�o quero que essa posi��o seja visivel na camada de xadrez */
	protected Position position; 
	private Board board;
	
	
	public Piece(Board board) {
		this.board = board;
		position = null;
	}


	protected Board getBoard() {
		return board;
	}

	public abstract boolean [][] possibleMoves();

	public boolean possibleMove(Position position) {
		return possibleMoves()[position.getRow()][position.getColumn()];
	}	
	
	public boolean isThereAnyPossibleMove() {
		boolean [][] mat = possibleMoves();
		/*pecorrendo a matriz "mat" para ver se alguma posi��o � verdadeira*/
		for(int i=0; i<mat.length; i++) {
			for(int j=0; j<mat.length; j++) {
				if(mat[i][j]) {
					return true; 
				}
			}
			
		}
		return false;
	}

	
}
