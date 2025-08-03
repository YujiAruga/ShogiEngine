package com.javashogi.board;

public class Lance extends Piece {
    public Lance(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public char getSymbol() {
        return isBlack ? 'L' : 'l';
    }
}
