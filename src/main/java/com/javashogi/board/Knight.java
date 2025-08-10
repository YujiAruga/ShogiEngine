package com.javashogi.board;

public class Knight extends Piece {
    public Knight(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public char getSymbol() {
        return isBlack ? 'K' : 'k';
    }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        if (!board.inBounds(toRow, toCol)) return false;
        else if (fromRow == toRow && fromCol == toCol) return false;
        else if (board.isOwnAt(toRow, toCol, isBlack)) return false;

        if (isBlack) {
            if ((toRow == fromRow - 2 && toCol == fromCol - 1) || (toRow == fromRow - 2 && toCol == fromCol + 1)) {
                return true;
            }
        }
        else {
            if ((toRow == fromRow + 2 && toCol == fromCol - 1) || (toRow == fromRow + 2 && toCol == fromCol + 1)) {
                return true;
            }
        }

        return false;
    }
}
