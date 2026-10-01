package Chess;

import java.util.ArrayList;
import java.util.List;

public class ChessGame implements ChessRules{

    private String turn = "W";
    private Board board;
    private boolean gameOver;
    private String winner;
    private boolean patt;
    private ChessFileHandler fileHandler;
    

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

    private Position slåttRute(Brikke brikke, Position fra, Position til){ //ruten til brikken som blir slått
        if(brikke instanceof Pawn pawn && pawn.erEnPassant(board, til)){
            return new Position(fra.getrow(), til.getcol());
        }
        return til;
    }

    private boolean setterEgenKongeISjakk(Position fra, Position til){ //prøver trekket, sjekker sjakk og setter brettet tilbake
        Brikke brikke = board.getBrikke(fra);
        Position slåttRute = slåttRute(brikke, fra, til);
        Brikke slått = board.getBrikke(slåttRute);

        board.fjernBrikke(slåttRute); //ved en passant står den slåtte bonden ikke på til-ruten
        board.movePiece(fra, til);
        boolean iSjakk = board.erISjakk(brikke.getColor());

        board.movePiece(til, fra);
        if(slått != null){
            board.settBrikke(slått);
        }
        return iSjakk;
    }

    public boolean erBondeforvandling(Position fra, Position til){ //bonde som når siste rad
        Brikke brikke = board.getBrikke(fra);
        if(!(brikke instanceof Pawn)){
            return false;
        }
        int sisteRad = "W".equals(brikke.getColor()) ? 7 : 0;
        return til.getrow() == sisteRad;
    }

    private Brikke lagForvandling(String forvandling, String farge, Position position){ //lager brikken bonden blir til
        if(forvandling == null){
            throw new IllegalArgumentException("Bonden må bli til en brikke");
        }
        switch(forvandling){
            case "Queen": return new Queen(farge, position);
            case "Rook": return new Rook(farge, position);
            case "Bishop": return new Bishop(farge, position);
            case "Horse": return new Horse(farge, position);
            default: throw new IllegalArgumentException("Bonden kan ikke bli til " + forvandling);
        }
    }

    public Boolean Move(Position fra, Position til) { //bonde som når siste rad blir dronning
        return Move(fra, til, "Queen");
    }

    public Boolean Move(Position fra, Position til, String forvandling) { //sjekker om det er et gyldig move, og da gjennomfører den bevegelsen. 
        lagForvandling(forvandling, "W", til); //kaster unntak med en gang hvis valget er ugyldig

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
        String color = brikke.getColor();
        boolean blirForvandlet = erBondeforvandling(fra, til); //må sjekkes før bonden flyttes

        board.fjernBrikke(slåttRute(brikke, fra, til)); //ved en passant står den slåtte bonden ikke på til-ruten
        board.movePiece(fra, til);
        brikke.setBrukt();

        if(brikke instanceof King && Math.abs(til.getcol() - fra.getcol()) == 2){ //rokade, tårnet flytter også
            int rad = fra.getrow();
            Position tårnFra = til.getcol() == 6 ? new Position(rad, 7) : new Position(rad, 0);
            Position tårnTil = til.getcol() == 6 ? new Position(rad, 5) : new Position(rad, 3);
            board.movePiece(tårnFra, tårnTil);
            board.getBrikke(tårnTil).setBrukt();
        }

        if(blirForvandlet){ //bonden står nå på siste rad og byttes ut
            Brikke ny = lagForvandling(forvandling, color, til);
            ny.setBrukt();
            board.settBrikke(ny);
        }

        if(brikke instanceof Pawn && Math.abs(til.getrow() - fra.getrow()) == 2){ //dobbelsteg, kan slås en passant i neste trekk
            board.setEnPassantRute(new Position((fra.getrow() + til.getrow()) / 2, fra.getcol()));
        }
        else{
            board.setEnPassantRute(null);
        }

        try {
        fileHandler.skrivTrekk(fra, til, color, blirForvandlet ? forvandling : null);
        } catch (Exception e) {e.printStackTrace();} //skriver ut hva som gikk galt

        PlayerTurnChange();
        sjekkOmSpilletErSlutt();
        return true;
        
    }        

}

