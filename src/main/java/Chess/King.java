package Chess;

import java.util.ArrayList;
import java.util.List;

public class King extends Brikke{

    private List<Position> muligetrekk = new ArrayList<>();

    public King(String color, Position position){
        super(color,position);
    }

    @Override
    public void lovligetrekk(Board board) {
        muligetrekk.clear();

        for(int i=0; i<3;i++){
            try{
                Position mulig = new Position(getPosition().getrow()+1,getPosition().getcol()-1+i);
                if(board.getBrikke(mulig) == null || (board.getBrikke(mulig) != null && !board.getBrikke(mulig).getColor().equals(getColor()))){
                    muligetrekk.add(mulig);
                }
                
            }
            catch(IllegalArgumentException e){}

            try{
                Position mulig = new Position(getPosition().getrow()-1,getPosition().getcol()-1+i);
                if(board.getBrikke(mulig) == null || (board.getBrikke(mulig) != null && !board.getBrikke(mulig).getColor().equals(getColor()))){
                    muligetrekk.add(mulig);
                }
            }
            catch(IllegalArgumentException e){}
        }

        try{
                Position mulig = new Position(getPosition().getrow(),getPosition().getcol()-1);
                if(board.getBrikke(mulig) == null || (board.getBrikke(mulig) != null && !board.getBrikke(mulig).getColor().equals(getColor()))){
                    muligetrekk.add(mulig);
                }
            }
            catch(IllegalArgumentException e){}

        try{
                Position mulig = new Position(getPosition().getrow(),getPosition().getcol()+1);
                if(board.getBrikke(mulig) == null || (board.getBrikke(mulig) != null && !board.getBrikke(mulig).getColor().equals(getColor()))){
                    muligetrekk.add(mulig);
                }
            }
            catch(IllegalArgumentException e){}
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
            return "/Chess/kingwhite.png";
        }
        return "/Chess/king.png";
    }

}
