package boardgame;

public class Piece {

	/*não quero que essa posição seja visivel na camada de xadrez */
	protected Position position; 
	private Board board;
	
	
	public Piece(Board board) {
		this.board = board;
		position = null;
	}


	protected Board getBoard() {
		return board;
	}


	
	
}
