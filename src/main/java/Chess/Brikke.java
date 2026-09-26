package Chess;

import java.util.List;

public abstract class Brikke {

    private String color;
    private Position position;
    private List<Position> getlovligetrekk;

    public Brikke(String color,Position position) {
        if(!"W".equals(color) && !"B".equals(color)){throw new IllegalArgumentException("Det er ikke gyldig farge");}
        this.color = color;
        this.position=position;
    }

    public String getColor() {
        return color;
    }

    public void setPosition(Position position){
        this.position=position;
    }

    public Position getPosition(){
        return position;
    }

    public List<Position> getlovligetrekk(){
        return this.getlovligetrekk;
    }

    public abstract void lovligetrekk(Board board); 

    public abstract String getImagePath();

    public abstract void setBrukt();

}
