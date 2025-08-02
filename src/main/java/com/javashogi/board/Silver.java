package com.javashogi.board;

public class Silver extends Piece {
    public Silver(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public char getSymbol() {
        return isBlack ? 'S' : 's';
    }
}
