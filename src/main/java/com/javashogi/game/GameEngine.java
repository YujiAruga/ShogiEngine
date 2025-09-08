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

        // Simulate on a copy
        Board temp = board.deepCopy();
        if (!temp.applyMoveUnchecked(fromRow, fromCol, toRow, toCol, promoteIfAvailable)) return false;

        // check self-check
        if (temp.isInCheck(piece.isBlack())) return false;

        if (!board.applyMoveUnchecked(fromRow, fromCol, toRow, toCol, promoteIfAvailable)) return false;

        // If this turn was Black, the next turn will be White and vice versa
        blackToMove = !blackToMove;

        return true;
    }

    public boolean drop(Class<? extends Piece> type, int row, int col) {
        boolean isBlack = blackToMove;

        // Simulate
        Board temp = board.deepCopy();
        if (!temp.dropUnchecked(type, row, col, isBlack)) return false;
        // cannot drop leaving own king in check
        if (temp.isInCheck(isBlack)) return false;
        if (!board.dropUnchecked(type, row, col, blackToMove)) return false;

        // If this turn was Black, the next turn will be White and vice versa
        blackToMove = !blackToMove;

        return true;
    }


}
