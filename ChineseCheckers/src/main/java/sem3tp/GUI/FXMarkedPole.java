package sem3tp.GUI;

import javafx.scene.shape.Circle;
import sem3tp.Poles.Pole;
import sem3tp.Storage.FXMarkedPoleStorage;
import sem3tp.Storage.FXPoleStorage;

public class FXMarkedPole extends Circle implements Comparable<FXMarkedPole> {
    private FXPole fxPole;
    private FXMarkedPoleStorage otherMoves;
    private FXPole parent;


    public FXPole getPoleParent() {
        return parent;
    }

    public void setParent(FXPole parent) {
        this.parent = parent;
    }

    public void setOtherMoves(FXMarkedPoleStorage otherMoves) {
        this.otherMoves = otherMoves;
    }

    public FXMarkedPoleStorage getOtherMoves() {
        return otherMoves;
    }

    public void setPole(FXPole pole) {
        this.fxPole = pole;
    }

    public FXPole getPole() {
        return fxPole;
    }


    public void draw(){
        //TODO implement drawing the pole on the stage using cords of Pole class
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FXMarkedPole fxpole){
            return fxPole.equals(fxpole.fxPole);
        } else if (obj instanceof Pole newpole) {
            return fxPole.equals(newpole);
        }
        return false;
    }

    @Override
    public int compareTo(FXMarkedPole o) {
        return fxPole.getPole().compareTo(o.fxPole.getPole());
    }
}
