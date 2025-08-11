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
        if (board.isOwnAt(toRow, toCol, isBlack)) return false;
        //if (fromRow != toRow && fromCol != toCol) return false;

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

        if (promoted) {
            int[][] neighbors = new int[][] {
                    {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
            };

            for (int[] neighbor : neighbors) {
                if (fromRow + neighbor[0] == toRow && fromCol + neighbor[1] == toCol) {
                    return true;
                }
            }
        }

        return false;
    }
}
