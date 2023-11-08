package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece  {

	private Color color;

	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	public ChessPosition getChessPosition() {
		//convertendo o position para ChessPosition
		return ChessPosition.fromPosition(position);
	}
	
	/*ela é protected pois so pode ser acessada por ela mesma e as peças*/
	
	protected boolean isThereOpponentPiece(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p != null && p.getColor() != color; 
	}

	
}
