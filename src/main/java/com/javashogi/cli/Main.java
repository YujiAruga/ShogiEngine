package com.javashogi.cli;

import com.javashogi.board.*;
import com.javashogi.game.*;
import com.javashogi.io.SFEN;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        GameEngine engine = new GameEngine(board);
        Scanner in = new Scanner(System.in);

        System.out.println("JavaShogi CLI");
        // help();

        while (true) {
            System.out.println();
            printBoardAndHands(board);
            System.out.println("Turn: " + (engine.isBlackToMove() ? "Black (Sente)" : "White (Gote)"));
            System.out.print("> ");

            if (!in.hasNextLine()) break;
            String line = in.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] t = line.split("\\s+");
            String cmd = t[0].toLowerCase();

            try {
                switch (cmd) {
                    case "h", "help" -> help();
                    case "q", "quit", "exit" -> { System.out.println("Bye!"); return; }

                    case "board" -> printBoardAndHands(board);

                    case "legal" -> {
                        List<Move> moves = engine.generateLegalMoves();
                        System.out.println("Legal moves (" + moves.size() + "):");
                        for (Move move : moves) System.out.println(" " + move);
                    }

                    // move row col toRow toCol [ + ]
                    case "m", "move" -> {
                        if (t.length < 5) { System.out.println("Usage: move fromRow fromCol toRow toCol [+]"); break; }
                        int fromRow = Integer.parseInt(t[1]);
                        int fromCol = Integer.parseInt(t[2]);
                        int toRow = Integer.parseInt(t[3]);
                        int toCol = Integer.parseInt(t[4]);
                        boolean promote = t.length >= 6 && (t[5].equals("+") || t[5].equalsIgnoreCase("true"));
                        boolean ok = engine.makeMove(fromRow, fromCol, toRow, toCol, promote);
                        if (!ok) System.out.println("Illegal move.");
                        else showResult(engine);
                    }

                    case "d", "drop" -> {
                        if (t.length < 4) { System.out.println("Usage: drop <P|L|N|S|G|B|R> row col"); break; }
                        Class<? extends Piece> klass = switch (t[1].toUpperCase()) {
                            case "P" -> Pawn.class;
                            case "L" -> Lance.class;
                            case "N" -> Knight.class;
                            case "S" -> SilverGeneral.class;
                            case "G" -> GoldGeneral.class;
                            case "B" -> Bishop.class;
                            case "R" -> Rook.class;
                            default -> null;
                        };
                        if (klass == null) { System.out.println("Unknown piece: " + t[1]); break; }
                        int r = Integer.parseInt(t[2]), c0 = Integer.parseInt(t[3]);
                        boolean ok = engine.drop(klass, r, c0);
                        if (!ok) System.out.println("Illegal drop.");
                        else showResult(engine);
                    }

                    case "undo" -> {
                        if (!engine.undo()) System.out.println("Nothing to undo.");
                    }

                    case "sfen" -> {
                        String sfen = SFEN.encode(board, engine.isBlackToMove());
                        System.out.println(sfen);
                    }

                    case "load" -> {
                        // Usage: load <sfen...>
                        String sfen = line.substring(cmd.length()).trim();
                        var pos = SFEN.decode(sfen);
                        // swap in new board/engine
                        Board newBoard = pos.board;
                        engine = new GameEngine(newBoard);
                        if (pos.blackToMove != engine.isBlackToMove()) {
                            // quick hack : set engine to side
                            if (!pos.blackToMove) engine.undo(); // depending on your engine, add a setter in real code
                        }
                        board = newBoard;
                    }

                    default -> System.out.println("Unknown command. Type 'help'.");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    private static void help() {
        System.out.println("""
                Commands:
                board                                    - print board + hands
                move fromRow fromCol toRow toCol [+]     - make a move (0-based rows/cols), optional '+' for promote
                drop Piece row col                       - drop a piece from hand (P, L, N, S, G, B, R)
                legal                                    - list legal moves for the side to move
                undo                                     - undo last move/drop
                sfen                                     - print SFEN-like string of the current position
                load <sfen>                              - load position from SFEN-like string
                help                                     - show this help
                quit                                     - exit
                """);
    }

    private static void printBoardAndHands(Board board) {
        board.printBoard();
        System.out.println("Black hand: " + board.handString(true));
        System.out.println("White hand: " + board.handString(false));
    }

    private static void showResult(GameEngine engine) {
        GameResult result = engine.getResult();
        if (result != GameResult.ONGOING) System.out.println("Result: " + result);
    }
}

