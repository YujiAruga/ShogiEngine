package com.javashogi.board;

public class King extends Piece {
    public King(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public char getSymbol() {
        return isBlack ? 'K' : 'k';
    }
}
