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
        if (promoted) return goldLikeMove(fromRow, fromCol, toRow, toCol, board);

        if (!board.inBounds(toRow, toCol)) return false;
        else if (fromRow == toRow && fromCol == toCol) return false;
        else if (board.isOwnAt(toRow, toCol, isBlack)) return false;

        int direction = isBlack ? -1 : 1;
        return (toRow == fromRow + direction) && (toCol == fromCol) && board.getPiece(toRow, toCol) == null;
    }
}
