package com.javashogi.game;

import com.javashogi.board.*;
import com.javashogi.testutil.TestBoardUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UchifuzumeTest {

    private Board board;
    private GameEngine engine;

    @BeforeEach
    void setup() {
        board = new Board();
        TestBoardUtils.clearBoard(board);
        engine = new GameEngine(board);
    }

    @Test
    void pawnDropMate_isRejected_byRules() {
        // While king boxed on top row, black covers all escape & capture squares
        // Coordinates (r, c) : 0 = top row. Black pawns move UP (r-1)
        // King at (0, 4), white pieces block side squares, black silver covers (1, 3) (1, 4) (1, 5)
        TestBoardUtils.setPiece(board, 0, 4, new King(false));
        TestBoardUtils.setPiece(board, 0, 3, new Pawn(false));
        TestBoardUtils.setPiece(board, 0, 5, new Pawn(false));
        TestBoardUtils.setPiece(board, 8, 4, new King(true));
        TestBoardUtils.setPiece(board, 2, 4, new SilverGeneral(true));

        // Black has a pawn in hand
        board.addToHandForTest(new Pawn(true));

        // Dropping a pawn at (1, 4) gives immediate mate (king cannot capture or move) -> ILLEGAL
        assertFalse(engine.drop(Pawn.class, 1, 4),"Uchifuzume: pawn-drop mate must be rejected");
    }

    @Test
    void pawnDropMate_isRejected_byRule() {
        // White king boxed at top, side squares blocked by own pawns
        TestBoardUtils.setPiece(board, 0, 4, new King(false));
        TestBoardUtils.setPiece(board, 0, 3, new Pawn(false));
        TestBoardUtils.setPiece(board, 0, 5, new Pawn(false));

        // Black pieces ensure the pawn drop is true mate:
        // Rook defends (1, 4) so KxP is illegal (king would be captured)
        TestBoardUtils.setPiece(board, 3, 4, new Rook(true));
        // Golds cover (1, 3) and (1, 5) so king can't sidestep
        TestBoardUtils.setPiece(board, 2, 3, new GoldGeneral(true));
        TestBoardUtils.setPiece(board, 2, 5, new GoldGeneral(true));
        TestBoardUtils.setPiece(board, 8, 4, new King(true));

        // Black has a pawn in hand
        board.addToHandForTest(new Pawn(true));

        // Dropping a pawn at (1, 4) gives immediate mate -> uchifuzume => must be rejected
        assertFalse(engine.drop(Pawn.class, 1, 4), "Uchifuzume: pawn-drop mate must be rejected");
    }


    @Test
    void checkingPawnDrop_that_is_not_mate_is_allowed() {
        // Same shell but remove the silver so the king can capture the dropped pawn.
        TestBoardUtils.setPiece(board, 0, 4, new King(false));
        TestBoardUtils.setPiece(board, 0, 3, new Pawn(false));
        TestBoardUtils.setPiece(board, 0, 5, new Pawn(false));
        TestBoardUtils.setPiece(board, 8, 4, new King(true));

        board.addToHandForTest(new Pawn(true));

        // The king can capture (1, 4), so it's NOT mate -> allowed
        assertTrue(engine.drop(Pawn.class, 1, 4), "Non-mating pawn drop should be allowed");
    }


}
