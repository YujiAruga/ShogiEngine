package com.javashogi.board;

public class Rook extends Piece {
    public Rook(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public char getSymbol() {
        return isBlack ? 'R' : 'r';
    }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        if (!board.inBounds(toRow, toCol)) return false;
        if (fromRow == toRow && fromCol == toCol) return false;
        if (fromRow != toRow && fromCol != toCol) return false;

        if (fromRow != toRow) {
            int min = Math.min(fromRow, toRow);
            int max = Math.max(fromRow, toRow);

            for (int i = min + 1; i < max; i++) {
                if (!board.isEmpty(i, toCol)) {
                    return false;
                }
            }
        }
        else {
            int min = Math.min(fromCol, toCol);
            int max = Math.max(fromCol, toCol);

            for (int i = min + 1; i < max; i++) {
                if (!board.isEmpty(toRow, i)) {
                    return false;
                }
            }
        }

        return !board.isOwnAt(toRow, toCol, isBlack);
    }
}
