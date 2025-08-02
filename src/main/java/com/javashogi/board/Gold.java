package com.javashogi.board;

public class Gold extends Piece {
    public Gold(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public char getSymbol() {
        return isBlack ? 'G' : 'g';
    }
}
