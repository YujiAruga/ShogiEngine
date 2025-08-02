package com.javashogi.board;

public class Pawn extends Piece {
    public Pawn(boolean isBlack) {
        super(isBlack);
    }
    // In terminal command, we can not tell which pieces belong to which player.
    // So pieces with capital letter belong to Black (Sente) and others belong to White (gote)
    @Override
    public char getSymbol() {
        return isBlack ? 'P' : 'p';
    }
}
