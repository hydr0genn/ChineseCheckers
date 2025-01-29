package sem3tp.Storage;

import sem3tp.Board.Colors;
import sem3tp.Board.Triangle;

import java.util.ArrayList;

public class TriangleStorage extends Storage<Triangle> {
    public TriangleStorage(){
        this.list=new ArrayList<Triangle>();
    }

    public Triangle getByColor(Colors color){
        for(Triangle x: this.getAll()){
            if(x.getColor()==color){
                return x;
            }
        }
        throw new RuntimeException("Triangle not found");
    }
}
