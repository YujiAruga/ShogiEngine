package com.javashogi.board;

public class Bishop extends Piece {
    public Bishop(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public char getSymbol() {
        return isBlack ? 'B' : 'b';
    }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        if (!board.inBounds(toRow, toCol)) return false;
        if (fromRow == toRow && fromCol == toCol) return false;
        if (Math.abs(toRow - fromRow) != Math.abs(toCol - fromCol)) return false;

        if (fromRow < toRow && fromCol < toCol) {
            for (int i = 1; i < (toRow - fromRow); i++) {
                if (!board.isEmpty(fromRow + i, fromCol + i)) return false;
            }
        }
        else if (fromRow < toRow && fromCol > toCol) {
            for (int i = 1; i < (toRow - fromRow); i++) {
                if (!board.isEmpty(fromRow + i, fromCol - i)) return false;
            }
        }
        else if (fromRow > toRow && fromCol > toCol) {
            for (int i = 1; i < (fromRow - toRow); i++) {
                if (!board.isEmpty(fromRow - i, fromCol - i)) return false;
            }
        }
        else {
            for (int i = 1; i < (fromRow - toRow); i++) {
                if (!board.isEmpty(fromRow - i, fromCol + i)) return false;
            }
        }

        return !board.isOwnAt(toRow, toCol, isBlack);
    }
}
