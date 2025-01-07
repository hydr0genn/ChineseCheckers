package sem3tp.Mover;

import sem3tp.Creator.Creator;
import sem3tp.Exceptions.WrongDirectionInput;
import sem3tp.Poles.Pole;
import sem3tp.Poles.StandardPole;

public class Mover {
    Creator create = Creator.getInstance();

    private static Mover instance;

    public static Mover getInstance() {
        if(instance==null){
            synchronized (Creator.class) {
                if(instance==null){
                    instance= new Mover();
                }
            }
        }
        return instance;
    }

    public Directions setDirection(String direction){
        return switch (direction) {
            case "E" -> Directions.East;
            case "W" -> Directions.West;
            case "NE" -> Directions.NorthEast;
            case "NW" -> Directions.NorthWest;
            case "SW" -> Directions.SouthWest;
            case "SE" -> Directions.SouthEast;
            default -> throw new WrongDirectionInput("U must type: E W SW SE NW NE");
        };
    }

//    private Mover(int layers){
//        this.layers=layers;
//    }

    private boolean isWithinBoundary(int x, int y, int z, int layers){
        return Math.abs(x)<layers && Math.abs(y)<layers && Math.abs(z)<layers;
    }

    public Pole move(Pole pole, Directions direction, int layers){
        int newX = pole.getxCord()+ direction.addXCord();
        int newY = pole.getyCord() + direction.addYCord();
        int newZ = pole.getzCord()+ direction.addZCord();
        if(isWithinBoundary(newX,newY,newZ, layers)) {return create.createSTDPole(newX, newY, newZ);}
        else {

        }
        return null;
    }
}
