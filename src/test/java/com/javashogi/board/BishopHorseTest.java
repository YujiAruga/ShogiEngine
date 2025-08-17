package com.javashogi.board;

import com.javashogi.testutil.TestBoardUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BishopHorseTest {

    private Board board;

    @BeforeEach
    void setup() {
        this.board = new Board();
        TestBoardUtils.clearBoard(board);
    }

    @Test
    void bishopDiagonalSlide_clearPath_isValid() {
        Piece bishop = new Bishop(true);
        TestBoardUtils.setPiece(board, 4, 4, bishop);

        assertTrue(bishop.isValidMove(4, 4, 7, 7, board));
        assertTrue(bishop.isValidMove(4, 4, 1, 1, board));
    }

    @Test
    void bishopCannotLandOnOwnPiece() {
        Piece bishop = new Bishop(true);
        TestBoardUtils.setPiece(board, 4, 4, bishop);
        TestBoardUtils.setPiece(board, 6, 6, new Pawn(true));

        assertFalse(bishop.isValidMove(4, 4, 6, 6, board));
    }

    @Test
    void bishopCapturesEnemyOnDestination() {
        Piece bishop = new Bishop(true);
        TestBoardUtils.setPiece(board, 2, 2, bishop);
        TestBoardUtils.setPiece(board, 5, 5, new Pawn(false));

        assertTrue(bishop.isValidMove(2, 2, 5, 5, board));
    }

    @Test
    void promotedBishopHorse_oneStepOrthogonal_isValid() {
        Bishop bishop = new Bishop(true);
        bishop.promote();
        TestBoardUtils.setPiece(board, 4, 4, bishop);

        assertTrue(bishop.isValidMove(4, 4, 4, 5, board));
        assertTrue(bishop.isValidMove(4, 4, 3, 4, board));
        assertTrue(bishop.isValidMove(4, 4, 5, 4, board));
        assertTrue(bishop.isValidMove(4, 4, 4, 3, board));
    }

    @Test
    void promotedBishopHorse_twoStepOrthogonal_isInvalid() {
        Bishop bishop = new Bishop(true);
        bishop.promote();
        TestBoardUtils.setPiece(board, 4, 4, bishop);

        assertFalse(bishop.isValidMove(4, 4, 4, 6, board));
    }
}
