package sem3tp.Builder;

import sem3tp.Board.*;
import sem3tp.Creator.Creator;
import sem3tp.Poles.Pole;
import sem3tp.Poles.StandardPole;
import sem3tp.Storage.TriangleStorage;

import java.util.HashMap;
import java.util.Map;

public class BoardBuilder {

    private int playersNumber;
    private int layers;
    private Creator creator=Creator.getInstance();
    private BoardBase base;

    public BoardBuilder(int layers, int playersNumber){
        this.layers=layers;
        this.playersNumber=playersNumber;
    }

    public HashMap<Colors,Pole> initialiseCharacteristicPoles(int number){
        HashMap<Colors, Pole> crucialPoints=new HashMap<>();
        crucialPoints.put(Colors.Black,this.base.getStorage().get(new StandardPole(0,-(layers-1),layers-1)));
        crucialPoints.put(Colors.White,this.base.getStorage().get(new StandardPole(0,(layers-1),-(layers-1))));
        crucialPoints.put(Colors.Yellow,this.base.getStorage().get(new StandardPole((layers-1),-(layers-1),0)));
        crucialPoints.put(Colors.Yellow.getOppositeColor(),this.base.getStorage().get(new StandardPole(-(layers-1),(layers-1),0)));
        crucialPoints.put(Colors.Blue,this.base.getStorage().get(new StandardPole((layers-1),0,-(layers-1))));
        crucialPoints.put(Colors.Blue.getOppositeColor(),this.base.getStorage().get(new StandardPole(-(layers-1),0,(layers-1))));
        return crucialPoints;
    }

    public void initialiseTriangle(Map.Entry<Colors, Pole> entry, TriangleStorage triangleStorage){
        Colors color = entry.getKey();
        Pole specialPole = entry.getValue();
        Triangle triangle = creator.createTriangleBuilder(layers,color,specialPole,base).build();
        triangleStorage.insert(triangle);
    }

    /*Returns storage of triangles - first version: only those triangles needed
    * second version: all triangles*/
    TriangleStorage getTriangles(){
        TriangleStorage triangleStorage = creator.createTriangleStorage();
        HashMap<Colors, Pole> crucialPoints = initialiseCharacteristicPoles(this.playersNumber);
        for (Map.Entry<Colors, Pole> entry : crucialPoints.entrySet()) {
            initialiseTriangle(entry, triangleStorage);
            }
        return triangleStorage;
    }

    public Board build() {
        Board board = new Board();
        BaseBuilder baseBuilder = creator.createBaseBuilder(layers);
        board.setBase(baseBuilder.build());
        this.base= board.getBase();
        board.setTriangles(getTriangles());

        /*we create a storage combining base storage and triangle storages*/
        board.setAllPoles(base.getStorage());
        for(Triangle triangle: board.getTriangles().getAll()){
            for(Pole pole:triangle.getStorage().getAll()){
                board.getAllPoles().insert(pole);
            }
        }
        board.setAllFXPoles(board.getAllPoles().initializeAllFXPoles());

        return board;
    }
}
