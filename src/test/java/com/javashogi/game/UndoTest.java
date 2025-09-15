package com.javashogi.game;

import com.javashogi.board.*;
import com.javashogi.testutil.TestBoardUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UndoTest {

    private Board board;
    private GameEngine engine;

    @BeforeEach
    void setup() {
        board = new Board();
        TestBoardUtils.clearBoard(board);
        engine = new GameEngine(board);
    }

    @Test
    void moveWhileCapture_thenUndo_restoresBoardAndHands() {
        // Black bishop captures white pawn on a clear diagonal.
        TestBoardUtils.setPiece(board, 4, 4, new Bishop(true));
        TestBoardUtils.setPiece(board, 6, 6, new Pawn(false));
        TestBoardUtils.setPiece(board, 8, 4, new King(true));
        //TestBoardUtils.setPiece(board, 0, 4, new King(false));

        // Ensure path is clear
        assertNull(TestBoardUtils.getPiece(board, 5, 5));

        assertTrue(engine.makeMove(4, 4, 6, 6, false));

        // After move, bishop at (6, 6), black hand contains a (black) pawn
        Piece after = board.getPiece(6, 6);
        assertTrue(after instanceof Bishop && after.isBlack());
        assertEquals(1, board.getHand(true).size());
        assertTrue(board.getHand(true).get(0) instanceof Pawn);

        // Undo
        assertTrue(engine.undo());


        // Board restored
        Piece backBishop = board.getPiece(4, 4);
        assertTrue(backBishop instanceof Bishop && backBishop.isBlack());
        Piece restoredPawn = board.getPiece(6, 6);
        assertTrue(restoredPawn instanceof Pawn && !restoredPawn.isBlack());

        // Hand emptied
        assertEquals(0, board.getHand(true).size());
    }

    @Test
    void drpp_thenUndo_restoresHandAndClearsSquare() {
        // Black has a knight in hand
        board.addToHandForTest(new Knight(true));
        TestBoardUtils.setPiece(board, 8, 4, new King(true));

        assertTrue(engine.drop(Knight.class, 2, 4));
        Piece dropped = board.getPiece(2, 4);
        assertTrue(dropped instanceof Knight && dropped.isBlack());
        assertEquals(0, board.getHand(true).size());

        // Undo
        assertTrue(engine.undo());
        assertNull(board.getPiece(2, 4));
        assertEquals(1, board.getHand(true).size());
        assertTrue(board.getHand(true).get(0) instanceof Knight);
    }

    @Test
    void forcedPromotion_thenUndo_restoresPromotionState() {
        // Black pawn at row 1 moves to row 0 -> forced promoted
        TestBoardUtils.setPiece(board, 1, 4, new Pawn(true));
        TestBoardUtils.setPiece(board, 8, 4, new King(true));
        assertTrue(engine.makeMove(1, 4, 0, 4, false)); // promote forced inside Board

        Piece piece = board.getPiece(0, 4);
        assertTrue(piece instanceof Pawn && piece.isPromoted(), "Pawn should be promoted after forced move");

        // Undo: should restore unpromoted pawn at original square
        assertTrue(engine.undo());
        Piece restored = board.getPiece(1, 4);
        assertTrue(restored instanceof Pawn && restored.isBlack() && !restored.isPromoted());
        assertNull(board.getPiece(0, 4));
    }
}
