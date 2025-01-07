package sem3tp.Poles;

import sem3tp.Board.Triangle;

//*The approach could be to ignore y and z cord and just work on x-cord
// - the last n  poles would be the poles adjacent to the base
// of the board(where n is the number of layers)*//
public class TrianglePole extends Pole {
    private Triangle parent;
    public int i;
    public int j;

    public void setParent(Triangle parent) {
        this.parent = parent;
    }

    public Triangle getParent() {
        return parent;
    }

    public TrianglePole(int xCord, int yCord, int zCord){
        this.setxCord(xCord);
        this.setyCord(yCord);
        this.setzCord(zCord);
    }

    public TrianglePole(int i, int j){
        this.i=i;
        this.j=j;
    }

//    public TrianglePole(int xCord, int yCord, int zCord) {
//        this.setxCord(xCord);
//        this.setyCord(yCord);
//        this.setzCord(zCord);
//    }

    public void exitTriangle(){//
//        if (getxCord())
    }
}
