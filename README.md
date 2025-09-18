# Java Shogi Engine

A Java 17 Shogi engine with full board rules, drops (komadai), legal move generation, check detection, checkmate, undo/history, SFEN I/O, and a tiny CLI to play from the terminal.

## Features
- Board + pieces (Pawn, Lance, Knight, Silver, Gold, Bishop, Rook, King)
- Promotion (forced + optional, zone-aware)
- Captures → demote & change side → **hand** (komadai)
- Drops with legality (nifu, last-rank / last-two-ranks for P/L/N)
- Self-check prevention
- **Pawn-drop mate** (uchifuzume) rule
- Legal move generator (moves & drops, promotion branches)
- Check / Checkmate detection
- Undo / move history
- SFEN encode & decode
- Simple CLI to play both sides

## Requirements
- Java 17+
- Maven 3.8+

## Build & Test
```bash
mvn clean test
