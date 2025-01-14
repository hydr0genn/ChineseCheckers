package sem3tp.GUI;

import javafx.scene.shape.Polygon;
import sem3tp.Poles.Pole;

import java.awt.*;

public class FXPole extends Polygon  implements Comparable<FXPole>{
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

    public void draw(){
        //TODO implement drawing the pole on the stage using cords of Pole class
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
