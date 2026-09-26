package Chess;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Brikke{

    private List<Position> muligetrekk = new ArrayList<>();

    public Queen(String color, Position position){
        super(color,position);
    }

    @Override
    public void lovligetrekk(Board board) {

        muligetrekk.clear();

        //skrå ned høyre
        for(int i=1; i<8; i++){
            try{
                Position mulig = new Position(getPosition().getrow()+i,getPosition().getcol()+i);
                if(board.getBrikke(mulig)==null){
                    muligetrekk.add(mulig);
                }
                else if(!board.getBrikke(mulig).getColor().equals(getColor())){//hvis det er brikke,så sjekker om fargen er motsatt
                    muligetrekk.add(mulig); //hvis ja så legger til trekk
                    break;
                }
                else {
                    break;
                }     
            }
            catch(IllegalArgumentException e){}//ikke gyldig posisjon
        }

        //skrå ned venstre
        for(int i=1; i<8; i++){
            try{
                Position mulig = new Position(getPosition().getrow()+i,getPosition().getcol()-i); //sjekker om gyldig posisjon
                if(board.getBrikke(mulig)==null){//sjekker om det står en brikke der
                    muligetrekk.add(mulig); //hvis ikke så legger til mulig trekk
                }
                else if(!board.getBrikke(mulig).getColor().equals(getColor())){//hvis det er brikke,så sjekker om fargen er motsatt
                    muligetrekk.add(mulig); //hvis ja så legger til trekk
                    break;
                }   
                else {
                    break;
                }     
            }
            catch(IllegalArgumentException e){}//ikke gyldig posisjon
        }

        //skrå opp til høyre
        for(int i=1; i<8; i++){
            try{
                Position mulig = new Position(getPosition().getrow()-i,getPosition().getcol()+i); //sjekker om gyldig posisjon
                if(board.getBrikke(mulig)==null){//sjekker om det står en brikke der
                    muligetrekk.add(mulig); //hvis ikke så legger til mulig trekk
                }
                else if(!board.getBrikke(mulig).getColor().equals(getColor())){//hvis det er brikke,så sjekker om fargen er motsatt
                    muligetrekk.add(mulig); //hvis ja så legger til trekk
                    break;
                }   
                else {
                    break;
                }     
            }
            catch(IllegalArgumentException e){}//ikke gyldig posisjon
        }

        //skrå opp til venstre
        for(int i=1; i<8; i++){
            try{
                Position mulig = new Position(getPosition().getrow()-i,getPosition().getcol()-i); //sjekker om gyldig posisjon
                if(board.getBrikke(mulig)==null){//sjekker om det står en brikke der
                    muligetrekk.add(mulig); //hvis ikke så legger til mulig trekk
                }
                else if(!board.getBrikke(mulig).getColor().equals(getColor())){//hvis det er brikke,så sjekker om fargen er motsatt
                    muligetrekk.add(mulig); //hvis ja så legger til trekk
                    break;
                }   
                else {
                    break;
                }     
            }
            catch(IllegalArgumentException e){}//ikke gyldig posisjon
        }


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

        

        // for(int i=-7;i<8;i++){
        //     if(i==0){continue;}
        //     try{
        //         //skrå høyre 
        //         Position mulig = new Position(getPosition().getrow()+i,getPosition().getcol()+i);
        //         muligetrekk.add(mulig);
        //     }
        //     catch (IllegalArgumentException e){}

        //     try{
        //         //skrå venstre
        //         Position mulig = new Position(getPosition().getrow()+i,getPosition().getcol()-i);
        //         muligetrekk.add(mulig);
        //     }
        //     catch (IllegalArgumentException e){}
        // }

        // for(int i=0; i<8;i++){//vannrett først
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


    public void setBrukt(){
    }
    
    @Override
    public List<Position> getlovligetrekk() {
        return muligetrekk;
    }

    @Override
    public String getImagePath() {
        if("W".equals(getColor())){
            return "/Chess/queenwhite.png";
        }
        return "/Chess/queen.png";
    }

}
