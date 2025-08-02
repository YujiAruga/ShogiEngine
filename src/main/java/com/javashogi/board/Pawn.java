package com.javashogi.board;

public class Pawn extends Piece {
    public Pawn(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public char getSymbol() {
        return isBlack ? '1' : '2';
    }
}
