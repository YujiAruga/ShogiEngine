package com.javashogi.board;

public class Lance extends Piece {
    public Lance(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public char getSymbol() {
        return isBlack ? 'L' : 'l';
    }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        if (promoted) return goldLikeMove(fromRow, fromCol, toRow, toCol, board);

        if (fromRow == toRow && fromCol == toCol) return false;
        int direction = isBlack ? -1 : 1;

        if (!board.inBounds(toRow,toCol) || fromCol != toCol) {
            return false;
        }

        if (isBlack) {
            if (toRow >= fromRow) return false;
            for (int i = fromRow - 1; i > toRow; i--) {
                if (!board.isEmpty(i, toCol)) {
                    return false;
                }
            }
        }
        else {
            if (toRow <= fromRow) return false;
            for (int i = fromRow + 1; i < toRow; i++) {
                if (!board.isEmpty(i, toCol)) {
                    return false;
                }
            }
        }

        return !board.isOwnAt(toRow, toCol, isBlack);
    }
}
