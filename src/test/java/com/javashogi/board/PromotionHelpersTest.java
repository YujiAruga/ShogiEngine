package com.javashogi.board;

import com.javashogi.board.*;
import com.javashogi.testutil.TestBoardUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PromotionHelpersTest {

    private Board board;

    @BeforeEach
    void setup() {
        board = new Board();
        TestBoardUtils.clearBoard(board);
    }

    @Test
    void inPromotionZone_blackTopThree_whiteBottomThree() {
        // Black moves up
        assertTrue(board.inPromotionZone(0, true));
        assertTrue(board.inPromotionZone(1, true));
        assertTrue(board.inPromotionZone(2, true));
        assertFalse(board.inPromotionZone(3, true));
        assertFalse(board.inPromotionZone(6, true));

        // White moves down
        assertTrue(board.inPromotionZone(6, false));
        assertTrue(board.inPromotionZone(7, false));
        assertTrue(board.inPromotionZone(8, false));
        assertFalse(board.inPromotionZone(5, false));
        assertFalse(board.inPromotionZone(2, false));
    }

    @Test
    void mustPromote_pawnAndLance_onLastRankOnly() {
        Pawn blackPawn = new Pawn(true);
        Lance blackLance = new Lance(true);
        assertTrue(board.mustPromote(blackPawn, 0));
        assertTrue(board.mustPromote(blackLance, 0));
        assertFalse(board.mustPromote(blackPawn, 1));
        assertFalse(board.mustPromote(blackLance, 1));

        Pawn whitePawn = new Pawn(false);
        Lance whiteLance = new Lance(false);
        assertTrue(board.mustPromote(whitePawn, 8));
        assertTrue(board.mustPromote(whiteLance, 8));
        assertFalse(board.mustPromote(whitePawn, 7));
        assertFalse(board.mustPromote(whiteLance, 7));
    }

    @Test
    void mustPromote_knight_onLastTwoRanks() {
        Knight blackKnight = new Knight(true);
        assertTrue(board.mustPromote(blackKnight, 0));
        assertTrue(board.mustPromote(blackKnight, 1));
        assertFalse(board.mustPromote(blackKnight, 2));

        Knight whiteKnight = new Knight(false);
        assertTrue(board.mustPromote(whiteKnight, 8));
        assertTrue(board.mustPromote(whiteKnight, 7));
        assertFalse(board.mustPromote(whiteKnight, 6));
    }

    @Test
    void canPromote_whiteMoveStartsOrEndsInZone() {
        Rook blackRook = new Rook(true);
        assertTrue(board.canPromote(blackRook, 2, 3)); // starts in zone
        assertTrue(board.canPromote(blackRook, 3, 2)); // ends in zone
        assertFalse(board.canPromote(blackRook, 3, 3)); // neither

        Bishop whiteBishop = new Bishop(false);
        assertTrue(board.canPromote(whiteBishop, 6, 5));
        assertTrue(board.canPromote(whiteBishop, 5, 6));
        assertFalse(board.canPromote(whiteBishop, 5, 5));
    }
}
