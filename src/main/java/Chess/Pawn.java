package Chess;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Brikke{

    private boolean brukt;
    private List<Position> muligetrekk = new ArrayList<>();

    public Pawn(String color,Position position){
        super(color,position);
    }

    @Override
    public void lovligetrekk(Board board) {
        muligetrekk.clear();

        if("W".equals(getColor())){

            try{Position mulig = new Position(getPosition().getrow()+1,getPosition().getcol());
                if(board.getBrikke(mulig) == null){
                muligetrekk.add(mulig);
                if(!brukt){
                    Position mulig1 = new Position(getPosition().getrow()+2,getPosition().getcol());
                        if(board.getBrikke(mulig1) == null){
                        muligetrekk.add(mulig1);}
                    }
                }
            }
                catch(IllegalArgumentException e){}
                      
                
            }
                   
                //Og om de som står der er hvit eller svart
                //Jeg må også legge til at den kan slå skrått   
                
        else{
            try{Position mulig = new Position(getPosition().getrow()-1,getPosition().getcol());
                if(board.getBrikke(mulig) == null){
                muligetrekk.add(mulig);
                if(!brukt){
                    Position mulig1 = new Position(getPosition().getrow()-2,getPosition().getcol());
                        if(board.getBrikke(mulig1) == null){
                        muligetrekk.add(mulig1);}
                        }
                    }
                }
                    catch(IllegalArgumentException e){}
                      
                }
            angrep(board);
            }
    

    public void angrep(Board board){
        if("B".equals(getColor())){
        try{
        Position offer1 = new Position(getPosition().getrow()-1,getPosition().getcol()+1);
        if(kanSlå(board, offer1)){
            muligetrekk.add(offer1);
        }
        }
        catch(IllegalArgumentException e){}

        try{
        Position offer2 = new Position(getPosition().getrow()-1,getPosition().getcol()-1);
        if(kanSlå(board, offer2)){
            muligetrekk.add(offer2);
        }
        }
        catch(IllegalArgumentException e){}
    }
    else{
        try{
        Position offer1 = new Position(getPosition().getrow()+1,getPosition().getcol()+1);
        if(kanSlå(board, offer1)){
            muligetrekk.add(offer1);
        }
        }
        catch(IllegalArgumentException e){}

        try{
        Position offer2 = new Position(getPosition().getrow()+1,getPosition().getcol()-1);
        if(kanSlå(board, offer2)){
            muligetrekk.add(offer2);
        }
        }
        catch(IllegalArgumentException e){}
    }

    }
    

    private boolean kanSlå(Board board, Position offer){ //motstanderbrikke på ruten, eller en passant
        Brikke brikke = board.getBrikke(offer);
        if(brikke != null){
            return !getColor().equals(brikke.getColor());
        }
        return erEnPassant(board, offer);
    }

    public boolean erEnPassant(Board board, Position offer){ //ruten er den en motstanderbonde akkurat hoppet over med dobbelsteg
        if(!offer.equals(board.getEnPassantRute())){
            return false;
        }
        Brikke vedSiden = board.getBrikke(new Position(getPosition().getrow(), offer.getcol())); //bonden som slås står ved siden av
        return vedSiden instanceof Pawn && !getColor().equals(vedSiden.getColor());
    }

    public boolean angriper(Position rute){ //bonden angriper skrått fremover, også når ruten er tom
        int retning = "W".equals(getColor()) ? 1 : -1;
        return rute.getrow() == getPosition().getrow() + retning
            && Math.abs(rute.getcol() - getPosition().getcol()) == 1;
    }

    @Override
    public List<Position> getlovligetrekk() {
        return muligetrekk;
        
    }


    @Override
    public String getImagePath() {
        if("W".equals(getColor())){return "/Chess/pawnwhite.png";}
        return "/Chess/pawn.png";
    }

    public void setBrukt(){
        this.brukt=true;
    }

    public boolean getBrukt(){
        return brukt;
    }
}
