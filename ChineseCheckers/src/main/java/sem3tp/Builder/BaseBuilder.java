package sem3tp.Builder;

import sem3tp.Board.BoardBase;
import sem3tp.Creator.Creator;
import sem3tp.Mover.Directions;
import sem3tp.Mover.Mover;
import sem3tp.Poles.Pole;
import sem3tp.Storage.PoleStorage;

public class BaseBuilder implements Builder{
    int layers;
    Creator creator;
    Mover mover;
    int maxPoles;
    Directions[] possibleMoves = {Directions.East,Directions.West,Directions.NorthEast, Directions.NorthWest, Directions.SouthEast, Directions.SouthWest};

    public BaseBuilder(int layers){
    creator=Creator.getInstance();
    mover=Mover.getInstance();
    this.layers=layers;
    }

    public void findMaxPoles(int layers_num){
        int suma=1;
        for(int i=2;i<=layers_num;i++){
            suma+=6*(i-1);
        }
        this.maxPoles=suma;
    }

    @Override
    public BoardBase build() {
        BoardBase boardBase = new BoardBase();
        PoleStorage poleList = new PoleStorage();
        findMaxPoles(this.layers);
        Pole current;
        poleList.insert(creator.createInitPole());
        for(int i=0;i< maxPoles;i++){
            current=poleList.getByIndex(i);
            addNeighbours(current, poleList);//kwestia iteratora potencjalnego
        }
        boardBase.setStorage(poleList);
        return boardBase;
    }

    public void addNeighbours(Pole currentPole, PoleStorage poleStorage){
        for (Directions direction : possibleMoves) {
            Pole temp = creator.createBasePole(currentPole, direction, this.layers);
            if(temp==null){
                continue;}
            if (poleStorage.contains(temp)) {
                Pole existingPole = poleStorage.get(temp);
                currentPole.addNeighbour(existingPole);
            } else {
                poleStorage.insert(temp);
                currentPole.addNeighbour(temp);
            }
        }
    }
}
