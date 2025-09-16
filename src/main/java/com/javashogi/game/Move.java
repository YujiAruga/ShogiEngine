package com.javashogi.game;

import com.javashogi.board.Piece;

public final class Move {
    public final boolean drop;
    public final int fromRow, fromCol, toRow, toCol;
    public final Class<? extends Piece> dropType; // for drops only
    public final boolean promote; // for moves only

    private Move(boolean drop, int fromRow, int fromCol, int toRow, int toCol, Class<? extends Piece> dropType, boolean promote) {
        this.drop = drop;
        this.fromRow = fromRow; this.fromCol = fromCol; this.toRow = toRow; this.toCol = toCol;
        this.dropType = dropType;
        this.promote = promote;
    }

    public static Move move(int fromRow, int fromCol, int toRow, int toCol, boolean promote) {
        return new Move(false, fromRow, fromCol, toRow, toCol, null, promote);
    }

    public static Move drop(Class<? extends Piece> dropType, int row, int col) {
        return new Move(true, -1, -1, row, col, dropType, false);
    }

    @Override public String toString() {
        return drop ? ("DROP " + dropType.getSimpleName() + "@" + toRow + "," + toCol)
                    : ("MOVE " + fromRow + "," + fromCol + "->" + toRow + "," + toCol + (promote ? "+" : ""));
    }

    @Override public boolean equals(Object object) {
        if (!(object instanceof Move move)) return false;
        return drop == move.drop &&
               fromRow == move.fromRow && fromCol == move.fromCol && toRow == move.toRow && toCol == move.toCol &&
               promote == move.promote &&
               dropType == move.dropType;
    }

    @Override public int hashCode() {
        int h = drop ? 1 : 0;
        h = 31*h + fromRow; h = 31*h + fromCol; h = 31*h + toRow; h = 31*h + toCol;
        h = 31*h + (promote ? 1 : 0);
        h = 31*h + (dropType == null ? 0 : dropType.hashCode());
        return h;
    }
}
