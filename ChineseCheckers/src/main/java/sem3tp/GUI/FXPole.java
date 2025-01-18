package sem3tp.GUI;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import sem3tp.Poles.Pole;

import java.awt.*;
import java.io.Serializable;

public class FXPole extends Circle implements Comparable<FXPole>, Serializable {
    private Pole pole;

    public void setPole(Pole pole) {
        this.pole = pole;
    }

    public Pole getPole() {
        return pole;
    }

    public FXPole(Pole pole){
        this.pole=pole;
    }

    public Circle draw(){
        double x = 250 + 5 * (getPole().getxCord() + 0.5 * getPole().getyCord());
        double y = 250 + 5 * 1.5 * getPole().getyCord();
        setCenterX(x);
        setCenterY(y);
        setRadius(10);
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FXPole fxpole){
            return pole.equals(fxpole.pole);
        } else if (obj instanceof Pole newpole) {
            return pole.equals(newpole);
        }
        return false;
    }

    @Override
    public int compareTo(FXPole o) {
        return pole.compareTo(o.pole);
    }
}
