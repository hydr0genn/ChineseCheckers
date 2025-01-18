package sem3tp.GUI;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import sem3tp.Board.Colors;
import sem3tp.Poles.Pole;

import java.awt.*;
import java.io.Serializable;

public class FXPole extends Circle implements Comparable<FXPole>, Serializable {
    private Pole pole;
    public Colors BorderColor;
    public Colors FXColor;

    public void setPole(Pole pole) {
        this.pole = pole;
    }

    public Pole getPole() {
        return pole;
    }

    public Colors getFXColor() {
        return FXColor;
    }

    public void setColor(Colors color){
        this.pole.setColor(color);
        this.FXColor=color;
        setFill(color.getVisualColor());
    }

    public FXPole(Pole pole){
        this.pole=pole;
        this.BorderColor=pole.getColor();
        this.FXColor=pole.getColor();
    }

    public Circle draw(){
        double x = 250 + 25 * (getPole().getxCord() + 0.5 * getPole().getyCord());
        double y = 250 + 25 * (-1) * Math.sqrt(3)/2 * getPole().getyCord();
        setCenterX(x);
        setCenterY(y);
        setRadius(9);
        setFill(getPole().getColor().getVisualColor());
        setStroke(getPole().getColor().getBorderColor());
        setStrokeWidth(2);


        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FXPole fxpole){
            return pole.equals(fxpole.pole);
        }
        return false;
    }

    @Override
    public int compareTo(FXPole o) {
        return pole.compareTo(o.pole);
    }
}
