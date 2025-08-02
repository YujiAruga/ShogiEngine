package com.javashogi.board;

public class Board {
    @Override
    public String toString() {
        return "Empty Shogi board (placeholder)";
    }

    private Piece[][] board;

    public Board() {
        this.board = new Piece[9][9];
    }

    private void setupInitialPosition() {
        board[0][4] = new King(false);
        board[8][4] = new King(true);

        // Other pieces
    }

    public void printBoard() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Piece piece = board[row][col];
                System.out.print(piece == null ? "." : piece.getSymbol());
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
