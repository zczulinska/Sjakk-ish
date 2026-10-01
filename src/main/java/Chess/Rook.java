package Chess;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Brikke{

    private boolean brukt; //har flyttet, brukes for rokade
    private List<Position> muligetrekk=new ArrayList<>();

    public Rook(String color, Position position){
        super(color,position);
    }

    @Override
    public void lovligetrekk(Board board) {
        muligetrekk.clear();

        for(int i=1;i<8;i++){//høyre
            try{
                Position mulig = new Position(getPosition().getrow(),getPosition().getcol()+i);
                if(board.getBrikke(mulig) != null && !getColor().equals(board.getBrikke(mulig).getColor())){
                    //hvis det er brikke på posisjonen, men brikken er motsatt farge
                    muligetrekk.add(mulig);
                    break;
                }
                else if(board.getBrikke(mulig)==null){
                    //hvis det ikke er brikke
                    muligetrekk.add(mulig);
                }
                else if(board.getBrikke(mulig) != null && getColor().equals(board.getBrikke(mulig).getColor())){
                    break;
                }
            }
            catch(IllegalArgumentException e){}
        }

        for(int i=1;i<8;i++){//venstre
            try{
                Position mulig = new Position(getPosition().getrow(),getPosition().getcol()-i);
                if(board.getBrikke(mulig) != null && !getColor().equals(board.getBrikke(mulig).getColor())){
                    //hvis det er brikke på posisjonen, men brikken er motsatt farge
                    muligetrekk.add(mulig);
                    break;
                }
                else if(board.getBrikke(mulig)==null){
                    //hvis det ikke er brikke
                    muligetrekk.add(mulig);
                }
                else if(board.getBrikke(mulig) != null && getColor().equals(board.getBrikke(mulig).getColor())){
                    break;
                }
            }
            catch(IllegalArgumentException e){}
        }

        for(int i=1;i<8;i++){//opp
            try{
                Position mulig = new Position(getPosition().getrow()-i,getPosition().getcol());
                if(board.getBrikke(mulig) != null && !getColor().equals(board.getBrikke(mulig).getColor())){
                    //hvis det er brikke på posisjonen, men brikken er motsatt farge
                    muligetrekk.add(mulig);
                    break;
                }
                else if(board.getBrikke(mulig)==null){
                    //hvis det ikke er brikke
                    muligetrekk.add(mulig);
                }
                else if(board.getBrikke(mulig) != null && getColor().equals(board.getBrikke(mulig).getColor())){
                    break;
                }
            }
            catch(IllegalArgumentException e){}
        }

        for(int i=1;i<8;i++){//ned
            try{
                Position mulig = new Position(getPosition().getrow()+i,getPosition().getcol());
                if(board.getBrikke(mulig) != null && !getColor().equals(board.getBrikke(mulig).getColor())){
                    //hvis det er brikke på posisjonen, men brikken er motsatt farge
                    muligetrekk.add(mulig);
                    break;
                }
                else if(board.getBrikke(mulig)==null){
                    //hvis det ikke er brikke
                    muligetrekk.add(mulig);
                }
                else if(board.getBrikke(mulig) != null && getColor().equals(board.getBrikke(mulig).getColor())){
                    break;
                }
            }
            catch(IllegalArgumentException e){}
        }
        
        //     for(int i=0; i<8;i++){//vannrett først
        //         if(i==getPosition().getcol()){
        //             continue;
        //         }
        //         else {
        //             Position mulig = new Position(getPosition().getrow(),i);
        //             muligetrekk.add(mulig);
        //         }
        //     }
        //     for(int i=0; i<8;i++){//loddrett 
        //         if(i==getPosition().getrow()){
        //             continue;
        //         }
        //         else {
        //             Position mulig = new Position(i,getPosition().getcol());
        //             muligetrekk.add(mulig);
        //         }
        // }
    }

    @Override
    public List<Position> getlovligetrekk() {
        return muligetrekk;
    }

    @Override
    public String getImagePath() {
        if("W".equals(getColor())){
            return "/Chess/rookwhite.png";
        }
        return "/Chess/rook.png";
    }

    @Override
    public void setBrukt() {
        this.brukt=true;
    }

    public boolean getBrukt(){
        return brukt;
    }

}
