package com.javashogi.board;

public final class PieceFactory {
    private PieceFactory() {}

    public static Piece create(PieceType type, boolean isBlack, boolean promoted) {
        Piece piece = switch (type) {
            case PAWN -> new Pawn(isBlack);
            case LANCE -> new Lance(isBlack);
            case KNIGHT -> new Knight(isBlack);
            case SILVER -> new SilverGeneral(isBlack);
            case GOLD -> new GoldGeneral(isBlack);
            case BISHOP -> new Bishop(isBlack);
            case ROOK -> new Rook(isBlack);
            case KING -> new King(isBlack);
        };

        if (promoted && !(piece instanceof King) && !(piece instanceof GoldGeneral)) piece.promote();

        return piece;
    }


    // Uppercase = black, lowercase = white. '+X' means promoted X.
    public static Piece fromSfenChar(char ch) {
        boolean promoted = false;
        if (ch == '+') throw new IllegalArgumentException("Standalone '+' not allowed");
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
            case 'K' -> PieceType.KING;
            default -> throw new IllegalArgumentException("Unknown SFEN piece: " + ch);
        };

        // promoted handled by caller (when it sees '+')
        return create(type, isBlack, false);
    }

    public static char toSfenChar(Piece piece) {
        char base = (piece instanceof Pawn) ? 'P'
                  : (piece instanceof Lance) ? 'L'
                  : (piece instanceof Knight) ? 'K'
                  : (piece instanceof SilverGeneral) ? 'S'
                  : (piece instanceof GoldGeneral) ? 'G'
                  : (piece instanceof Bishop) ? 'B'
                  : (piece instanceof Rook) ? 'R'
                  : (piece instanceof King) ? 'K'
                  : '?';

        if (!piece.isBlack()) base = Character.toLowerCase(base);
        if (piece.isPromoted() && !(piece instanceof King) && !(piece instanceof GoldGeneral)) {
            // SFEN uses '+' prefix for promoted pieces
            // We'll return a sentinel here; encoder will handle '+' + char
        }

        return base;
    }

    public static PieceType typeOf(Piece piece) {
        if (piece instanceof Pawn) return PieceType.PAWN;
        if (piece instanceof Lance) return PieceType.LANCE;
        if (piece instanceof Knight) return PieceType.KNIGHT;
        if (piece instanceof SilverGeneral) return PieceType.SILVER;
        if (piece instanceof GoldGeneral) return PieceType.GOLD;
        if (piece instanceof Bishop) return PieceType.BISHOP;
        if (piece instanceof Rook) return PieceType.ROOK;
        if (piece instanceof King) return PieceType.KING;
        throw new IllegalArgumentException("Unknown piece: " + piece.getClass());
    }
}
