package com.javashogi.game;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class RandomPlayer implements Player {
    @Override
    public Move selectMove(GameEngine engine) {
        List<Move> moves = engine.generateLegalMoves();
        if (moves.isEmpty()) return null;
        return moves.get(ThreadLocalRandom.current().nextInt(moves.size()));
    }
}
