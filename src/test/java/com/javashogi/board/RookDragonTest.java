package com.javashogi.board;

import com.javashogi.board.Board;
import com.javashogi.testutil.TestBoardUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RookDragonTest {

    private Board board;

    @BeforeEach
    void setup() {
        board = new Board();
        TestBoardUtils.clearBoard(board);
    }

    @Test
    void rookOrthogonalSlide_clearPath_isValid() {
        Piece rook = new Rook(true);
        TestBoardUtils.setPiece(board, 4, 4, rook);

        assertTrue(rook.isValidMove(4, 4, 4, 8, board));
        assertTrue(rook.isValidMove(4, 4, 0, 4, board));
    }

    @Test
    void rookOrthogonalSlide_blocked_isInvalid() {
        Piece rook = new Rook(true);
        TestBoardUtils.setPiece(board, 4, 4, rook);
        TestBoardUtils.setPiece(board, 4, 6, new Pawn(false));

        assertFalse(rook.isValidMove(4, 4, 4, 8, board));
    }

    @Test
    void rookCannotLandOnOwnPiece() {
        Piece rook = new Rook(true);
        TestBoardUtils.setPiece(board, 4, 4, rook);
        TestBoardUtils.setPiece(board, 4, 7, new Pawn(true));

        assertFalse(rook.isValidMove(4, 4, 4, 7, board));
    }

    @Test
    void rookCapturesEnemyOnDestination() {
        Piece rook = new Rook(true);
        TestBoardUtils.setPiece(board, 4, 4, rook);
        TestBoardUtils.setPiece(board, 4, 7, new Pawn(false));

        assertTrue(rook.isValidMove(4, 4, 4, 7, board));
    }

    @Test
    void unpromotedRookDiagonal_isInvalid() {
        Piece rook = new Rook(true);
        TestBoardUtils.setPiece(board, 4, 4, rook);

        assertFalse(rook.isValidMove(4, 4, 5, 5, board));
    }

    @Test
    void promotedRookDragon_oneStepDiagonal_isValid() {
        Rook rook = new Rook(true);
        rook.promote();
        TestBoardUtils.setPiece(board, 4, 4, rook);

        assertTrue(rook.isValidMove(4, 4, 5, 5, board));
        assertTrue(rook.isValidMove(4, 4, 3, 3, board));
        assertTrue(rook.isValidMove(4, 4, 5, 3, board));
        assertTrue(rook.isValidMove(4, 4, 3, 5, board));
    }

    @Test
    void promotedRookDragon_twoStepDiagonal_isInvalid() {
        Rook rook = new Rook(true);
        rook.promote();
        TestBoardUtils.setPiece(board, 4, 4, rook);

        assertFalse(rook.isValidMove(4, 4, 6, 6, board));
    }
}
