package com.javashogi.board;

public class SilverGeneral extends Piece {
    public SilverGeneral(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public char getSymbol() {
        return isBlack ? 'S' : 's';
    }
}
