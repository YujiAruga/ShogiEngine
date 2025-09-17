package com.javashogi.io;

import com.javashogi.board.*;
import java.util.*;

public final class SFEN {
    private SFEN() {}

    public static String encode(Board board, boolean blackToMove) {
        StringBuilder sb = new StringBuilder();

        // 1: Board
        for (int r = 0; r < 9; r++) {
            int empties = 0;
            for (int c = 0; c < 9; c++) {
                Piece piece = board.getPiece(r, c);
                if (piece == null) { empties++; continue; }
                if (empties > 0) { sb.append(empties); empties = 0; }
                char ch = PieceFactory.toSfenChar(piece);
                if (piece.isPromoted() && !(piece instanceof GoldGeneral) && !(piece instanceof King)) {
                    sb.append('+');
                }
                sb.append(ch);
            }

            if (empties > 0) sb.append(empties);
            if (r < 8) sb.append('/');
        }

        // 2: Side
        sb.append(' ').append(blackToMove ? 'b' : 'w').append(' ');

        // 3: Hands
        String hands = encodeHands(board);
        sb.append(hands.isEmpty() ? "-" : hands);

        // 4: ply (ignored)
        sb.append(" -");
        return sb.toString();
    }


    private static String encodeHands(Board board) {
        Map<Character, Integer> counts = new TreeMap<>();

        // Black hand uppercase
        for (Piece piece : board.getHand(true)) {
            if (piece instanceof King) continue;
            char ch = Character.toLowerCase(PieceFactory.toSfenChar(piece));
            counts.merge(ch, 1, Integer::sum);
        }

        // White hand lowercase
        for (Piece piece : board.getHand(false)) {
            if (piece instanceof King) continue;
            char ch = Character.toLowerCase(PieceFactory.toSfenChar(piece));
            counts.merge(ch, 1, Integer::sum);
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Character, Integer> entry : counts.entrySet()) {
            int n = entry.getValue();
            if (n > 1) sb.append(n);
            sb.append(entry.getKey());
        }

        return sb.toString();
    }


    /** Decode SFEN into a fresh Board; returns side to move as well. */
    public static SfenPosition decode(String sfen) {
        String[] parts = sfen.trim().split("\\s+");
        if (parts.length < 3) throw new IllegalArgumentException("SFEN must have at least 3 fields");
        String boardPart = parts[0];
        String sidePart = parts[1];
        String handPart = parts[2];

        Board board = new Board();
        board.resetForTest();

        // 1: Board
        String[] ranks = boardPart.split("/");
        if (ranks.length != 9) throw new IllegalArgumentException("SFEN must have 9 ranks");
        for (int r = 0; r < 9; r++) {
            int c = 0;
            for (int i = 0; i < ranks[r].length(); i++) {
                char ch = ranks[r].charAt(i);
                if (Character.isDigit(ch)) {
                    c += ch - '0';
                }
                else if (ch == '+') {
                    char next = ranks[r].charAt(++i);
                    Piece piece = PieceFactory.fromSfenChar(next);

                    // promoted flag
                    if (!(piece instanceof GoldGeneral) && !(piece instanceof King)) piece.promote();
                    board.setPiece(r, c++, piece);
                }
                else {
                    Piece piece = PieceFactory.fromSfenChar(ch);
                    board.setPiece(r, c++, piece);
                }
            }
            if (c != 9) throw new IllegalArgumentException("Rank " + r + "does not sum to 9 files");
        }

        // 2: Side
        boolean blackToMove = switch (sidePart) {
            case "b" -> true;
            case "w" -> false;
            default -> throw new IllegalArgumentException("Side must be 'b' or 'w'");
        };

        if (!handPart.equals("-")) {
            int i = 0, n = handPart.length();
            while (i < n) {
                int count = 0;
                while (i < n && Character.isDigit(handPart.charAt(i))) {
                    count = count * 10 + (handPart.charAt(i) - '0');
                    i++;
                }
                if (i >= n) break;
                char ch = handPart.charAt(i++);
                if (count == 0) count = 1;

                boolean isBlack = Character.isUpperCase(ch);
                char u = Character.toUpperCase(ch);
                PieceType type = switch (u) {
                    case 'P' -> PieceType.PAWN;
                    case 'L' -> PieceType.LANCE;
                    case 'N' -> PieceType.KNIGHT;
                    case 'S' -> PieceType.SILVER;
                    case 'G' -> PieceType.GOLD;
                    case 'B' -> PieceType.BISHOP;
                    case 'R' -> PieceType.ROOK;
                    case 'K' -> null; // ignore king in hand
                    default -> throw new IllegalArgumentException("Unknown hand piece: " + ch);
                };

                if (type != null) {
                    for (int k = 0; k < count; k++) {
                        board.addToHandRow(isBlack, PieceFactory.create(type, isBlack, false));
                    }
                }
            }
        }

        return new SfenPosition(board, blackToMove);
    }

    public static final class SfenPosition {
        public final Board board;
        public final boolean blackToMove;
        public SfenPosition(Board board, boolean blackToMove) {
            this.board = board; this.blackToMove = blackToMove;
        }
    }
}
