package de.dhbw.tinf18b4.chess.backend;

import de.dhbw.tinf18b4.chess.backend.piece.*;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import de.dhbw.tinf18b4.chess.backend.user.User;
import de.dhbw.tinf18b4.chess.states.GameState;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * Test cases for the {@link Game} implementation
 *
 * @author Leonhard Gahr
 */
public class GameTest {
    @NotNull
    private User user1 = new User("user1", "", "");
    @NotNull
    private User user2 = new User("user2", "", "");

    @NotNull
    private Player white = new Player(true, user1);
    @NotNull
    private Player black = new Player(false, user2);

    @Test
    public void fenTest() {
        Game game = new Game(white, black);
        assertEquals("Initial game FEN should be", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", game.asFen());
    }

    @Test
    public void drawFalseTest() {

        Game game = new Game(white, black);
        assertFalse(game.evaluateGame().isDraw());
    }

    @Test
    public void stalemateTest() {
        Piece[] pieces = new Piece[]{
                new King(true, new Position("e1")),
                new Pawn(false, new Position("e2")),
                new King(false, new Position("e3"))
        };
        Game game = new Game(white, black, pieces);
        assertSame("Game should be in stalemate", GameState.STALEMATE, game.evaluateGame());
    }

    @Test
    public void drawByInsufficientMaterialTest() {
        Piece[] kingVsKing = new Piece[]{
                new King(false, new Position("a1")),
                new King(true, new Position("a8"))
        };
        Game game = new Game(white, black, kingVsKing);

        assertSame("Game should be in draw - king vs king", GameState.KING_VS_KING, game.evaluateGame());

        Piece[] kingBishopVsKing = new Piece[]{
                new King(false, new Position("a1")),
                new Bishop(true, new Position("a7")),
                new King(true, new Position("a8"))
        };
        game = new Game(white, black, kingBishopVsKing);
        assertSame("Game should be in draw - king and bishop vs king", GameState.KING_BISHOP_VS_KING, game.evaluateGame());

        Piece[] kingKnightVsKing = new Piece[]{
                new King(false, new Position("a1")),
                new Knight(true, new Position("a7")),
                new King(true, new Position("a8"))
        };
        game = new Game(white, black, kingKnightVsKing);
        assertSame("Game should be in draw - king and knight vs king", GameState.KING_KNIGHT_VS_KING, game.evaluateGame());

        Piece[] kingBishopVsKingBishop = new Piece[]{
                new King(false, new Position("a1")),
                new Bishop(false, new Position("a2")),

                new King(true, new Position("a8")),
                new Bishop(true, new Position("b1"))
        };
        game = new Game(white, black, kingBishopVsKingBishop);
        assertSame("Game should be in draw - king and bishop vs king and bishop", GameState.KING_BISHOP_VS_KING_BISHOP, game.evaluateGame());
    }

    @Test(expected = AssertionError.class)
    public void checkmateTest() {
        Piece[] checkmate = new Piece[]{
                new King(true, new Position("f5")),
                new Rook(true, new Position("h1")),

                new King(false, new Position("h5"))
        };

        Game game = new Game(white, black, checkmate);
        assertEquals("White player should have won", white, game.isCheckmate());
    }

    @Test
    public void initialSetupTest() {
        Game game = new Game(white, black);

        game.getBoard().getPieces()
                .filter(piece -> piece instanceof Rook || piece instanceof Bishop || piece instanceof King || piece instanceof Queen)
                .forEach(piece -> {
                    assertEquals(0, piece.getValidMoves(game.getBoard()).count());
                    assertEquals(0, piece.getValidCaptureMoves(game.getBoard()).count());
                });
        game.getBoard().getPieces()
                .filter(piece -> piece instanceof Knight || piece instanceof Pawn)
                .forEach(piece -> {
                    assertEquals(2, piece.getValidMoves(game.getBoard()).count());
                    assertEquals(0, piece.getValidCaptureMoves(game.getBoard()).count());
                });
    }

    static class PgnParser {
        @RegExp
        private final static String moveText = "(\\d+)[ \\n]*\\.[ \\n]*((?:[QNKRBxabcdefgh]+\\d\\+?)|O-O|O-O-O)[ \\n]*((?:[QNKRBxabcdefgh]+\\d\\+?)|O-O|O-O-O)?(1-0|0-1|1/2-1/2)?";
        @RegExp
        private final static String movePattern = "([QNKRBP])?([abcdefgh])?(\\d)?([abcdefgh])(\\d)(?:=([QNKRBP]))?([+#])?";
        @RegExp
        private final static String headerEntryPattern = "\\[(.+?) \"(.+?)\"]";

        private final String document;
        private Player player1;
        private Player player2;
        private Board board;

        PgnParser(String document) {
            this.document = document;
        }

        public static void main(String[] args) {
            String GameWithCastling = "[Event \"F/S Return Match\"]\n" +
                    "[Site \"Belgrade, Serbia JUG\"]\n" +
                    "[Date \"1992.11.04\"]\n" +
                    "[Round \"29\"]\n" +
                    "[White \"Fischer, Robert J.\"]\n" +
                    "[Black \"Spassky, Boris V.\"]\n" +
                    "[Result \"1/2-1/2\"]\n" +
                    "\n" +
                    "1. e4 e5 2. Nf3 Nc6 3. Bb5 a6 {This opening is called the Ruy Lopez.}\n" +
                    "4. Ba4 Nf6 5. O-O Be7 6. Re1 b5 7. Bb3 d6 8. c3 O-O 9. h3 Nb8 10. d4 Nbd7\n" +
                    "11. c4 c6 12. cxb5 axb5 13. Nc3 Bb7 14. Bg5 b4 15. Nb1 h6 16. Bh4 c5 17. dxe5\n" +
                    "Nxe4 18. Bxe7 Qxe7 19. exd6 Qf6 20. Nbd2 Nxd6 21. Nc4 Nxc4 22. Bxc4 Nb6\n" +
                    "23. Ne5 Rae8 24. Bxf7+ Rxf7 25. Nxf7 Rxe1+ 26. Qxe1 Kxf7 27. Qe3 Qg5 28. Qxg5\n" +
                    "hxg5 29. b3 Ke6 30. a3 Kd6 31. axb4 cxb4 32. Ra5 Nd5 33. f3 Bc8 34. Kf2 Bf5\n" +
                    "35. Ra7 g6 36. Ra6+ Kc5 37. Ke1 Nf4 38. g3 Nxh3 39. Kd2 Kb5 40. Rd6 Kc5 41. Ra6\n" +
                    "Nf2 42. g4 Bd3 43. Re6 1/2-1/2";

            String gameWithoutCastling = "[Event \"12. Agzamov Mem 2018\"]\n" +
                    "[Site \"Tashkent UZB\"]\n" +
                    "[Date \"2018.04.27\"]\n" +
                    "[Round \"5.3\"]\n" +
                    "[White \"Yakubboev, Nodirbek\"]\n" +
                    "[Black \"Duzhakov, Ilya\"]\n" +
                    "[Result \"1-0\"]\n" +
                    "[WhiteElo \"2482\"]\n" +
                    "[BlackElo \"2421\"]\n" +
                    "[ECO \"E60\"]\n" +
                    "[EventDate \"2018.04.23\"]\n" +
                    "\n" +
                    "1.d4 Nf6 2.c4 g6 3.f3 e6 4.e4 d5 5.cxd5 exd5 6.e5 Nh5 7.Be3 c5 8.dxc5 Qh4+\n" +
                    "9.Bf2 Qb4+ 10.Nc3 Nc6 11.a3 Qxb2 12.Nge2 Nxe5 13.Bd4 Nd3+ 14.Qxd3 Qxa1+ \n" +
                    "15.Nd1 Qa2 16.Nc1 1-0";
            try {
                new PgnParser(gameWithoutCastling).moveStream().forEach(System.out::println);
            } catch (CastlingNotImplementedException | ErroneousInputException e) {
                e.printStackTrace();
            }
        }

        private void reset() {
            player1 = new Player(true, new User("user1", "", ""));
            player2 = new Player(false, new User("user2", "", ""));
            board = new Board(new Game(player1, player2));
        }

        /**
         * Parses the input document and creates a mapping with the headers
         *
         * @return the header mapping
         */
        Map<String, String> parseHeader() {
            Pattern pattern = Pattern.compile(headerEntryPattern, Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(document);

            return matcher.results().collect(Collectors.toMap(o -> o.group(1), o -> o.group(2)));
        }

        /**
         * Parses the input document and creates moves contained in the document movetext
         *
         * @return a stream of moves
         * @throws CastlingNotImplementedException castling is not yet supported
         * @throws ErroneousInputException         a parsing error occurred
         */
        Stream<Move> moveStream() throws CastlingNotImplementedException, ErroneousInputException {
            reset();

            String beginMarker = "]\n\n1.";
            int bodyBegin = document.indexOf(beginMarker);

            if (bodyBegin == -1) {
                throw new ErroneousInputException("Invalid Pgn string supplied");
            }

            bodyBegin += 3;
            Pattern pattern = Pattern.compile(moveText);
            Matcher matcher = pattern.matcher(document.substring(bodyBegin));

            String headerMatchResult = parseHeader().get("Result");


            List<MatchResult> matchResults = matcher.results().collect(Collectors.toList());
            Stream.Builder<Move> builder = Stream.builder();
            for (MatchResult matchResult : matchResults) {
                int moveNumber = Integer.parseUnsignedInt(matchResult.group(1));
                String whiteMoveToken = matchResult.group(2);
                String blackMoveToken = matchResult.group(3);
                String bodyMatchResult = matchResult.group(4);

                builder.accept(parseMove(whiteMoveToken, true));

                // if white wins black cannot make a move
                // because the game ended
                if (blackMoveToken == null) {
                    if (!bodyMatchResult.equals(headerMatchResult)) {
                        throw new ErroneousInputException("match result in movetext is not equal to the match result found in the header");
                    }
                    continue;
                }

                builder.accept(parseMove(blackMoveToken, false));
            }

            return builder.build();
        }

        /**
         * Parse a move obtained from the pgn movetext. It is important to call this in the order of
         * appearance as given in the movetext. That is because if the origin position of a piece
         * is unambiguous the specification allows to leave it implicit. This means more work for us
         * because we have to keep track of the board state and need to find the origin position for every move.
         *
         * @param moveToken a move of a single player from the movetext
         * @param white     the player color
         * @return the parsed move
         * @throws CastlingNotImplementedException castling is not yet supported
         * @throws ErroneousInputException         if the move is not recognized
         */
        @NotNull
        private Move parseMove(String moveToken, boolean white) throws CastlingNotImplementedException, ErroneousInputException {
            boolean isCapture = moveToken.contains("x");
            String token = moveToken.replaceAll("x", "");

            if (token.equals("O-O")) { // kingside castling
                int rookFile = white ? 1 : 8;
                Position rookPosition = new Position(rookFile, 'h');
                throw new CastlingNotImplementedException("castling support not implemented");
            } else if (token.equals("O-O-O")) { // queenside castling
                int rookFile = white ? 1 : 8;
                Position rookPosition = new Position(rookFile, 'a');
                throw new CastlingNotImplementedException("castling support not implemented");
            }

            MatchResult matcher = Pattern.compile(movePattern).matcher(token)
                    .results().findFirst().orElseThrow();
            String fenIdentifier = Optional.ofNullable(matcher.group(1)).orElse("P");
            String originFile = matcher.group(2);
            String originRank = matcher.group(3);
            String destinationFile = matcher.group(4);
            String destinationRank = matcher.group(5);
            String pawnPromotion = matcher.group(6);
            String checkMateCheck = matcher.group(7);

            Piece piece;
            Position origin;
            Position destination;
            try {
                destination = new Position(destinationFile + destinationRank);
                if (originFile == null || originRank == null) {
                    Stream<Piece> pieces = board.getPieces()
                            // find all pieces of the given type
                            .filter(p -> Character.toString(p.getFenIdentifier()).toUpperCase().equals(fenIdentifier))
                            .filter(p -> p.isWhite() == white)
                            // find only pieces which can reach the destination
                            .filter(p -> {
                                Stream<Position> destinations = isCapture ? p.getValidCaptureMoves(board) : p.getValidMoves(board);
                                return destinations.anyMatch(pos -> pos.equals(destination));
                            });

                    if (originFile != null) {
                        pieces = pieces.filter(p -> String.valueOf(p.getPosition().getFile()).equals(originFile));
                    }

                    if (originRank != null) {
                        pieces = pieces.filter(p -> String.valueOf(p.getPosition().getRank()).equals(originRank));
                    }

                    List<Piece> pieceList = pieces.collect(Collectors.toList());

                    if (pieceList.size() != 1) {
                        throw new ErroneousInputException("Invalid pgn token token: ambiguous token encountered in " + moveToken + " " + pieceList);
                    }

                    piece = pieceList.get(0);
                    origin = piece.getPosition();
                } else {
                    origin = new Position(originFile + originRank);
                    piece = Objects.requireNonNull(board.findPieceByPosition(origin));
                }
            } catch (IllegalArgumentException e) {
                throw new ErroneousInputException(e);
            }

            Move move = new Move(player1.isWhite() == white ? player1 : player2, origin, destination, piece);
            board.applyMove(move);
            return move;
        }

        static class ErroneousInputException extends Throwable {
            ErroneousInputException(String s) {
                super(s);
            }

            ErroneousInputException(Throwable e) {
                super(e);
            }
        }

        static class CastlingNotImplementedException extends Throwable {
            CastlingNotImplementedException(String s) {
                super(s);
            }
        }
    }
}
