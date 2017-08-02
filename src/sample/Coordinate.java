package sample;

/**
 * Created by smrut on 5/20/2017.
 */
public class Coordinate {
    private int row;
    private int col;
    public Coordinate(int r, int c){
        row=r;
        col=c;
    }
    public int getRow(){return row;}
    public int getCol(){return col;}
    public boolean equals(Object other){
        if(!(other instanceof Coordinate)){
            return false;
        }
        else{
            Coordinate co=(Coordinate)other;
            return((this.row==co.getRow())&&(this.col==co.getCol()));
        }
    }
    public int hashCode(){
        return row*21+col;
    }
    @Override
    public String toString() {
        return "(Row: " + row + ", Col: " + col + ")";
    }

}
