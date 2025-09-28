package com.javashogi.game;

public interface Player {
    // Return a legal move or null if no moves available
    Move selectMove(GameEngine engine);
    default String name() { return getClass().getSimpleName(); }
}
