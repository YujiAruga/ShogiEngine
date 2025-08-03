package com.javashogi.board;

public class Knight extends Piece {
    public Knight(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public char getSymbol() {
        return isBlack ? 'K' : 'k';
    }
}
