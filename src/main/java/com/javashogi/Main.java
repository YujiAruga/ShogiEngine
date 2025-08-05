package com.javashogi;

import com.javashogi.board.Board;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        board.printBoard();

        System.out.println("Trying to move black pawn at (6, 4) to (5, 4)...");
        boolean moved = board.movePiece(6, 4, 5, 4);
        System.out.println("Move result: " + moved);
        board.printBoard();
    }
}

