package com.javashogi.board;

public class SilverGeneral extends Piece {
    public SilverGeneral(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public char getSymbol() {
        return isBlack ? 'S' : 's';
    }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        if (!board.inBounds(toRow, toCol)) return false;
        if (fromRow == toRow && fromCol == toCol) return false;
        if (promoted) return goldLikeMove(fromRow, fromCol, toRow, toCol, board);
        if (board.isOwnAt(toRow, toCol, isBlack)) return false;

        int[][] neighbors = new int[][] {
                {-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}
        };

        if (isBlack) {
            for (int[] neighbor : neighbors) {
                if ((neighbor[0] == 1 && neighbor[1] == 0) ||
                        (neighbor[0] == 0 && neighbor[1] == 1) ||
                        (neighbor[0] == 0 && neighbor[1] == -1)) {
                    continue;
                }

                if (fromRow + neighbor[0] == toRow && fromCol + neighbor[1] == toCol) {
                    return true;
                }
            }
        }
        else {
            for (int[] neighbor : neighbors) {
                if ((neighbor[0] == -1 && neighbor[1] == 0) ||
                        (neighbor[0] == 0 && neighbor[1] == 1) ||
                        (neighbor[0] == 0 && neighbor[1] == -1)) {
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
