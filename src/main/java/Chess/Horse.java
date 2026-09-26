package Chess;

import java.util.ArrayList;
import java.util.List;

public class Horse extends Brikke{

    //private boolean brukt;
    private List<Position> muligetrekk = new ArrayList<>();

    public Horse(String color,Position position) {
        super(color,position);
    } 

    @Override
    public void lovligetrekk(Board board) {
        muligetrekk.clear();

        try{
            Position mulig0 = new Position(getPosition().getrow()+2,getPosition().getcol()-1);
            if((board.getBrikke(mulig0) != null && !getColor().equals(board.getBrikke(mulig0).getColor())) || board.getBrikke(mulig0) == null){
                muligetrekk.add(mulig0);}
            }
            catch (IllegalArgumentException e){}

        try{Position mulig1 = new Position(getPosition().getrow()+2,getPosition().getcol()+1);
            if((board.getBrikke(mulig1) != null && !getColor().equals(board.getBrikke(mulig1).getColor())) || board.getBrikke(mulig1) == null){
                muligetrekk.add(mulig1);}
        }
            catch (IllegalArgumentException e){}

        try{Position mulig2 = new Position(getPosition().getrow()+1,getPosition().getcol()+2);
            if((board.getBrikke(mulig2) != null && !getColor().equals(board.getBrikke(mulig2).getColor())) || board.getBrikke(mulig2) == null){
                muligetrekk.add(mulig2);}
        }
            catch (IllegalArgumentException e){}

        try{Position mulig3 = new Position(getPosition().getrow()-1,getPosition().getcol()+2);
            if((board.getBrikke(mulig3) != null && !getColor().equals(board.getBrikke(mulig3).getColor())) || board.getBrikke(mulig3) == null){
                muligetrekk.add(mulig3);}
        }
            catch (IllegalArgumentException e){}

        try{Position mulig4 = new Position(getPosition().getrow()-2,getPosition().getcol()+1);
            if((board.getBrikke(mulig4) != null && !getColor().equals(board.getBrikke(mulig4).getColor())) || board.getBrikke(mulig4) == null){
                muligetrekk.add(mulig4);}
        }
            catch (IllegalArgumentException e){}

        try{Position mulig5 = new Position(getPosition().getrow()-2,getPosition().getcol()-1);
            if((board.getBrikke(mulig5) != null && !getColor().equals(board.getBrikke(mulig5).getColor())) || board.getBrikke(mulig5) == null){
                muligetrekk.add(mulig5);}
        }
            catch (IllegalArgumentException e){}

        try{Position mulig6 = new Position(getPosition().getrow()+1,getPosition().getcol()-2);
            if((board.getBrikke(mulig6) != null && !getColor().equals(board.getBrikke(mulig6).getColor())) || board.getBrikke(mulig6) == null){
                muligetrekk.add(mulig6);
            }
        }
            catch (IllegalArgumentException e){}
        
        try{Position mulig7 = new Position(getPosition().getrow()-1,getPosition().getcol()-2);
            if((board.getBrikke(mulig7) != null && !getColor().equals(board.getBrikke(mulig7).getColor())) || board.getBrikke(mulig7) == null){
                muligetrekk.add(mulig7);}
        }
            catch (IllegalArgumentException e){}
    }

    public void setBrukt(){
    }

    @Override
    public List<Position> getlovligetrekk() {
        return muligetrekk;
    }

    @Override
    public String getImagePath() {
        if("W".equals(getColor())){
            return "/Chess/horsewhite.png";
        }
        return "/Chess/horse.png";
    }

}
