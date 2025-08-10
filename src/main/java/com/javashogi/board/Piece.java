package com.javashogi.board;

public abstract class Piece {
    protected boolean isBlack; // if this is true, player is sente which means that this piece is the first player's piece.
    protected boolean promoted = false;

    public Piece(boolean isBlack) {
        this.isBlack = isBlack;
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void promote() {
        promoted = true;
    }

    public boolean isBlack() {
        return isBlack;
    }

    public abstract char getSymbol();

    public abstract boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Board board);

    protected boolean goldLikeMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        if (!board.inBounds(toRow, toCol)) return false;
        else if (toRow == fromRow && toCol == fromCol) return false;
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
