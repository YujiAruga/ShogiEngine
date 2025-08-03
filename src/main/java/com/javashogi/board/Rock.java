package com.javashogi.board;

public class Rock extends Piece {
    public Rock(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public char getSymbol() {
        return isBlack ? 'R' : 'r';
    }
}
