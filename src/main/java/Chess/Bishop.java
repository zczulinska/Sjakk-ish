package Chess;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Brikke{

    private List<Position> muligetrekk = new ArrayList<>();

    public Bishop(String color,Position position) {
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
            return "/Chess/bishopwhitepng.png";
        }
        return "/Chess/bishop.png";
    }

}
