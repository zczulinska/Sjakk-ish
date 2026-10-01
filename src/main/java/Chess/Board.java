package Chess;


public class Board {

    private Brikke[][] board = new Brikke[8][8]; //Array med 8x8 ruter 

    public Board(){
        this(true);
    }

    private Board(boolean medBrikker){
        if(medBrikker){
            settOppBrikker();
        }
    }

    public static Board tomtBrett(){ //brett uten brikker, brukes for å sette opp egne stillinger
        return new Board(false);
    }

    public void settBrikke(Brikke brikke){ //setter brikken på ruten den selv har som posisjon
        board[brikke.getPosition().getrow()][brikke.getPosition().getcol()] = brikke;
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

    public boolean erAngrepet(Position rute, String angriperFarge){ //sjekker om en brikke av angriperFarge kan slå på ruten
        for(int row=0; row<8; row++){
            for(int col=0; col<8; col++){
                Brikke brikke = board[row][col];
                if(brikke == null || !brikke.getColor().equals(angriperFarge)){
                    continue;
                }
                if(brikke instanceof Pawn pawn){ //bondens vanlige trekk går rett frem, men den angriper skrått
                    if(pawn.angriper(rute)){
                        return true;
                    }
                }
                else{
                    brikke.lovligetrekk(this);
                    if(brikke.getlovligetrekk().contains(rute)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Position finnKonge(String farge){ //posisjonen til kongen med denne fargen, eller null
        for(int row=0; row<8; row++){
            for(int col=0; col<8; col++){
                Brikke brikke = board[row][col];
                if(brikke instanceof King && brikke.getColor().equals(farge)){
                    return brikke.getPosition();
                }
            }
        }
        return null;
    }

    public boolean erISjakk(String farge){ //sjekker om kongen med denne fargen står i sjakk
        Position konge = finnKonge(farge);
        if(konge == null){
            return false; //ingen konge på brettet
        }
        String motstander = "W".equals(farge) ? "B" : "W";
        return erAngrepet(konge, motstander);
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

