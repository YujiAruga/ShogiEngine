package com.javashogi.testutil;

import com.javashogi.board.Board;
import com.javashogi.board.Piece;
import java.lang.reflect.Field;

public class TestBoardUtils {

    private TestBoardUtils() {}

    @SuppressWarnings("unchecked")
    public static Piece[][] grid(Board board) {
        try {
            Field field = Board.class.getDeclaredField("board");
            field.setAccessible(true);
            return (Piece[][]) field.get(board);
        } catch (Exception e) {
            throw new RuntimeException("Could not access Board.board via reflection", e);
        }
    }

    public static void clearBoard(Board board) {
        Piece[][] realBoard = grid(board);
        for (int r = 0; r < realBoard.length; r++) {
            for (int c = 0; c < realBoard[r].length; c++) {
                realBoard[r][c] = null;
            }
        }
    }

    public static void setPiece(Board board, int row, int col, Piece piece) {
        grid(board)[row][col] = piece;
    }

    public static Piece getPiece(Board board, int row, int col) {
        return grid(board)[row][col];
    }
}
