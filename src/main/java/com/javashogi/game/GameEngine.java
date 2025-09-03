package com.javashogi.game;

import com.javashogi.board.Board;
import com.javashogi.board.Piece;

public class GameEngine {
    private final Board board;
    private boolean blackToMove = true;

    public GameEngine(Board board) { this.board = board; }
    public boolean isBlackToMove() { return blackToMove; }
    public Board board() { return board; }

    public boolean makeMove(int fromRow, int fromCol, int toRow, int toCol, boolean promoteIfAvailable) {
        Piece piece = board.getPiece(fromRow, fromCol);
        if (piece == null || piece.isBlack() != blackToMove) return false;

        if (!board.applyMoveUnchecked(fromRow, fromCol, toRow, toCol, promoteIfAvailable)) return false;

        // If this turn was Black, the next turn will be White and vice versa
        blackToMove = !blackToMove;

        return true;
    }

    public boolean drop(Class<? extends Piece> type, int row, int col) {
        if (!board.dropUnchecked(type, row, col, blackToMove)) return false;

        // If this turn was Black, the next turn will be White and vice versa
        blackToMove = !blackToMove;

        return true;
    }


}
