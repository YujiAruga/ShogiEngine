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
        if (board.isOwnAt(toRow, toCol, isBlack)) return false;

        if (promoted) {
            int[][] neighbors = new int[][] {
                    {1, 0}, {0, 1}, {-1, 0}, {0, -1}
            };

            for (int[] neighbor : neighbors) {
                if (fromRow + neighbor[0] == toRow && fromCol + neighbor[1] == toCol) {
                    return true;
                }
            }
        }

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

        return true;
    }
}
