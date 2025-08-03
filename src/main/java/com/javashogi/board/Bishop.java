package com.javashogi.board;

public class Bishop extends Piece {
    public Bishop(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public char getSymbol() {
        return isBlack ? 'B' : 'b';
    }
}
