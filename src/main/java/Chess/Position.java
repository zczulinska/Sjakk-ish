package Chess;

public class Position {

    private int row = -1;
    private int col = -1;

    public Position(int row, int col){
        if(row>7 || row<0 || col>7 || col<0){throw new IllegalArgumentException("Ikke gyldig posisjon på brettet");}
        this.row=row;
        this.col=col;
    }

    public int getrow(){
        return row;
    }

    public int getcol(){
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position other = (Position) o;
        return this.getrow() == other.getrow() && this.getcol() == other.getcol();
    }

    @Override
    public int hashCode() {
    return 31 * getrow() + getcol();
    }
}
