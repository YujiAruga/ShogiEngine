package com.javashogi.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.javashogi.testutil.TestBoardUtils;

import static org.junit.jupiter.api.Assertions.*;

public class DropRulesTest {

    private Board board;

    @BeforeEach
    void setup() {
        board = new Board();
        TestBoardUtils.clearBoard(board);
    }

    @Test
    void dropPawn_basic_ok() {
        board.addToHandForTest(new Pawn(true));
        assertTrue(board.dropPiece(Pawn.class, 4, 4, true));
        assertTrue(board.getPiece(4, 4) instanceof Pawn);
        assertTrue(board.getPiece(4, 4).isBlack());
        assertFalse(board.getPiece(4, 4).isPromoted());
        assertEquals(0, board.getHand(true).size());
    }

    @Test
    void dropIntoOccupied_isRejected() {
        board.addToHandForTest(new Rook(true));
        TestBoardUtils.setPiece(board, 4, 4, new Rook(false));
        assertFalse(board.dropPiece(Rook.class, 4, 4, true));
    }

    @Test
    void nifu_preventsPawnDropOnSameFile() {
        // black already has an unpromoted pawn on file 4
        TestBoardUtils.setPiece(board, 6, 4, new Pawn(true));
        board.addToHandForTest(new Pawn(true));
        assertFalse(board.dropPiece(Pawn.class, 3, 4, true));
    }

    @Test
    void promotedPawnOnBoardDoesNotCountForNifu() {
        Pawn p = new Pawn(true);
        p.promote();
        TestBoardUtils.setPiece(board, 6, 4, p);
        board.addToHandForTest(new Pawn(true));
        assertTrue(board.dropPiece(Pawn.class, 3, 4, true)); // allowed: promoted pawn doesn't count
    }

    @Test
    void pawnAndLance_cannotDropOnLastRank() {
        board.addToHandForTest(new Pawn(true));
        board.addToHandForTest(new Lance(true));
        assertFalse(board.dropPiece(Pawn.class, 0, 4, true));
        assertFalse(board.dropPiece(Lance.class, 0, 5, true));
    }

    @Test
    void knight_cannotDropOnLastTwoRanks() {
        board.addToHandForTest(new Knight(true));
        assertFalse(board.dropPiece(Knight.class, 0, 4, true));
        assertFalse(board.dropPiece(Knight.class, 1, 4, true));
        // but row 2 is fine
        board.addToHandForTest(new Knight(true));
        assertTrue(board.dropPiece(Knight.class, 2, 4, true));
    }

    @Test
    void rook_dropAnywhereEmpty_ok() {
        board.addToHandForTest(new Rook(true));
        assertTrue(board.dropPiece(Rook.class, 8, 8, true));
        assertTrue(board.getPiece(8, 8) instanceof Rook);
    }

    @Test
    void dropFailsIfNotInHand() {
        assertFalse(board.dropPiece(SilverGeneral.class, 4, 4, true)); // no silver in hand → false
    }
}
