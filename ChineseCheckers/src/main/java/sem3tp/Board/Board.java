package sem3tp.Board;

import sem3tp.GUI.FXMarkedPole;
import sem3tp.GUI.FXPole;
import sem3tp.Poles.Pole;
import sem3tp.Storage.FXMarkedPoleStorage;
import sem3tp.Storage.FXPoleStorage;
import sem3tp.Storage.PoleStorage;
import sem3tp.Storage.TriangleStorage;

import java.io.Serializable;

public class Board implements Serializable {
    public FXMarkedPoleStorage markedPoleStorage = new FXMarkedPoleStorage();
    private BoardBase base;
    private TriangleStorage triangles;
    private PoleStorage allPoles;
    private FXPoleStorage allFXPoles;
/*Only to be used after the allPoles storage is completed*/
    public void initializeAllFXPoles() {
        allFXPoles = new FXPoleStorage();
        for (Pole pole: allPoles.getAll()){
            allFXPoles.insert(new FXPole(pole));
        }
    }

    public FXPoleStorage getAllFXPoles() {
        return allFXPoles;
    }

    public void setAllPoles(PoleStorage allPoles) {
        this.allPoles = allPoles;
    }

    public PoleStorage getAllPoles() {
        return allPoles;
    }

    public void setBase(BoardBase base) {
        this.base = base;
    }

    public BoardBase getBase() {
        return base;
    }

    public TriangleStorage getTriangles() {
        return triangles;
    }

    public void setTriangles(TriangleStorage triangles) {
        this.triangles = triangles;
    }

}
