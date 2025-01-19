package sem3tp.GUI;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import sem3tp.Storage.FXMarkedPoleStorage;

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

    public void setPole(FXPole pole) {
        this.fxPole = pole;
    }

    public FXPole getPole() {
        return fxPole;
    }


    public Circle draw(){
        double x = 250 + 25 * (getPole().getPole().getxCord() + 0.5 * getPole().getPole().getyCord());
        double y = 250 + 25 * (-1) * Math.sqrt(3)/2 * getPole().getPole().getyCord();
        setCenterX(x);
        setCenterY(y);
        setRadius(5);
        setFill(Color.web("#FFD700"));


        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FXMarkedPole fxpole) {
            return fxPole.equals(fxpole.fxPole);
        }
        return false;
    }

    @Override
    public int compareTo(FXMarkedPole o) {
        return fxPole.getPole().compareTo(o.fxPole.getPole());
    }
}
