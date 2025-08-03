package com.javashogi.board;

public class GoldGeneral extends Piece {
    public GoldGeneral(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public char getSymbol() {
        return isBlack ? 'G' : 'g';
    }
}
