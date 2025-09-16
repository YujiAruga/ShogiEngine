package com.javashogi.game;

import com.javashogi.board.Board;
import com.javashogi.board.King;
import com.javashogi.board.Pawn;
import com.javashogi.board.Piece;

import java.util.*;

public class GameEngine {
    private final Board board;
    private boolean blackToMove = true;

    // Move history
    private final Deque<MoveRecord> history = new ArrayDeque<>();

    public GameEngine(Board board) { this.board = board; }
    public boolean isBlackToMove() { return blackToMove; }
    public Board board() { return board; }

    public boolean makeMove(int fromRow, int fromCol, int toRow, int toCol, boolean promoteIfAvailable) {
        Piece piece = board.getPiece(fromRow, fromCol);
        if (piece == null || piece.isBlack() != blackToMove) return false;

        // Simulate on a copy
        Board temp = board.deepCopy();
        if (!temp.applyMoveUnchecked(fromRow, fromCol, toRow, toCol, promoteIfAvailable)) return false;

        // check self-check
        if (temp.isInCheck(piece.isBlack())) return false;

        // Record for undo
        Piece targetBefore = board.getPiece(toRow, toCol);
        boolean promotedBefore = piece.isPromoted();

        if (!board.applyMoveUnchecked(fromRow, fromCol, toRow, toCol, promoteIfAvailable)) return false;

        boolean promotedAfter = board.getPiece(toRow, toCol).isPromoted();
        history.push(MoveRecord.moved(
                piece.getClass(), piece.isBlack(),
                fromRow, fromCol, toRow, toCol,
                promotedBefore, promotedAfter,
                targetBefore == null ? null : targetBefore.getClass(),
                targetBefore == null ? false : targetBefore.isBlack(),
                targetBefore != null && targetBefore.isPromoted()
        ));

        // If this turn was Black, the next turn will be White and vice versa
        blackToMove = !blackToMove;
        return true;
    }

    public boolean drop(Class<? extends Piece> type, int row, int col) {
        boolean isBlack = blackToMove;

        // Simulate
        Board temp = board.deepCopy();
        if (!temp.dropUnchecked(type, row, col, isBlack)) return false;
        // cannot drop leaving own king in check
        if (temp.isInCheck(isBlack)) return false;

        if (type == Pawn.class) {
            boolean opp = !isBlack;
            if (temp.isInCheck(opp) && isPawnDropMate(temp, row, col, opp)) {
                return false; // forbid pawn-drop mate
            }
        }
        if (!board.dropUnchecked(type, row, col, blackToMove)) return false;
        history.push(MoveRecord.drop(type, isBlack, row, col));

        // If this turn was Black, the next turn will be White and vice versa
        blackToMove = !blackToMove;

        return true;
    }

    // Returns true if opponent has NO legal reply to the check delivered by the pawn drop.
    private boolean isPawnDropMate(Board board, int pawnRow, int pawnCol, boolean defenderIsBlack) {
        // case 1: can the king move to any adjacent square to escape
        int[] king = board.findKing(defenderIsBlack);
        int kingRow = king[0], kingCol = king[1];
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int nr = kingRow + dr, nc = kingCol + dc;
                if (!board.inBounds(nr, nc)) continue;
                Piece k = board.getPiece(kingRow, kingCol);
                if (!(k instanceof King)) continue;

                Board simulation = board.deepCopy();
                // Try king move
                if (simulation.applyMoveUnchecked(kingRow, kingCol, nr, nc, false) && !simulation.isInCheck(defenderIsBlack)) {
                    return false; // has a legal king-escape
                }
            }
        }

        // case 2: can any defender piece capture the dropped pawn (and be legal)?
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Piece piece = board.getPiece(r, c);
                if (piece == null || piece.isBlack() != defenderIsBlack) continue;
                if (!piece.isValidMove(r, c, pawnRow, pawnCol, board)) continue;

                Board simulation = board.deepCopy();
                if (simulation.applyMoveUnchecked(r, c, pawnRow, pawnCol, false) && !simulation.isInCheck(defenderIsBlack)) {
                    return false; // can capture the pawn safely
                }
            }
        }

        // Note: blocking a pawn check by drop is impossible (drops can't capture)
        return true;
    }


    public boolean undo() {
        if (history.isEmpty()) return false;
        MoveRecord previousMove = history.pop();
        blackToMove = !blackToMove;

        if (previousMove.drop) {
            // remove dropped piece from board and add back to hand
            board.removePiece(previousMove.toRow, previousMove.toCol);
            board.addToHandRow(previousMove.moverBlack, board.makePiece(previousMove.moverType, previousMove.moverBlack, false));
            return true;
        }
        else {
            // move piece back
            board.removePiece(previousMove.toRow, previousMove.toCol);
            Piece mover = board.makePiece(previousMove.moverType, previousMove.moverBlack, previousMove.promotedBefore);
            board.setPiece(previousMove.fromRow, previousMove.fromCol, mover);

            // restore captured piece (if any) and remove from hand of mover
            if (previousMove.capturedType != null) {
                board.removeFromHand(previousMove.moverBlack, previousMove.capturedType);
                Piece captured = board.makePiece(previousMove.capturedType, previousMove.capturedBlack, previousMove.capturedPromoted);
                board.setPiece(previousMove.toRow, previousMove.toCol, captured);
            }

            return true;
        }
    }

    /**
     * Generate ALL legal moves for side to move
     */
    public List<Move> generateLegalMoves() { return generateLegalMoves(blackToMove); }

    // Generate ALL legal moves for the given side,
    public List<Move> generateLegalMoves(boolean forBlack) {
        List<Move> out = new ArrayList<>();

        // for normal moves
        for (int row = 0; row < 9; row++) for (int col = 0; col < 9; col++) {
            Piece piece = board.getPiece(row, col);
            if (piece == null || piece.isBlack() != forBlack) continue;

            for (int toRow = 0; toRow < 9; toRow++) for (int toCol = 0; toCol < 9; toCol++) {
                if (!piece.isValidMove(row, col, toRow, toCol, board)) continue;

                boolean mustPromote = board.mustPromote(piece, toRow);
                boolean canPromote = board.canPromote(piece, row, toRow);

                if (mustPromote) {
                    if (legalAfterSim(Move.move(row, col, toRow, toCol, true), forBlack)) out.add(Move.move(row, col, toRow, toCol, true));
                }
                else if (canPromote) {
                    if (legalAfterSim(Move.move(row, col, toRow, toCol, false), forBlack)) out.add(Move.move(row, col, toRow, toCol, false));
                    if (legalAfterSim(Move.move(row, col, toRow, toCol, true), forBlack)) out.add(Move.move(row, col, toRow, toCol, true));
                }
                else {
                    if (legalAfterSim(Move.move(row, col, toRow, toCol, false), forBlack)) out.add(Move.move(row, col, toRow, toCol, false));
                }
            }
        }

        // drops (dedupe types in hand to avoid identical moves twice)
        Map<Class<? extends Piece>, Integer> handCounts = new HashMap<>();
        for (Piece hand : board.getHand(forBlack)) {
            handCounts.merge(hand.getClass(), 1, Integer::sum);
        }

        for (Class<? extends Piece> type : handCounts.keySet()) {
            for (int row = 0; row < 9; row++) for (int col = 0; col < 9; col++) {
                if (!board.isEmpty(row, col)) continue;
                // Simulate drop to see if geometrically legal & not self-check
                Move drop = Move.drop(type, row, col);
                if (legalAfterSim(drop, forBlack)) out.add(drop);
            }
        }

        return out;
    }



    /** True if applying 'm' for 'forBlack' on a copy is legal (not leaving own king in check, & passes rules).**/
    private boolean legalAfterSim(Move move, boolean forBlack) {
        Board tmp = board.deepCopy();
        boolean ok = move.drop
                ? tmp.dropUnchecked(move.dropType, move.toRow, move.toCol, forBlack)
                : tmp.applyMoveUnchecked(move.fromRow, move.fromCol, move.toRow, move.toCol, move.promote);
        if (!ok) return false;

        // No self-check
        if (tmp.isInCheck(forBlack)) return false;

        // Uchifuzume: reject pawn drop mate
        if (move.drop && move.dropType == Pawn.class) {
            boolean opp = !forBlack;
            if (tmp.isInCheck(opp) && isPawnDropMate(tmp, move.toRow, move.toCol, opp)) return false;
        }

        return true;
    }

    /** Checkmate if side is currently in check AND has no legal replies. */
    public boolean isCheckmate(boolean forBlack) {
        if (!board.isInCheck(forBlack)) return false;
        return generateLegalMoves(forBlack).isEmpty();
    }

    /** Evaluate simple result */
    public GameResult getResult() {
        if (isCheckmate(blackToMove)) {
            return blackToMove ? GameResult.CHECKMATE_WHITE_WINS : GameResult.CHECKMATE_BLACK_WINS;
        }
        return GameResult.ONGOING;
    }





    // ==== move record ====
    private static final class MoveRecord {
        final boolean drop;
        final Class<? extends Piece> moverType;
        final boolean moverBlack;
        final int fromRow, fromCol, toRow, toCol;
        final boolean promotedBefore, promotedAfter;
        final Class<? extends Piece> capturedType;
        final boolean capturedBlack;
        final boolean capturedPromoted;

        private MoveRecord(boolean drop, Class<? extends Piece> moverType, boolean moverBlack,
                           int fromRow, int fromCol, int toRow, int toCol, boolean promotedBefore,
                           boolean promotedAfter, Class<? extends Piece> capturedType, boolean capturedBlack, boolean capturedPromoted) {
            this.drop = drop;
            this.moverType = moverType;
            this.moverBlack = moverBlack;
            this.fromRow = fromRow; this.fromCol = fromCol; this.toRow = toRow; this.toCol = toCol;
            this.promotedBefore = promotedBefore; this.promotedAfter = promotedAfter;
            this.capturedType = capturedType; this.capturedBlack = capturedBlack; this.capturedPromoted = capturedPromoted;
        }

        static MoveRecord moved(Class<? extends Piece> type, boolean black,
                                int fromRow, int fromCol, int toRow, int toCol,
                                boolean promotedBefore, boolean promotedAfter,
                                Class<? extends Piece> capturedType, boolean capturedBlack, boolean capturedPromoted) {
            return new MoveRecord(false, type, black, fromRow, fromCol, toRow, toCol, promotedBefore, promotedAfter, capturedType, capturedBlack, capturedPromoted);
        }

        static MoveRecord drop(Class<? extends Piece> type, boolean black, int row, int col) {
            return new MoveRecord(true, type, black, -1, -1, row, col, false, false, null, false, false);
        }
    }
}
