package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
	
	private int turn; 
	private Color currentPlayer; 
	private Board board;
	private boolean check; 
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	
	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;		
		currentPlayer = Color.WHITE;	
		/*ja chama o setup logo no inicio*/
		initialSetup();
	}
	
	public int getTurn() {
		return turn; 
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer; 
	}
	
	public boolean getCheck() {
		return check; 
	}
 
	/*retorna a matriz de pe�as da partida de xadrez  */
	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i=0; i<board.getRows(); i++) {
			for (int j=0; j <board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i,j);
			}
		}
		return mat;
	}
	
	//metodo de movimentos possiveis dado uma posi��o para a aplica��o colorir o fundo de cada posi��o 
	public boolean[][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	
	 /*metodo que move a pe�a*/
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		
		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check");
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false; 
		
		nextTurn();
		return (ChessPiece)capturedPiece;
		
	}
	
	private Piece makeMove(Position source, Position target) {
		/*retirei a pe�a que estava na posi��o de origem*/
		Piece p = board.removePiece(source);
		/*removendo a possivel pe�a que esteja na posi��o de destino e ela por padr�o vai ser a pe�a capturada*/
		Piece capturedPiece = board.removePiece(target);
		/*colocando a posi��o que estava na origem na posi��o de destino*/
		board.placePiece(p, target);
		
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		return capturedPiece;
	}
	
	// desfazendo o movimento
	private void undoMove(Position source, Position target, Piece capturedPiece ) {
		Piece p = board.removePiece(target);
		board.placePiece(p, source);
		
		//voltando a pe�a que foi capturada pra sua posi��o de destino
		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
			
		}
	}
	
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		//verificando se o pe�a n�o � do adversario para n�o ocorrer o caso de mover pe�as erradas
		if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
			throw new ChessException ("The chosen piece is nor yours");
		}
		/*se n�o tiver nenhuma posi��o possivel eu lan�o uma exce��o*/
		if (!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chosen piece");
		}
	}
	
	// metodo para validar o target
		private void validateTargetPosition(Position source, Position target) {
			if (!board.piece(source).possibleMove(target)) {
				throw new ChessException("The chosen piece can't move to target position");
			}
		} 
		
	// metodo que troca o turno, ou seja troca o jogador que vai fazer a play 	
	
	private void nextTurn () {
		turn++;
		//nessa troca de turno a logica pe se o jogador for da pe�a branca ent�o o proximo a jogar seria a preta caso o contrario vai o branco
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
		
	}	
	
	//devolve o devido oponente do que est� jogando 
	private Color opponent(Color color) {
		// se essa core for igual a branco ent�o (?) vou retornar preto caso o contrario (:) eu retorno branco
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x ->((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for(Piece p : list) {
			if (p instanceof King) {
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("There is no " + color + "king on the board");
	}
	//fazer uma logica que tem que passar por todas as pe�as adiversarias e verificar se elas n�o podem cair na casa do rei 
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x ->((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			boolean [][] mat = p.possibleMoves();
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true; 
			}
		}
		return false; 
	}
	
	
	/*metodo que recebe as cordenadas do xadrez*/
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}
	
	/*iniciando o setup do jogo*/
	private void initialSetup() {
		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));

        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK));
	}
}
