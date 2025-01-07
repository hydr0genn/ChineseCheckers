package sem3tp.Mover;

import sem3tp.Board.Board;
import sem3tp.Board.Triangle;
import sem3tp.Creator.Creator;
import sem3tp.Exceptions.WrongDirectionInput;
import sem3tp.Poles.Pole;
import sem3tp.Poles.StandardPole;
import sem3tp.Poles.TrianglePole;
import sem3tp.Storage.PoleStorage;
import sem3tp.Storage.Storage;
import sem3tp.Storage.TriangleStorage;

import java.util.ArrayList;

public class Mover {
    Creator creator = Creator.getInstance();

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

    public boolean inTriangle(Pole pole, Board board){
        return board.getBase().getStorage().contains(pole);
    }

    public boolean inBase(Pole pole, Board board){
        TriangleStorage triangleStorage = board.getTriangles();
        for(Triangle triangle: triangleStorage.getAll()){
            if(triangle.getStorage().contains(pole)){
                return true;
            }
        }
        return false;
    }

    /*CHECKS WHETHER THERE IS ENENMY AROUND and returns the list of poles with enemies
    * verifies all neighbour. checking its size allows it to function as boolean
    * if size==0 it means that there ain't no enemies around*/
    private PoleStorage isNearEnemy(Pole currentPole){
        PoleStorage temp = creator.createPoleStorage();
        for (Pole pole:currentPole.getNeighbours().getAll()){
            if (pole.getColor()!=null&&pole.getColor()!=currentPole.getColor()){
                temp.insert(pole);
            }
        }
        return temp;
    }

    /* ASSUMING INCORRECT MOVES CANNOT BE DONE else - such logic should be included*/
    /*Returns a pole next to given pole (in a given direction). Requires a polestorage either
    * from a triangle or a base */
    public Pole getNeighbour(Pole pole, Directions direction, Board board){
        int newX = pole.getxCord()+ direction.addXCord();
        int newY = pole.getyCord() + direction.addYCord();
        int newZ = pole.getzCord()+ direction.addZCord();
        return board.getAllPoles().get(creator.createSTDPole(newX,newY,newZ));
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

    /*GENERAL MOVE FUNCTION
    * from here it is divded into two possibilities: move inside the base or from base to a triangle -*/
    public void move(Pole currentPole, Directions direction, Board board){
        Pole potentialPole = getNeighbour(currentPole, direction, board);
        if(inBase(potentialPole, board)) standardMove(currentPole, direction, potentialPole);
        else moveToTriangle(currentPole, direction);
    }

    /*Standard move without 'crossing borders'*/
    private void standardMove(Pole currentPole, Directions direction, Pole potentialPole){
            //TODO logika plus co my robimy z biciem pionkow? czy bedziemy to tu rozwazac czy bardziej serwer ma od razu to pokazywac
    }

    /*Move from base to triangle MOZE BYC TAK ZE TO JEST USELESS XD*/
    private  void moveToTriangle(Pole currentPole,Directions direction){
        //TODO Zastanowic sie nad sensem tej funkcji (czy musimy wogole to rozdzielac) i ewentualna logika
    }
}
