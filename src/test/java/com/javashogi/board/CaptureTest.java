package com.javashogi.board;

import com.javashogi.board.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.javashogi.testutil.TestBoardUtils;

import static org.junit.jupiter.api.Assertions.*;

public class CaptureTest {
    private Board board;

    @BeforeEach
    void setup() {
        board = new Board();
        TestBoardUtils.clearBoard(board);
    }

    @Test
    void capturingDemotesAndRecolorsToHand() {
        Piece bishop = new Bishop(true);
        Piece pawn = new Pawn(false);

        TestBoardUtils.clearBoard(board);
        TestBoardUtils.setPiece(board, 4, 4, bishop);
        TestBoardUtils.setPiece(board, 6, 6, pawn);

        assertTrue(board.movePiece(4, 4, 6, 6, false));
        assertEquals(bishop, board.getPiece(6, 6));
        assertNull(board.getPiece(4, 4));

        assertEquals(1, board.getHand(true).size());
        Piece inHand = board.getHand(true).get(0);
        assertTrue(inHand instanceof Pawn);
        assertTrue(inHand.isBlack());
        assertFalse(inHand.isPromoted());
    }
}
