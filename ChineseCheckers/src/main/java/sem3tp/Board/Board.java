package sem3tp.Board;

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

    public FXPoleStorage getAllFXPolesOfColor(Colors color){
        FXPoleStorage fxPoles = new FXPoleStorage();
        for(FXPole fxpole : allFXPoles.getAll()){
            if(fxpole.FXColor==color) {
                fxPoles.insert(fxpole);
            }
        }
        return fxPoles;
    }

    public void setAllFXPoles(FXPoleStorage allFXPoles) {
        this.allFXPoles = allFXPoles;
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
