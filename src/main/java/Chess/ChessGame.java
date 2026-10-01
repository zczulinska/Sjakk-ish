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
        if(brikke instanceof King konge){
            kandidater.addAll(rokadeTrekk(konge)); //ligger her og ikke i King, ellers ville angrepssjekken kalt seg selv i det uendelige
        }
        for(Position til : kandidater){
            if(!setterEgenKongeISjakk(fra, til)){
                lovlige.add(til);
            }
        }
        return lovlige;
    }

    private List<Position> rokadeTrekk(King konge){ //rutene kongen kan rokere til
        List<Position> trekk = new ArrayList<>();
        String farge = konge.getColor();
        String motstander = "W".equals(farge) ? "B" : "W";
        int rad = konge.getPosition().getrow();

        if(konge.getBrukt() || konge.getPosition().getcol() != 4 || board.erISjakk(farge)){
            return trekk;
        }

        //kort rokade: tårnet i kolonne 7, kongen går til kolonne 6
        if(kanRokereMed(new Position(rad, 7), farge)
            && erTomOgTrygg(new Position(rad, 5), motstander)
            && erTomOgTrygg(new Position(rad, 6), motstander)){
            trekk.add(new Position(rad, 6));
        }

        //lang rokade: tårnet i kolonne 0, kongen går til kolonne 2. Rute 1 må være tom, men kan være angrepet
        if(kanRokereMed(new Position(rad, 0), farge)
            && board.getBrikke(new Position(rad, 1)) == null
            && erTomOgTrygg(new Position(rad, 2), motstander)
            && erTomOgTrygg(new Position(rad, 3), motstander)){
            trekk.add(new Position(rad, 2));
        }
        return trekk;
    }

    private boolean kanRokereMed(Position tårnRute, String farge){ //står det et tårn av riktig farge som ikke har flyttet
        return board.getBrikke(tårnRute) instanceof Rook tårn
            && tårn.getColor().equals(farge)
            && !tårn.getBrukt();
    }

    private boolean erTomOgTrygg(Position rute, String motstander){
        return board.getBrikke(rute) == null && !board.erAngrepet(rute, motstander);
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

        if(brikke instanceof King && Math.abs(til.getcol() - fra.getcol()) == 2){ //rokade, tårnet flytter også
            int rad = fra.getrow();
            Position tårnFra = til.getcol() == 6 ? new Position(rad, 7) : new Position(rad, 0);
            Position tårnTil = til.getcol() == 6 ? new Position(rad, 5) : new Position(rad, 3);
            board.movePiece(tårnFra, tårnTil);
            board.getBrikke(tårnTil).setBrukt();
        }

        try {
        fileHandler.skrivTrekk(fra, til, color);
        } catch (Exception e) {e.printStackTrace();} //skriver ut hva som gikk galt

        PlayerTurnChange();
        sjekkOmSpilletErSlutt();
        return true;
        
    }        

}

