package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {
	
	//acrescentando uma dependencia pra partida 
	private ChessMatch chessMatch;

	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch; 

	}
	
	@Override 
	public String toString() {
		return "K"; 
	}
	
	// esse metodo vai dizer se o rei pode mover para determinada posição
	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		//caso o p (position) for vazio quer dizer que ele pode se mover || (ou) se a cor for diferente também
		return p == null || p.getColor() != getColor();
	}
	
	//metodo auxiliar que vai ajudar a testar a condição de hook 
	private boolean testRookCastling(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p != null && p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean [][] mat = new boolean [getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0, 0);
		
		//acima 
		p.setValues(position.getRow() - 1, position.getColumn());
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//abaixo 
		p.setValues(position.getRow() + 1, position.getColumn());
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// esquerda 
		p.setValues(position.getRow(), position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// direita 
		p.setValues(position.getRow(), position.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// diagonal cima esquerda 
		p.setValues(position.getRow() - 1, position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}	
		
		// diagonal cima direita 
		p.setValues(position.getRow() - 1 , position.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//diagonal baixo esquerda
		p.setValues(position.getRow() + 1 , position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//diagonal baixa direita 
		p.setValues(position.getRow() + 1 , position.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//#movimento especial castling
		if (getMoveCount() == 0 && !chessMatch.getCheck()) {
			//movimento especial rook do lado do King  
			Position posT1 = new Position(position.getRow(), position.getColumn() + 3);
			if (testRookCastling(posT1)) {
				Position p1 = new Position(position.getRow(), position.getColumn() + 1);
				Position p2 = new Position(position.getRow(), position.getColumn() + 2);
				if (getBoard().piece(p1) == null && getBoard().piece(p2) == null) {
					mat[position.getRow()][position.getColumn() + 2] = true;
				}
			}
		}
		//movimento especial rook mais longe do King  
		Position posT2 = new Position (position.getRow(), position.getColumn() - 4);
		if (testRookCastling(posT2)) {
			Position p1 = new Position (position.getRow(), position.getColumn() - 1);
			Position p2 = new Position (position.getRow(), position.getColumn() - 2);
			Position p3 = new Position (position.getRow(), position.getColumn() - 3);
			if (getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3) == null) {
				mat[position.getRow()][position.getColumn() - 2] = true;
			}
		}
		
		return mat;
	}

}
