package com.javashogi.board;

public class Board {
    @Override
    public String toString() {
        return "Empty Shogi board (placeholder)";
    }

    private Piece[][] board;

    public Board() {
        this.board = new Piece[9][9];
        setupInitialPosition();
    }

    private void setupInitialPosition() {
        // These pieces initialization is for Gote (White) side

        // Row 0
        board[0][0] = new Lance(false);
        board[0][1] = new Knight(false);
        board[0][2] = new SilverGeneral(false);
        board[0][3] = new GoldGeneral(false);
        board[0][4] = new King(false);
        board[0][5] = new GoldGeneral(false);
        board[0][6] = new SilverGeneral(false);
        board[0][7] = new Knight(false);
        board[0][8] = new Lance(false);

        // Row 1
        board[1][1] = new Rock(false);
        board[1][7] = new Bishop(false);

        // Row 2
        for (int col = 0; col < 9; col++) {
            board[2][col] = new Pawn(false);
        }

        // These pieces initialization is for Sente (Black) side

        // Row 6
        for (int col = 0; col < 9; col++) {
            board[6][col] = new Pawn(true);
        }

        // Row 7
        board[7][1] = new Bishop(true);
        board[7][7] = new Rock(true);

        // Row 8
        board[8][0] = new Lance(true);
        board[8][1] = new Knight(true);
        board[8][2] = new SilverGeneral(true);
        board[8][3] = new GoldGeneral(true);
        board[8][4] = new King(true);
        board[8][5] = new GoldGeneral(true);
        board[8][6] = new SilverGeneral(true);
        board[8][7] = new Knight(true);
        board[8][8] = new Lance(true);
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
