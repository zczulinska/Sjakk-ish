package Chess;


public class Board {

    private Brikke[][] board = new Brikke[8][8]; //Array med 8x8 ruter 

    public Board(){
        settOppBrikker();
    }

    private void settOppBrikker() {
        //White
            for(int kolonne=0;kolonne<8;kolonne++){
                board[1][kolonne] = new Pawn("W",new Position(1, kolonne));
            }
            board[0][0] = new Rook("W",new Position(0,0)); 
            board[0][1] = new Horse("W",new Position(0,1)); 
            board[0][2] = new Bishop("W",new Position(0,2));
            board[0][3] = new Queen("W",new Position(0,3));
            board[0][4] = new King("W",new Position(0,4));
            board[0][5] = new Bishop("W",new Position(0,5));
            board[0][6] = new Horse("W",new Position(0,6)); 
            board[0][7] = new Rook("W",new Position(0,7));
            
            //BLACK
            for(int kolonne=0;kolonne<8;kolonne++){
                board[6][kolonne] = new Pawn("B",new Position(6, kolonne));
            }
            board[7][0] = new Rook("B",new Position(7,0)); 
            board[7][1] = new Horse("B",new Position(7,1)); 
            board[7][2] = new Bishop("B",new Position(7,2));
            board[7][3] = new Queen("B",new Position(7,3));
            board[7][4] = new King("B",new Position(7,4));
            board[7][5] = new Bishop("B",new Position(7,5));
            board[7][6] = new Horse("B",new Position(7,6)); 
            board[7][7] = new Rook("B",new Position(7,7));
    }
    
    public Brikke[][] getboard(){
        return board;
    }
    
    public Brikke getBrikke(Position position){
        return(board[position.getrow()][position.getcol()]);
    }

    public void movePiece(Position fra, Position til){
        Brikke brikke = this.getBrikke(fra);
        board[fra.getrow()][fra.getcol()] = null;
        board[til.getrow()][til.getcol()] = brikke;

        if(brikke!= null){
            brikke.setPosition(til);
        }
    }
}

