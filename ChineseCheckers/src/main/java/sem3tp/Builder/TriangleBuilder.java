package sem3tp.Builder;

import sem3tp.Board.BoardBase;
import sem3tp.Board.Colors;
import sem3tp.Board.Triangle;
import sem3tp.Creator.Creator;
import sem3tp.Mover.Directions;
import sem3tp.Mover.Mover;
import sem3tp.Poles.Pole;
import sem3tp.Poles.TrianglePole;
import sem3tp.Storage.PoleStorage;

public class TriangleBuilder implements Builder{

    private Colors color;
    private Creator creator;
    private int layers;
    private Pole pole;
    private BoardBase base;

    public TriangleBuilder(int layers, Colors color, Pole pole, BoardBase base){
        creator= Creator.getInstance();
        this.layers=layers;
        this.color=color;
        this.pole=pole;
        this.base=base;
    }

//    /*Adding a neighbour logic - it is responsible for creating a triangle pole
//    * it does not verify whether one should be created*/
//    private TrianglePole findNeighbour(Pole current, Directions direction){
//        int newX = current.getxCord()+direction.addXCord();
//        int newY = current.getyCord()+direction.addYCord();
//        int newZ = current.getzCord()+direction.addZCord();
//        return creator.createTrianglePole(newX,newY,newZ);
//    }

    /*Loop responsible for creating poles inside the triangle, return a storage of triangle poles
    it receives a source and for each pole it
    Color's got property direction - existing solely for the purpose of creating a board -
    for example for Black in order to create a traingle of such color
     */
    public PoleStorage createTrianglePoleStorage(Pole source, int layers){
        PoleStorage temp = new PoleStorage();
        temp.insert(source);
        TrianglePole current = (TrianglePole) source;
        current.setColor(color);
        int limit = (layers-1)*layers/2;

        while(temp.getSize()<limit){
            TrianglePole potential;
            potential =creator.createNeighbour(current,color.getDirectionCreate().nextDirection);
            if(!temp.contains(potential)){
                potential.setColor(color);
                temp.insert(potential);
            }
            potential = creator.createNeighbour(current, color.getDirectionCreate());
            if(!temp.contains(potential))
            {
                potential.setColor(color);
                temp.insert(potential);
            }
            current = (TrianglePole) temp.getByIndex(temp.indexOf(current)+1);
        }
        return temp;
    }

    void connectPoles(PoleStorage polesInTriangle, PoleStorage polesInBase){
        for (int i=0; i<polesInTriangle.getSize();i++){
            Pole current = polesInTriangle.getByIndex(i);
            Directions currentDirection = color.getDirectionCreate();
            for(int j=0;j<6;j++){
                TrianglePole potential = creator.createNeighbour(current,currentDirection);
                if(polesInTriangle.contains(potential)){
                    current.addNeighbour(polesInTriangle.get(potential));
                }
                else if(j>1 && polesInBase.contains(potential)){
                    Pole stdpole = polesInBase.get(potential);
                    current.addNeighbour(stdpole);
                    stdpole.addNeighbour(current);
                }
                currentDirection = currentDirection.nextDirection;
            }
        }
    }

    @Override
    public Triangle build() {

        PoleStorage listOfPoles = createTrianglePoleStorage(creator.createNeighbour(pole,color.getDirectionCreate()), layers);
        connectPoles(listOfPoles,base.getStorage());

        Triangle triangle = creator.createTriangle();
        triangle.setStorage(listOfPoles);
        triangle.setColor(color);

        return triangle;
    }
}
