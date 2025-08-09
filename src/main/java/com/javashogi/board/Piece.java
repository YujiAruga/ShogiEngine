package com.javashogi.board;

public abstract class Piece {
    protected boolean isBlack; // if this is true, player is sente which means that this piece is the first player's piece.
    protected boolean promoted = false;

    public Piece(boolean isBlack) {
        this.isBlack = isBlack;
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void promote() {
        promoted = true;
    }

    public boolean isBlack() {
        return isBlack;
    }

    public abstract char getSymbol();

    public abstract boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Board board);
}
