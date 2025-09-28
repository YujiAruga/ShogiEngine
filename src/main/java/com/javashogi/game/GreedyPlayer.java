package com.javashogi.game;

import com.javashogi.board.*;

import java.util.Comparator;
import java.util.List;

public final class GreedyPlayer implements Player {
    @Override
    public Move selectMove(GameEngine engine) {
        List<Move> moves = engine.generateLegalMoves();
        if (moves.isEmpty()) return null;

        // Simple piece values
        java.util.Map<Class<? extends Piece>, Integer> value = java.util.Map.of(
                King.class, 10000, Rook.class, 900, Bishop.class, 800,
                GoldGeneral.class, 700, SilverGeneral.class, 600,
                Knight.class, 500, Lance.class, 400, Pawn.class, 100
        );

        return moves.stream()
                .max(Comparator.comparingInt(m -> captureValue(engine, m, value)))
                .orElse(moves.get(0));
    }

    private int captureValue(GameEngine engine, Move move, java.util.Map<Class<? extends Piece>, Integer> value) {
        var b = engine.board();
        if (move.drop) return 0;
        var target = b.getPiece(move.toRow, move.toCol);
        return (target == null) ? 0 : value.getOrDefault(target.getClass(), 0);
    }
}
