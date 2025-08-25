package com.javashogi.game;

import com.javashogi.board.Board;
import com.javashogi.board.Piece;

public class GameEngine {
    private final Board board;
    private boolean blackToMove = true;

    public GameEngine(Board board) { this.board = board; }
    public boolean isBlackToMove() { return blackToMove; }
    public Board board() { return board; }


}
