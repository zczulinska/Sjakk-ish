package Chess;

public class ChessGame implements ChessRules{

    private String turn = "W";
    private Board board;
    public boolean gameOver;
    private String winner;
    public ChessFileHandler fileHandler;
    

    public Boolean isGameOver() {
        return gameOver;
    }

    public String getWinner() {
        return winner;
    }


    public ChessGame(){
        this.board = new Board();
        this.fileHandler = new ChessFileHandler();
        try{
        fileHandler.nullstillFiler();}
        catch(Exception e){e.printStackTrace();}
    }

    

    public Board getBoard(){
        return board;
    }

    @Override
    public void PlayerTurnChange() {
        if(turn.equals("W")){
            turn="B";
        }
        else{
            turn="W";
        }
        
    }

    public String getTurn(){
        return turn;
    }

    public void erSjakkMatt(){
        int sjekk = 0;
        for(int row=0;row<8;row++){
            for(int kol=0; kol<8; kol++){
                Brikke brikke = board.getBrikke(new Position(row, kol));
                if(brikke instanceof King){
                    sjekk+=1;
                }
            }
        }
        if(sjekk<2){
            gameOver=true;
        }
    }

    public Boolean Move(Position fra, Position til) { //sjekker om det er et gyldig move, og da gjennomfører den bevegelsen. 
        //sjekke om det er et gyldig move
        if(gameOver){
            return false;
        }

        Brikke brikke = board.getBrikke(fra);

        if(brikke == null){
            return false;
        }
        brikke.lovligetrekk(board); 
        if(!brikke.getColor().equals(turn)){
            return false;
        }

        if(!brikke.getlovligetrekk().contains(til)){
            return false;
        }
        //Trekket er gyldig, så nå gjennomfører vi trekket
        String color = board.getBrikke(fra).getColor();

        board.movePiece(fra, til);
        brikke.setBrukt();

        try {
        fileHandler.skrivTrekk(fra, til, color);
        } catch (Exception e) {e.printStackTrace();} //skriver ut hva som gikk galt

        erSjakkMatt();
        if (gameOver) {
            winner = turn;}
        
        PlayerTurnChange();
        return true;
        
    }        

}

