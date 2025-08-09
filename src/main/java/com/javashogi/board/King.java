package com.javashogi.board;

public class King extends Piece {
    public King(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public char getSymbol() {
        return isBlack ? 'K' : 'k';
    }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        if (!board.inBounds(toRow, toCol)) return false;
        int[][] neighbors = new int[][] {
            {1, 1}, {1, 0}, {1, -1}, {0, 1}, {0, -1}, {-1, 1}, {-1, 0}, {-1, -1}
        };

        for (int[] neighbor : neighbors) {
            if (fromRow + neighbor[0] == toRow && fromCol + neighbor[1] == toCol && !board.isOwnAt(toRow, toCol, isBlack)) {
                return true;
            }
        }

        return false;
    }
}
