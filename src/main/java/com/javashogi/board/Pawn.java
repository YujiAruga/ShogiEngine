package com.javashogi.board;

public class Pawn extends Piece {
    public Pawn(boolean isBlack) {
        super(isBlack);
    }
    // In terminal command, we can not tell which pieces belong to which player.
    // So pieces with capital letter belong to Black (Sente) and others belong to White (gote)
    @Override
    public char getSymbol() {
        return isBlack ? 'P' : 'p';
    }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        int direction = isBlack ? -1 : 1;
        return (toRow == fromRow + direction) && (toCol == fromCol) && board.getPiece(toRow, toCol) == null;
    }
}
