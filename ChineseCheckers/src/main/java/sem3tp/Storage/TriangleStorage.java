package sem3tp.Storage;

import sem3tp.Board.Triangle;
import sem3tp.Poles.Pole;

import java.util.ArrayList;

public class TriangleStorage extends Storage<Triangle> {
    public TriangleStorage(){
        this.list=new ArrayList<Triangle>();
    }
}
