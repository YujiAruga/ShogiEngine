package com.javashogi.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board {
    @Override
    public String toString() {
        return "Empty Shogi board (placeholder)";
    }

    private Piece[][] board;
    private final List<Piece> blackHand = new ArrayList<>();
    private final List<Piece> whiteHand = new ArrayList<>();

    public Board() {
        this.board = new Piece[9][9];
        setupInitialPosition();
    }

    private void setupInitialPosition() {
        // These pieces initialization is for Gote (White) side

        // Row 0
        board[0][0] = new Lance(false);
        board[0][1] = new Knight(false);
        board[0][2] = new SilverGeneral(false);
        board[0][3] = new GoldGeneral(false);
        board[0][4] = new King(false);
        board[0][5] = new GoldGeneral(false);
        board[0][6] = new SilverGeneral(false);
        board[0][7] = new Knight(false);
        board[0][8] = new Lance(false);

        // Row 1
        board[1][1] = new Rook(false);
        board[1][7] = new Bishop(false);

        // Row 2
        for (int col = 0; col < 9; col++) {
            board[2][col] = new Pawn(false);
        }

        // These pieces initialization is for Sente (Black) side

        // Row 6
        for (int col = 0; col < 9; col++) {
            board[6][col] = new Pawn(true);
        }

        // Row 7
        board[7][1] = new Bishop(true);
        board[7][7] = new Rook(true);

        // Row 8
        board[8][0] = new Lance(true);
        board[8][1] = new Knight(true);
        board[8][2] = new SilverGeneral(true);
        board[8][3] = new GoldGeneral(true);
        board[8][4] = new King(true);
        board[8][5] = new GoldGeneral(true);
        board[8][6] = new SilverGeneral(true);
        board[8][7] = new Knight(true);
        board[8][8] = new Lance(true);
    }

    public boolean movePiece(int fromRow, int fromCol, int toRow, int toCol) {
//        Piece piece = board[fromRow][fromCol];
//
//        if (piece == null) {
//            System.out.println("No piece at source.");
//            return false;
//        }
//
//        if (!isValidMove(piece, fromRow, fromCol, toRow, toCol)) {
//            System.out.println("Invalid move for " + piece.getSymbol());
//            return false;
//        }

        // Capture if any piece at destination
        /*
        if (board[toRow][toCol] != null) {
            reserve.add(board[toRow][toCol])
        */

//        board[toRow][toCol] = piece;
//        board[fromRow][fromCol] = null;
//
//        return true;

        return movePiece(fromRow, fromCol, toRow, toCol, false);
    }

    public boolean movePiece(int fromRow, int fromCol, int toRow, int toCol, boolean promoteIfAvailable) {
        if (!inBounds(fromRow, fromCol) || !inBounds(toRow, toCol)) return false;
        Piece p = getPiece(fromRow, fromCol);
        if (p == null) return false;

        if (!p.isValidMove(fromRow, fromCol, toRow, toCol, this)) return false;

        Piece target = getPiece(toRow, toCol);
        if (target != null) {
            addToHand(target, p.isBlack());
        }

        board[toRow][toCol] = p;
        board[fromRow][fromCol] = null;

        if (mustPromote(p, toRow)) {
            p.promote();
        }
        else if (promoteIfAvailable && canPromote(p, fromRow, toRow)) {
            p.promote();
        }

        return true;
    }

    private boolean isValidMove(Piece piece, int fromRow, int fromCol, int toRow, int toCol) {

        if (piece instanceof Pawn) {
            int direction = piece.isBlack ? -1 : 1;
            return (toRow == fromRow + direction) && (toCol == fromCol);
        }

        // TODO: Add logic for other pieces
        return true;
    }

    public Piece getPiece(int row, int col) {
        if (row < 0 || row >= 9 || col < 0 || col >= 9) return null;
        return board[row][col];
    }

    public boolean inBounds(int r, int c) {
        return r >= 0 && r < 9 && c >= 0 && c < 9;
    }

    public boolean isEmpty(int r, int c) {
        return getPiece(r, c) == null;
    }

    public boolean isEnemyAt(int r, int c, boolean myColor) {
        Piece p = getPiece(r, c);
        return p != null && p.isBlack() != myColor;
    }

    public boolean isOwnAt(int r, int c, boolean myColor) {
        Piece p = getPiece(r, c);
        return p != null && p.isBlack() == myColor;
    }

    public boolean inPromotionZone(int row, boolean forBlack) {
        return forBlack ? (row <= 2) : (row >= 6);
    }

    public boolean canPromote(Piece p, int fromRow, int toRow) {
        if (p instanceof King || p instanceof GoldGeneral) return false;
        if (p.isPromoted()) return false;
        return inPromotionZone(fromRow, p.isBlack()) || inPromotionZone(toRow, p.isBlack());
    }

    public boolean mustPromote(Piece p, int toRow) {
        if (p.isPromoted()) return false;

        int last = p.isBlack() ? 0 : 8;
        int lastTow = p.isBlack() ? 1 : 7;

        if (p instanceof Pawn || p instanceof Lance) {
            return toRow == last;
        }
        else if (p instanceof Knight) {
            return toRow == last || toRow == lastTow;
        }

        return false;
    }

    void resetForTest() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                board[r][c] = null;
            }
        }
    }

    public List<Piece> getHand(boolean isBlack) {
        return Collections.unmodifiableList(isBlack ? blackHand : whiteHand);
    }

    public String handString(boolean isBlack) {
        StringBuilder sb = new StringBuilder();
        List<Piece> hand = isBlack ? blackHand : whiteHand;

        for (Piece piece : hand) {
            sb.append(piece.getSymbol() + ", ");
        }

        return sb.toString();
    }

    private void addToHand(Piece captured, boolean captorIsBlack) {
        Piece toHand = demotedCopyFor(captured, captorIsBlack);
        if (captorIsBlack) {
            blackHand.add(toHand);
        }
        else {
            whiteHand.add(toHand);
        }
    }

    private Piece demotedCopyFor(Piece piece, boolean newIsBlack) {
        if (piece instanceof Pawn) return new Pawn(newIsBlack);
        if (piece instanceof Lance) return new Lance(newIsBlack);
        if (piece instanceof Knight) return new Knight(newIsBlack);
        if (piece instanceof SilverGeneral) return new SilverGeneral(newIsBlack);
        if (piece instanceof GoldGeneral) return new GoldGeneral(newIsBlack);
        if (piece instanceof Bishop) return new Bishop(newIsBlack);
        if (piece instanceof Rook) return new Rook(newIsBlack);
        if (piece instanceof King) return new King(newIsBlack);
        throw new IllegalArgumentException("Unknown piece class: " + piece.getClass());
    }

    public void printHands() {
        System.out.println("Black hand: " + handString(true));
        System.out.println("White hand: " + handString(false));
    }

    private List<Piece> hand(boolean isBlack) {
        return isBlack ? blackHand : whiteHand;
    }

    private int indexOfHandPiece(List<Piece> hand, Class<? extends Piece> type) {
        for (int i = 0; i < hand.size(); i++) {
            if (type.isInstance(hand.get(i))) {
                return i;
            }
        }

        return -1;
    }

    private boolean hasUnpromotedPawnInFile(int col, boolean isBlack) {
        for (int r = 0; r < 9; r++) {
            Piece piece = board[r][col];
            if (piece instanceof Pawn && piece.isBlack() == isBlack && !piece.isPromoted()) {
                return true;
            }
        }

        return false;
    }

    private boolean violatesLastRankRules(Class<? extends Piece> type, int row, boolean isBlack) {
        int last = isBlack ? 0 : 8;
        int lastTwo = isBlack ? 1 : 7;

        if (type == Pawn.class || type == Lance.class) {
            return row == last;
        }

        if (type == Knight.class) {
            return row == last || row == lastTwo;
        }

        return false;
    }

    /**
     *  Drop a piece from hand onto the board.
     *  @param type      the class of the piece to drop
     *  @param row, col  destination (must be empty)
     *  @param isBlack   side performing the drop
     *  @return true if the drop was legal and applied
     */
    public boolean dropPiece(Class<? extends Piece> type, int row, int col, boolean isBlack) {
        if (!inBounds(row, col)) return false;
        if (!isEmpty(row, col)) return false;

        List<Piece> h = hand(isBlack);
        int index = indexOfHandPiece(h, type);
        if (index == -1) return false;

        if (type == Pawn.class && hasUnpromotedPawnInFile(col, isBlack)) return false;

        if (violatesLastRankRules(type, row, isBlack)) return false;

        // TODO forbid uchifuzume (pawn-drop checkmate)
        // TODO disallow drops that leave own king in check

        // Do the drop: drop the actual piece from hand
        Piece piece = h.remove(index);
        if (piece.isPromoted()) {
            piece = demotedCopyFor(piece, isBlack);
        }

        board[row][col] = piece;
        return true;
    }

    public boolean applyMoveUnchecked(int fromRow, int fromCol, int toRow, int toCol,  boolean promoteIfAvailable) {
        if (!inBounds(fromRow, fromCol) || !inBounds(toRow, toCol)) return false;
        Piece piece = getPiece(fromRow, fromCol);
        if (piece == null) return false;
        if (!piece.isValidMove(fromRow, fromCol, toRow, toCol, this)) return false;

        Piece target = getPiece(toRow, toCol);
        if (target != null) addToHand(target, piece.isBlack());

        board[toRow][toCol] = piece;
        board[fromRow][fromCol] = null;

        if (mustPromote(piece, toRow)) piece.promote();
        else if (promoteIfAvailable && canPromote(piece, fromRow, toRow)) piece.promote();

        return true;
    }

    public boolean dropUnchecked(Class<? extends Piece> type, int row, int col, boolean isBlack) {
        if (!inBounds(row, col) || !isEmpty(row, col)) return false;
        var hand = hand(isBlack);
        int index = indexOfHandPiece(hand, type);
        if (index == -1) return false;
        if (type == Pawn.class && hasUnpromotedPawnInFile(col, isBlack)) return false;
        if (violatesLastRankRules(type, row, isBlack)) return false;

        Piece piece = hand.remove(index);
        if (piece.isPromoted()) piece = demotedCopyFor(piece, isBlack);
        board[row][col] = piece;
        return true;
    }

    void addToHandForTest(Piece piece) {
        if (piece.isBlack()) blackHand.add(piece);
        else                 whiteHand.add(piece);
    }

    public int[] findKing(boolean isBlack) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Piece piece = board[row][col];
                if (piece instanceof King && piece.isBlack() == isBlack) {
                    return new int[] {row, col};
                }
            }
        }

        throw new IllegalStateException("King not found for side: " + (isBlack ? "Black" : "White"));
    }

    public boolean isSquareAttacked(int row, int col, boolean byBlack) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Piece piece = board[r][c];
                if (piece != null && piece.isBlack() == byBlack) {
                    if (piece.isValidMove(r, c, row, col, this)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isInCheck(boolean isBlack) {
        int[] king = findKing(isBlack);
        return isSquareAttacked(king[0], king[1], !isBlack);
    }

    public void printBoard() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Piece piece = board[row][col];
                System.out.print(piece == null ? "." : piece.getSymbol());
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
