package Chess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChessTest {

    private Board board; 
    private ChessGame game;

    @BeforeEach
    void setUp(){
        board = new Board(); 
        game = new ChessGame();
    }

    @Test  
    void boardIsSetUp(){
        assertNotNull(board);
    }

    @Test
    void whiteStarts(){
        assertEquals("W", game.getTurn());
    }

    @Test
    void startFormation(){
        Brikke brikke = board.getBrikke(new Position(0,0));
        assertNotNull(brikke);
        assertTrue(brikke instanceof Rook);
        assertEquals(null, board.getBrikke(new Position(2,4)));
        assertEquals(King.class, board.getBrikke(new Position(0,4)).getClass());
        assertEquals(Rook.class, board.getBrikke(new Position(0,0)).getClass());
        assertEquals(King.class, board.getBrikke(new Position(7,4)).getClass());
        assertEquals(Bishop.class, board.getBrikke(new Position(7,2)).getClass());

    }

    @Test
    void firstMove(){
        assertFalse(game.Move(new Position(6,2), new Position(5,2)));
        assertTrue(game.Move(new Position(1,2), new Position(3,2)));
        
    } 

    @Test
    void changeTurns(){
        assertFalse(game.Move(new Position(6,2), new Position(5,2)));
        assertTrue(game.Move(new Position(1,3), new Position(2,3)));
        assertFalse(game.Move(new Position(0,2), new Position(2,3)));
        assertTrue(game.Move(new Position(6,2), new Position(5,2)));

    }

    @Test
    void pawnMoveTwoOnlyOnce(){
        //whitemove
        assertTrue(game.Move(new Position(1,2 ), new Position(3, 2)));
        //blackmove
        game.Move(new Position(6,3 ), new Position(5, 3));
        //whitemove
        assertTrue(game.Move(new Position(3,2 ), new Position(4, 2)));
        //blackmove
        game.Move(new Position(5,3 ), new Position(4, 3));
        //whitemove
        assertFalse(game.Move(new Position(4,2 ), new Position(6, 2)));
        assertTrue(game.Move(new Position(4,2 ), new Position(5, 2)));
    }

    @Test
    void acceptedPositionOnlyOnBoard(){
        assertThrows(IllegalArgumentException.class,()-> new Position(1,8));
        assertThrows(IllegalArgumentException.class,()-> new Position(0,-1));
        assertThrows(IllegalArgumentException.class,()-> new Position(-1,-2));
        assertThrows(IllegalArgumentException.class,()-> new Position(11,2));
        assertThrows(IllegalArgumentException.class,()-> new Position(2,9));
    }

    @Test
    void positionToCoordinate(){ //standard notasjon: hvits bakerste rad (row 0) er rad 1
        ChessFileHandler fileHandler = new ChessFileHandler();
        assertEquals(fileHandler.finnPosisjon(new Position(4,6)), "G5");
        assertEquals(fileHandler.finnPosisjon(new Position(0,0)), "A1");
        assertEquals(fileHandler.finnPosisjon(new Position(0,7)), "H1");
        assertEquals(fileHandler.finnPosisjon(new Position(5,4)), "E6");
        assertEquals(fileHandler.finnPosisjon(new Position(0,4)), "E1"); //hvit konge
        assertEquals(fileHandler.finnPosisjon(new Position(7,3)), "D8"); //svart dronning
    } 

    @Test
    void move_writeToFile() throws IOException{
        Path path1 = Path.of("data","Trekkhvit.txt");
        Path path2 = Path.of("data","Trekksvart.txt");
        
        game.Move(new Position(0, 1), new Position(2, 2));
        game.Move(new Position(6,3 ), new Position(5, 3));

        String innhold = Files.readString(path1);
        assertEquals("B1->C3" + System.lineSeparator(), innhold); //hvit springer

        String innhold2 = Files.readString(path2);
        assertEquals("D7->D6"+ System.lineSeparator(),innhold2); //svart bonde

        game.Move(new Position(0, 1), new Position(2, 2));
        String innhold3 = Files.readString(path1);
        assertEquals("B1->C3" + System.lineSeparator(), innhold3); 
        



    }

    @Test
    void emptyBoard(){
        Board tomt = Board.tomtBrett();
        for(int row=0; row<8; row++){
            for(int col=0; col<8; col++){
                assertEquals(null, tomt.getBrikke(new Position(row, col)));
            }
        }
    }

    @Test
    void placePiece(){
        Board tomt = Board.tomtBrett();
        Rook rook = new Rook("W", new Position(3,4));
        tomt.settBrikke(rook);
        assertEquals(rook, tomt.getBrikke(new Position(3,4)));
    }

    @Test
    void gameFromCustomBoard(){
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new King("W", new Position(0,4)));
        tomt.settBrikke(new King("B", new Position(7,4)));
        tomt.settBrikke(new Rook("W", new Position(3,0)));
        ChessGame customGame = new ChessGame(tomt);

        assertEquals(tomt, customGame.getBoard());
        assertTrue(customGame.Move(new Position(3,0), new Position(3,7)));
        assertEquals(Rook.class, tomt.getBrikke(new Position(3,7)).getClass());
        assertEquals(null, tomt.getBrikke(new Position(3,0)));
        assertEquals("B", customGame.getTurn());
    }

    @Test
    void noCheckAtStart(){
        assertFalse(board.erISjakk("W"));
        assertFalse(board.erISjakk("B"));
    }

    @Test
    void rookGivesCheckUnlessBlocked(){
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new King("W", new Position(0,4)));
        tomt.settBrikke(new King("B", new Position(7,0)));
        tomt.settBrikke(new Rook("B", new Position(5,4)));
        assertTrue(tomt.erISjakk("W"));

        tomt.settBrikke(new Pawn("W", new Position(2,4))); //brikke i veien
        assertFalse(tomt.erISjakk("W"));
    }

    @Test
    void bishopGivesCheck(){
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new King("W", new Position(0,4)));
        tomt.settBrikke(new King("B", new Position(7,0)));
        tomt.settBrikke(new Bishop("B", new Position(3,7)));
        assertTrue(tomt.erISjakk("W"));
    }

    @Test
    void horseGivesCheckOverOtherPieces(){
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new King("W", new Position(0,4)));
        tomt.settBrikke(new King("B", new Position(7,0)));
        tomt.settBrikke(new Pawn("W", new Position(1,4)));
        tomt.settBrikke(new Pawn("W", new Position(1,5)));
        tomt.settBrikke(new Horse("B", new Position(2,5)));
        assertTrue(tomt.erISjakk("W"));
    }

    @Test
    void pawnAttacksDiagonallyOnly(){
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new Pawn("B", new Position(5,3)));
        assertTrue(tomt.erAngrepet(new Position(4,2), "B")); //tom rute skrått foran
        assertTrue(tomt.erAngrepet(new Position(4,4), "B"));
        assertFalse(tomt.erAngrepet(new Position(4,3), "B")); //rett frem er ikke angrep
        assertFalse(tomt.erAngrepet(new Position(6,4), "B")); //bakover er ikke angrep

        tomt.settBrikke(new Pawn("W", new Position(1,1)));
        assertTrue(tomt.erAngrepet(new Position(2,0), "W"));
        assertTrue(tomt.erAngrepet(new Position(2,2), "W"));
        assertFalse(tomt.erAngrepet(new Position(2,1), "W"));
    }

    @Test
    void pawnGivesCheck(){
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new King("W", new Position(0,4)));
        tomt.settBrikke(new King("B", new Position(7,0)));
        tomt.settBrikke(new Pawn("B", new Position(1,5)));
        assertTrue(tomt.erISjakk("W"));
    }

    @Test
    void kingAttacksNeighbourSquares(){
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new King("B", new Position(4,4)));
        assertTrue(tomt.erAngrepet(new Position(3,3), "B"));
        assertTrue(tomt.erAngrepet(new Position(4,5), "B"));
        assertFalse(tomt.erAngrepet(new Position(2,4), "B"));
    }

    @Test
    void pinnedPieceCannotLeaveLine(){
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new King("W", new Position(0,4)));
        tomt.settBrikke(new King("B", new Position(7,0)));
        tomt.settBrikke(new Rook("W", new Position(1,4))); //bundet til kongen
        tomt.settBrikke(new Rook("B", new Position(5,4)));
        ChessGame customGame = new ChessGame(tomt);

        List<Position> trekk = customGame.lovligeTrekk(new Position(1,4));
        assertFalse(trekk.contains(new Position(1,0))); //ut av linjen
        assertTrue(trekk.contains(new Position(3,4))); //langs linjen
        assertTrue(trekk.contains(new Position(5,4))); //slå brikken som binder
        assertFalse(customGame.Move(new Position(1,4), new Position(1,0)));
    }

    @Test
    void mustGetOutOfCheck(){
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new King("W", new Position(0,4)));
        tomt.settBrikke(new King("B", new Position(7,0)));
        tomt.settBrikke(new Rook("B", new Position(5,4))); //gir sjakk
        tomt.settBrikke(new Rook("W", new Position(2,0)));
        tomt.settBrikke(new Horse("W", new Position(0,1)));
        ChessGame customGame = new ChessGame(tomt);

        assertFalse(customGame.Move(new Position(0,1), new Position(2,2))); //hjelper ikke
        assertFalse(customGame.lovligeTrekk(new Position(0,4)).contains(new Position(1,4))); //fortsatt i sjakk
        assertTrue(customGame.lovligeTrekk(new Position(0,4)).contains(new Position(0,3))); //går ut av sjakk
        assertTrue(customGame.Move(new Position(2,0), new Position(2,4))); //blokkerer
    }

    @Test
    void kingCannotMoveIntoCheck(){
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new King("W", new Position(0,4)));
        tomt.settBrikke(new King("B", new Position(7,0)));
        tomt.settBrikke(new Rook("B", new Position(5,3))); //dekker kolonne 3
        tomt.settBrikke(new Pawn("B", new Position(2,6))); //dekker (1,5) og (1,7)
        ChessGame customGame = new ChessGame(tomt);

        List<Position> trekk = customGame.lovligeTrekk(new Position(0,4));
        assertFalse(trekk.contains(new Position(0,3)));
        assertFalse(trekk.contains(new Position(1,3)));
        assertFalse(trekk.contains(new Position(1,5)));
        assertTrue(trekk.contains(new Position(0,5)));
        assertTrue(trekk.contains(new Position(1,4)));
    }

    @Test
    void kingCannotCaptureDefendedPiece(){
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new King("W", new Position(0,4)));
        tomt.settBrikke(new King("B", new Position(7,0)));
        tomt.settBrikke(new Rook("B", new Position(1,4)));
        tomt.settBrikke(new Rook("B", new Position(5,4))); //dekker tårnet på (1,4)
        ChessGame customGame = new ChessGame(tomt);

        assertFalse(customGame.lovligeTrekk(new Position(0,4)).contains(new Position(1,4)));
        //brettet er satt tilbake etter at trekket ble prøvd
        assertEquals(King.class, tomt.getBrikke(new Position(0,4)).getClass());
        assertEquals(Rook.class, tomt.getBrikke(new Position(1,4)).getClass());
        assertEquals(new Position(1,4), tomt.getBrikke(new Position(1,4)).getPosition());
    }

    @Test
    void foolsMate(){
        assertTrue(game.Move(new Position(1,5), new Position(2,5))); //f3
        assertTrue(game.Move(new Position(6,4), new Position(4,4))); //e5
        assertTrue(game.Move(new Position(1,6), new Position(3,6))); //g4
        assertTrue(game.Move(new Position(7,3), new Position(3,7))); //Dh4 matt

        assertTrue(game.isGameOver());
        assertTrue(game.erSjakkMatt());
        assertFalse(game.erPatt());
        assertEquals("B", game.getWinner());
        assertFalse(game.Move(new Position(1,0), new Position(2,0))); //ingen trekk etter matt
    }

    @Test
    void stalemate(){
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new King("W", new Position(0,0)));
        tomt.settBrikke(new King("B", new Position(7,7)));
        tomt.settBrikke(new Queen("W", new Position(5,5)));
        ChessGame customGame = new ChessGame(tomt);

        assertTrue(customGame.Move(new Position(5,5), new Position(5,6))); //svart konge kan ikke flytte, men står ikke i sjakk

        assertTrue(customGame.isGameOver());
        assertTrue(customGame.erPatt());
        assertFalse(customGame.erSjakkMatt());
        assertEquals(null, customGame.getWinner());
    }

    @Test
    void checkDoesNotEndGame(){
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new King("W", new Position(0,4)));
        tomt.settBrikke(new King("B", new Position(7,4)));
        tomt.settBrikke(new Rook("W", new Position(3,0)));
        ChessGame customGame = new ChessGame(tomt);

        assertTrue(customGame.Move(new Position(3,0), new Position(3,4))); //sjakk
        assertTrue(customGame.erSjakk());
        assertFalse(customGame.isGameOver());

        assertTrue(customGame.Move(new Position(7,4), new Position(7,3))); //kongen går ut av sjakk
        assertFalse(customGame.erSjakk());
    }

    @Test
    void findKing(){
        assertEquals(new Position(0,4), board.finnKonge("W"));
        assertEquals(new Position(7,4), board.finnKonge("B"));
        assertEquals(null, Board.tomtBrett().finnKonge("W"));
    }

    private Board castlingBoard(){ //hvit konge og begge tårn på startrutene, svart konge
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new King("W", new Position(0,4)));
        tomt.settBrikke(new Rook("W", new Position(0,0)));
        tomt.settBrikke(new Rook("W", new Position(0,7)));
        tomt.settBrikke(new King("B", new Position(7,4)));
        return tomt;
    }

    @Test
    void castleShort(){
        Board tomt = castlingBoard();
        ChessGame customGame = new ChessGame(tomt);
        assertTrue(customGame.Move(new Position(0,4), new Position(0,6)));
        assertEquals(King.class, tomt.getBrikke(new Position(0,6)).getClass());
        assertEquals(Rook.class, tomt.getBrikke(new Position(0,5)).getClass());
        assertEquals(new Position(0,5), tomt.getBrikke(new Position(0,5)).getPosition());
        assertEquals(null, tomt.getBrikke(new Position(0,7)));
        assertEquals(null, tomt.getBrikke(new Position(0,4)));
    }

    @Test
    void castleLong(){
        Board tomt = castlingBoard();
        ChessGame customGame = new ChessGame(tomt);
        assertTrue(customGame.Move(new Position(0,4), new Position(0,2)));
        assertEquals(King.class, tomt.getBrikke(new Position(0,2)).getClass());
        assertEquals(Rook.class, tomt.getBrikke(new Position(0,3)).getClass());
        assertEquals(null, tomt.getBrikke(new Position(0,0)));
    }

    @Test
    void blackCanCastle(){
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new King("W", new Position(0,0)));
        tomt.settBrikke(new King("B", new Position(7,4)));
        tomt.settBrikke(new Rook("B", new Position(7,7)));
        ChessGame customGame = new ChessGame(tomt);
        assertTrue(customGame.Move(new Position(0,0), new Position(0,1)));
        assertTrue(customGame.Move(new Position(7,4), new Position(7,6)));
        assertEquals(Rook.class, tomt.getBrikke(new Position(7,5)).getClass());
    }

    @Test
    void noCastlingAfterKingHasMoved(){
        ChessGame customGame = new ChessGame(castlingBoard());
        assertTrue(customGame.Move(new Position(0,4), new Position(1,4)));
        assertTrue(customGame.Move(new Position(7,4), new Position(7,3)));
        assertTrue(customGame.Move(new Position(1,4), new Position(0,4))); //tilbake på startruten
        assertTrue(customGame.Move(new Position(7,3), new Position(7,4)));
        assertFalse(customGame.lovligeTrekk(new Position(0,4)).contains(new Position(0,6)));
        assertFalse(customGame.lovligeTrekk(new Position(0,4)).contains(new Position(0,2)));
    }

    @Test
    void noCastlingWithMovedRook(){
        ChessGame customGame = new ChessGame(castlingBoard());
        assertTrue(customGame.Move(new Position(0,7), new Position(1,7)));
        assertTrue(customGame.Move(new Position(7,4), new Position(7,3)));
        assertTrue(customGame.Move(new Position(1,7), new Position(0,7))); //tilbake på startruten
        assertTrue(customGame.Move(new Position(7,3), new Position(7,4)));
        assertFalse(customGame.lovligeTrekk(new Position(0,4)).contains(new Position(0,6)));
        assertTrue(customGame.lovligeTrekk(new Position(0,4)).contains(new Position(0,2))); //det andre tårnet har ikke flyttet
    }

    @Test
    void noCastlingThroughPieces(){
        Board tomt = castlingBoard();
        tomt.settBrikke(new Bishop("W", new Position(0,5)));
        tomt.settBrikke(new Horse("W", new Position(0,1)));
        ChessGame customGame = new ChessGame(tomt);
        assertFalse(customGame.lovligeTrekk(new Position(0,4)).contains(new Position(0,6)));
        assertFalse(customGame.lovligeTrekk(new Position(0,4)).contains(new Position(0,2)));
    }

    @Test
    void noCastlingOutOfCheck(){
        Board tomt = castlingBoard();
        tomt.settBrikke(new Rook("B", new Position(5,4)));
        ChessGame customGame = new ChessGame(tomt);
        assertFalse(customGame.lovligeTrekk(new Position(0,4)).contains(new Position(0,6)));
        assertFalse(customGame.lovligeTrekk(new Position(0,4)).contains(new Position(0,2)));
    }

    @Test
    void noCastlingThroughOrIntoAttackedSquare(){
        Board tomt = castlingBoard();
        tomt.settBrikke(new Rook("B", new Position(5,5))); //angriper ruten kongen går over ved kort rokade
        tomt.settBrikke(new Rook("B", new Position(5,2))); //angriper ruten kongen lander på ved lang rokade
        ChessGame customGame = new ChessGame(tomt);
        assertFalse(customGame.lovligeTrekk(new Position(0,4)).contains(new Position(0,6)));
        assertFalse(customGame.lovligeTrekk(new Position(0,4)).contains(new Position(0,2)));
    }

    @Test
    void longCastlingAllowedWhenOnlyRookPathIsAttacked(){
        Board tomt = castlingBoard();
        tomt.settBrikke(new Rook("B", new Position(5,1))); //angriper (0,1), som bare tårnet passerer
        ChessGame customGame = new ChessGame(tomt);
        assertTrue(customGame.lovligeTrekk(new Position(0,4)).contains(new Position(0,2)));
    }

    @Test
    void enPassantFromStart(){
        assertTrue(game.Move(new Position(1,4), new Position(3,4))); //e4
        assertTrue(game.Move(new Position(6,0), new Position(5,0))); //a6
        assertTrue(game.Move(new Position(3,4), new Position(4,4))); //e5
        assertTrue(game.Move(new Position(6,3), new Position(4,3))); //d5, hopper over (5,3)
        assertTrue(game.Move(new Position(4,4), new Position(5,3))); //exd6 en passant

        assertEquals(Pawn.class, game.getBoard().getBrikke(new Position(5,3)).getClass());
        assertEquals("W", game.getBoard().getBrikke(new Position(5,3)).getColor());
        assertEquals(null, game.getBoard().getBrikke(new Position(4,3))); //svart bonde er slått
        assertEquals(null, game.getBoard().getBrikke(new Position(4,4)));
    }

    @Test
    void blackCanCaptureEnPassant(){
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new King("W", new Position(0,0)));
        tomt.settBrikke(new King("B", new Position(7,7)));
        tomt.settBrikke(new Pawn("W", new Position(1,4)));
        tomt.settBrikke(new Pawn("B", new Position(3,3)));
        ChessGame customGame = new ChessGame(tomt);

        assertTrue(customGame.Move(new Position(1,4), new Position(3,4)));
        assertTrue(customGame.Move(new Position(3,3), new Position(2,4)));
        assertEquals(null, tomt.getBrikke(new Position(3,4)));
    }

    @Test
    void enPassantOnlyRightAfterDoubleStep(){
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new King("W", new Position(0,0)));
        tomt.settBrikke(new King("B", new Position(7,7)));
        tomt.settBrikke(new Pawn("W", new Position(4,4)));
        tomt.settBrikke(new Pawn("B", new Position(6,3)));
        ChessGame customGame = new ChessGame(tomt);

        assertTrue(customGame.Move(new Position(0,0), new Position(0,1)));
        assertTrue(customGame.Move(new Position(6,3), new Position(4,3))); //dobbelsteg
        assertTrue(customGame.lovligeTrekk(new Position(4,4)).contains(new Position(5,3)));

        assertTrue(customGame.Move(new Position(0,1), new Position(0,0))); //hvit venter
        assertTrue(customGame.Move(new Position(7,7), new Position(7,6)));
        assertFalse(customGame.Move(new Position(4,4), new Position(5,3))); //for sent
    }

    @Test
    void noEnPassantAfterSingleSteps(){
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new King("W", new Position(0,0)));
        tomt.settBrikke(new King("B", new Position(7,7)));
        tomt.settBrikke(new Pawn("W", new Position(4,4)));
        tomt.settBrikke(new Pawn("B", new Position(6,3)));
        ChessGame customGame = new ChessGame(tomt);

        assertTrue(customGame.Move(new Position(0,0), new Position(0,1)));
        assertTrue(customGame.Move(new Position(6,3), new Position(5,3))); //ett steg
        assertTrue(customGame.Move(new Position(0,1), new Position(0,0)));
        assertTrue(customGame.Move(new Position(5,3), new Position(4,3))); //ett steg til, står nå ved siden av
        assertFalse(customGame.lovligeTrekk(new Position(4,4)).contains(new Position(5,3)));
    }

    @Test
    void noEnPassantThatExposesKing(){
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new King("W", new Position(4,0)));
        tomt.settBrikke(new Pawn("W", new Position(4,4)));
        tomt.settBrikke(new Rook("W", new Position(0,7)));
        tomt.settBrikke(new Pawn("B", new Position(6,3)));
        tomt.settBrikke(new Rook("B", new Position(4,7))); //begge bøndene står mellom tårnet og hvit konge
        tomt.settBrikke(new King("B", new Position(7,6)));
        ChessGame customGame = new ChessGame(tomt);

        assertTrue(customGame.Move(new Position(0,7), new Position(1,7)));
        assertTrue(customGame.Move(new Position(6,3), new Position(4,3))); //dobbelsteg
        List<Position> trekk = customGame.lovligeTrekk(new Position(4,4));
        assertFalse(trekk.contains(new Position(5,3))); //en passant ville fjernet begge bøndene fra raden
        assertTrue(trekk.contains(new Position(5,4)));
        //brettet er satt tilbake etter at trekket ble prøvd
        assertEquals(Pawn.class, tomt.getBrikke(new Position(4,3)).getClass());
    }

    private Board promotionBoard(){ //hvit bonde ett steg fra siste rad
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new King("W", new Position(0,0)));
        tomt.settBrikke(new King("B", new Position(7,7)));
        tomt.settBrikke(new Pawn("W", new Position(6,4)));
        return tomt;
    }

    @Test
    void promotesToQueenByDefault(){
        Board tomt = promotionBoard();
        ChessGame customGame = new ChessGame(tomt);
        assertTrue(customGame.Move(new Position(6,4), new Position(7,4)));
        Brikke ny = tomt.getBrikke(new Position(7,4));
        assertEquals(Queen.class, ny.getClass());
        assertEquals("W", ny.getColor());
        assertEquals(new Position(7,4), ny.getPosition());
    }

    @Test
    void promoteToHorse(){
        Board tomt = promotionBoard();
        ChessGame customGame = new ChessGame(tomt);
        assertTrue(customGame.Move(new Position(6,4), new Position(7,4), "Horse"));
        assertEquals(Horse.class, tomt.getBrikke(new Position(7,4)).getClass());
    }

    @Test
    void promoteWhileCapturing(){
        Board tomt = promotionBoard();
        tomt.settBrikke(new Bishop("B", new Position(7,5)));
        ChessGame customGame = new ChessGame(tomt);
        assertTrue(customGame.Move(new Position(6,4), new Position(7,5), "Rook"));
        assertEquals(Rook.class, tomt.getBrikke(new Position(7,5)).getClass());
        assertEquals("W", tomt.getBrikke(new Position(7,5)).getColor());
        assertEquals(null, tomt.getBrikke(new Position(6,4)));
    }

    @Test
    void blackPromotes(){
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new King("W", new Position(2,7)));
        tomt.settBrikke(new King("B", new Position(7,7)));
        tomt.settBrikke(new Pawn("B", new Position(1,3)));
        ChessGame customGame = new ChessGame(tomt);
        assertTrue(customGame.Move(new Position(2,7), new Position(2,6)));
        assertTrue(customGame.Move(new Position(1,3), new Position(0,3), "Bishop"));
        assertEquals(Bishop.class, tomt.getBrikke(new Position(0,3)).getClass());
        assertEquals("B", tomt.getBrikke(new Position(0,3)).getColor());
    }

    @Test
    void invalidPromotionChoice(){
        Board tomt = promotionBoard();
        ChessGame customGame = new ChessGame(tomt);
        assertThrows(IllegalArgumentException.class, () -> customGame.Move(new Position(6,4), new Position(7,4), "King"));
        assertThrows(IllegalArgumentException.class, () -> customGame.Move(new Position(6,4), new Position(7,4), null));
        assertEquals(Pawn.class, tomt.getBrikke(new Position(6,4)).getClass()); //ingenting er flyttet
        assertEquals("W", customGame.getTurn());
    }

    @Test
    void detectsPromotion(){
        ChessGame customGame = new ChessGame(promotionBoard());
        assertTrue(customGame.erBondeforvandling(new Position(6,4), new Position(7,4)));
        assertFalse(customGame.erBondeforvandling(new Position(0,0), new Position(1,0))); //konge
        assertFalse(game.erBondeforvandling(new Position(1,4), new Position(2,4))); //vanlig bondetrekk
    }

    @Test
    void promotionIsWrittenToFile() throws IOException{
        ChessGame customGame = new ChessGame(promotionBoard());
        assertTrue(customGame.Move(new Position(6,4), new Position(7,4), "Horse"));
        assertEquals("E7->E8=S" + System.lineSeparator(), Files.readString(Path.of("data","Trekkhvit.txt")));
    }

    private List<Position> trekkPåBrett(Board brett, Brikke brikke){ //setter brikken ut og henter brikkens egne trekk
        brett.settBrikke(brikke);
        brikke.lovligetrekk(brett);
        return brikke.getlovligetrekk();
    }

    @Test
    void horseMoves(){
        assertEquals(8, trekkPåBrett(Board.tomtBrett(), new Horse("W", new Position(3,3))).size()); //midt på brettet

        List<Position> hjørne = trekkPåBrett(Board.tomtBrett(), new Horse("W", new Position(0,0)));
        assertEquals(2, hjørne.size());
        assertTrue(hjørne.contains(new Position(2,1)));
        assertTrue(hjørne.contains(new Position(1,2)));
    }

    @Test
    void horseCanTakeEnemyButNotOwnPiece(){
        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new Pawn("W", new Position(2,1)));
        tomt.settBrikke(new Pawn("B", new Position(1,2)));
        List<Position> trekk = trekkPåBrett(tomt, new Horse("W", new Position(0,0)));
        assertEquals(List.of(new Position(1,2)), trekk);
    }

    @Test
    void bishopMoves(){
        assertEquals(13, trekkPåBrett(Board.tomtBrett(), new Bishop("W", new Position(3,3))).size());

        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new Pawn("W", new Position(5,5))); //egen brikke stopper løperen før ruten
        tomt.settBrikke(new Pawn("B", new Position(1,1))); //motstander kan slås, men ikke hoppes over
        List<Position> trekk = trekkPåBrett(tomt, new Bishop("W", new Position(3,3)));
        assertTrue(trekk.contains(new Position(4,4)));
        assertFalse(trekk.contains(new Position(5,5)));
        assertFalse(trekk.contains(new Position(6,6)));
        assertTrue(trekk.contains(new Position(1,1)));
        assertFalse(trekk.contains(new Position(0,0)));
        assertFalse(trekk.contains(new Position(3,4))); //ikke rett frem
    }

    @Test
    void rookMoves(){
        assertEquals(14, trekkPåBrett(Board.tomtBrett(), new Rook("W", new Position(3,3))).size());

        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new Pawn("W", new Position(3,5)));
        tomt.settBrikke(new Pawn("B", new Position(1,3)));
        List<Position> trekk = trekkPåBrett(tomt, new Rook("W", new Position(3,3)));
        assertTrue(trekk.contains(new Position(3,4)));
        assertFalse(trekk.contains(new Position(3,5)));
        assertFalse(trekk.contains(new Position(3,6)));
        assertTrue(trekk.contains(new Position(1,3)));
        assertFalse(trekk.contains(new Position(0,3)));
        assertFalse(trekk.contains(new Position(4,4))); //ikke skrått
    }

    @Test
    void queenMoves(){
        List<Position> trekk = trekkPåBrett(Board.tomtBrett(), new Queen("W", new Position(3,3)));
        assertEquals(27, trekk.size()); //14 som tårn + 13 som løper
        assertTrue(trekk.contains(new Position(7,7)));
        assertTrue(trekk.contains(new Position(3,0)));
        assertFalse(trekk.contains(new Position(5,4))); //ikke springertrekk
    }

    @Test
    void kingMoves(){
        assertEquals(8, trekkPåBrett(Board.tomtBrett(), new King("W", new Position(3,3))).size());
        assertEquals(3, trekkPåBrett(Board.tomtBrett(), new King("W", new Position(0,0))).size());

        Board tomt = Board.tomtBrett();
        tomt.settBrikke(new Pawn("W", new Position(1,1)));
        List<Position> trekk = trekkPåBrett(tomt, new King("W", new Position(0,0)));
        assertFalse(trekk.contains(new Position(1,1))); //egen brikke
        assertEquals(2, trekk.size());
    }

    @Test
    void pawnMoves(){
        List<Position> start = trekkPåBrett(Board.tomtBrett(), new Pawn("W", new Position(1,4)));
        assertEquals(2, start.size()); //ett eller to steg frem
        assertTrue(start.contains(new Position(2,4)));
        assertTrue(start.contains(new Position(3,4)));

        Board sperret = Board.tomtBrett();
        sperret.settBrikke(new Pawn("B", new Position(2,4)));
        assertTrue(trekkPåBrett(sperret, new Pawn("W", new Position(1,4))).isEmpty()); //kan ikke hoppe over

        Board slag = Board.tomtBrett();
        slag.settBrikke(new Pawn("B", new Position(4,3)));
        slag.settBrikke(new Pawn("W", new Position(4,5)));
        slag.settBrikke(new Pawn("B", new Position(4,4))); //rett foran, kan ikke slås
        List<Position> trekk = trekkPåBrett(slag, new Pawn("W", new Position(3,4)));
        assertTrue(trekk.contains(new Position(4,3))); //slår motstander skrått
        assertFalse(trekk.contains(new Position(4,5))); //ikke egen brikke
        assertFalse(trekk.contains(new Position(4,4)));
    }

    @Test
    void blackPawnMovesDown(){
        List<Position> start = trekkPåBrett(Board.tomtBrett(), new Pawn("B", new Position(6,2)));
        assertTrue(start.contains(new Position(5,2)));
        assertTrue(start.contains(new Position(4,2)));
        assertEquals(2, start.size());
    }

}
