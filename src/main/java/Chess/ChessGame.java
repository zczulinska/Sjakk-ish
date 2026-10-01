package Chess;

import java.util.ArrayList;
import java.util.List;

public class ChessGame implements ChessRules{

    private String turn = "W";
    private Board board;
    public boolean gameOver;
    private String winner;
    private boolean patt;
    public ChessFileHandler fileHandler;
    

    public Boolean isGameOver() {
        return gameOver;
    }

    public String getWinner() {
        return winner;
    }


    public ChessGame(){
        this(new Board());
    }

    public ChessGame(Board board){ //starter spillet fra en gitt stilling
        this.board = board;
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

    public Boolean erSjakk(){ //står den som har tur i sjakk
        return board.erISjakk(turn);
    }

    public Boolean erSjakkMatt(){
        return gameOver && winner != null;
    }

    public Boolean erPatt(){
        return patt;
    }

    private boolean harLovligeTrekk(String farge){
        for(int row=0; row<8; row++){
            for(int kol=0; kol<8; kol++){
                Position pos = new Position(row, kol);
                Brikke brikke = board.getBrikke(pos);
                if(brikke != null && brikke.getColor().equals(farge) && !lovligeTrekk(pos).isEmpty()){
                    return true;
                }
            }
        }
        return false;
    }

    private void sjekkOmSpilletErSlutt(){ //kalles etter turbytte: kan den som har tur gjøre noe?
        if(harLovligeTrekk(turn)){
            return;
        }
        gameOver = true;
        if(board.erISjakk(turn)){
            winner = "W".equals(turn) ? "B" : "W"; //sjakkmatt, den som trakk sist vinner
        }
        else{
            patt = true; //remis, ingen vinner
        }
    }

    public List<Position> lovligeTrekk(Position fra){ //brikkens trekk, uten de som setter egen konge i sjakk
        List<Position> lovlige = new ArrayList<>();
        Brikke brikke = board.getBrikke(fra);
        if(brikke == null){
            return lovlige;
        }
        brikke.lovligetrekk(board);
        List<Position> kandidater = new ArrayList<>(brikke.getlovligetrekk()); //kopi, siden sjakksjekken regner ut trekklister på nytt
        for(Position til : kandidater){
            if(!setterEgenKongeISjakk(fra, til)){
                lovlige.add(til);
            }
        }
        return lovlige;
    }

    private boolean setterEgenKongeISjakk(Position fra, Position til){ //prøver trekket, sjekker sjakk og setter brettet tilbake
        Brikke brikke = board.getBrikke(fra);
        Brikke slått = board.getBrikke(til);

        board.movePiece(fra, til);
        boolean iSjakk = board.erISjakk(brikke.getColor());

        board.movePiece(til, fra);
        if(slått != null){
            board.settBrikke(slått);
        }
        return iSjakk;
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
        if(!brikke.getColor().equals(turn)){
            return false;
        }

        if(!lovligeTrekk(fra).contains(til)){
            return false;
        }
        //Trekket er gyldig, så nå gjennomfører vi trekket
        String color = board.getBrikke(fra).getColor();

        board.movePiece(fra, til);
        brikke.setBrukt();

        try {
        fileHandler.skrivTrekk(fra, til, color);
        } catch (Exception e) {e.printStackTrace();} //skriver ut hva som gikk galt

        PlayerTurnChange();
        sjekkOmSpilletErSlutt();
        return true;
        
    }        

}

