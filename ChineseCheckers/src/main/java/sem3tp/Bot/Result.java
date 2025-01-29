package sem3tp.Bot;

import sem3tp.GUI.FXPole;
import sem3tp.Poles.Pole;

public class Result implements Comparable<Result>{
    public FXPole fxPole;
    public double distance;

    public Result(double distance, FXPole pole){
        this.distance=distance;
        this.fxPole=pole;
    }


    @Override
    public int compareTo(Result o) {
        return Double.compare(this.distance,o.distance);
    }
}
