package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
	
	private int turn; 
	private Color currentPlayer; 
	private Board board;
	
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
 
	/*retorna a matriz de peças da partida de xadrez  */
	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i=0; i<board.getRows(); i++) {
			for (int j=0; j <board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i,j);
			}
		}
		return mat;
	}
	
	//metodo de movimentos possiveis dado uma posição para a aplicação colorir o fundo de cada posição 
	public boolean[][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	
	 /*metodo que move a peça*/
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		nextTurn();
		return (ChessPiece)capturedPiece;
		
	}
	
	private Piece makeMove(Position source, Position target) {
		/*retirei a peça que estava na posição de origem*/
		Piece p = board.removePiece(source);
		/*removendo a possivel peça que esteja na posição de destino e ela por padrão vai ser a peça capturada*/
		Piece capturedPiece = board.removePiece(target);
		/*colocando a posição que estava na origem na posição de destino*/
		board.placePiece(p, target);
		return capturedPiece;
	}
	
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		//verificando se o peça não é do adversario para não ocorrer o caso de mover peças erradas
		if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
			throw new ChessException ("The chosen piece is nor yours");
		}
		/*se não tiver nenhuma posição possivel eu lanço uma exceção*/
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
		//nessa troca de turno a logica pe se o jogador for da peça branca então o proximo a jogar seria a preta caso o contrario vai o branco
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
		
	}	
	
	/*metodo que recebe as cordenadas do xadrez*/
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
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
