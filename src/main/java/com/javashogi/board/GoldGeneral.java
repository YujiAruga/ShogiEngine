package com.javashogi.board;

public class GoldGeneral extends Piece {
    public GoldGeneral(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public char getSymbol() {
        return isBlack ? 'G' : 'g';
    }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        if (!board.inBounds(toRow, toCol)) return false;
        else if (fromRow == toRow && fromCol == toCol) return false;
        else if (board.isOwnAt(toRow, toCol, isBlack)) return false;

        int[][] neighbors = new int[][] {
                {-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}
        };

        if (isBlack) {
            for (int[] neighbor : neighbors) {
                if ((neighbor[0] == 1 && neighbor[1] == -1) || (neighbor[0] == 1 && neighbor[1] == 1)) {
                    continue;
                }

                if (fromRow + neighbor[0] == toRow && fromCol + neighbor[1] == toCol) {
                    return true;
                }
            }
        }
        else {
            for (int[] neighbor : neighbors) {
                if ((neighbor[0] == -1 && neighbor[1] == -1) || (neighbor[0] == -1 && neighbor[1] == 1)) {
                    continue;
                }

                if (fromRow + neighbor[0] == toRow && fromCol + neighbor[1] == toCol) {
                    return true;
                }
            }
        }

        return false;
    }
}
