package Chess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
    void positionToCoordinate(){
        ChessFileHandler fileHandler = new ChessFileHandler();
        assertEquals(fileHandler.finnPosisjon(new Position(4,6)), "G4");
        assertEquals(fileHandler.finnPosisjon(new Position(0,0)), "A8");
        assertEquals(fileHandler.finnPosisjon(new Position(0,7)), "H8");
        assertEquals(fileHandler.finnPosisjon(new Position(5,4)), "E3");
    } 

    @Test
    void move_writeToFile() throws IOException{
        Path path1 = Path.of("data","Trekkhvit.txt");
        Path path2 = Path.of("data","Trekksvart.txt");
        
        game.Move(new Position(0, 1), new Position(2, 2));
        game.Move(new Position(6,3 ), new Position(5, 3));

        String innhold = Files.readString(path1);
        assertEquals("B8->C6" + System.lineSeparator(), innhold);   

        String innhold2 = Files.readString(path2);
        assertEquals("D2->D3"+ System.lineSeparator(),innhold2);

        game.Move(new Position(0, 1), new Position(2, 2));
        String innhold3 = Files.readString(path1);
        assertEquals("B8->C6" + System.lineSeparator(), innhold3); 
        



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

}
