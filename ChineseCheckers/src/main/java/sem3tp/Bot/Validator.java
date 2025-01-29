package sem3tp.Bot;

import sem3tp.Board.Colors;
import sem3tp.Board.Triangle;
import sem3tp.Creator.Creator;
import sem3tp.GUI.FXPole;
import sem3tp.Mover.Directions;
import sem3tp.Mover.Variants;
import sem3tp.Poles.InitPole;
import sem3tp.Poles.Pole;
import sem3tp.Poles.StandardPole;
import sem3tp.Poles.TrianglePole;
import sem3tp.Storage.FXPoleStorage;
import sem3tp.Storage.PoleStorage;

import java.util.concurrent.TransferQueue;

public class Validator {

    private static Validator instance;

    private Validator(){}

    public static Validator getInstance() {
        if(instance==null){
            synchronized (Validator.class) {
                if(instance==null){
                    instance= new Validator();
                }
            }
        }
        return instance;
    }

    public FXPoleStorage findPossibleJumps(FXPole fxpole, FXPoleStorage allFXPoles, Variants variant, FXPoleStorage parents){
        FXPoleStorage neighbourList = new FXPoleStorage();
        if (fxpole.getPole() instanceof TrianglePole currentPole){
            for (Pole neighbour : currentPole.getNeighbours().getAll()){
                if(neighbour instanceof TrianglePole){ //inside a triangle
                    Pole potentialPole = checkForJump(neighbour,currentPole, variant);
                    if(potentialPole==null)continue;
                    FXPole actualPole = allFXPoles.get(new FXPole(potentialPole));
                    if(actualPole!=null&&hasJumped(fxpole,actualPole)){
                        neighbourList.insert(actualPole);
                    }
                }else if(neighbour instanceof StandardPole){//from triangle to baseboard
                    Pole potentialPole = checkForJump(neighbour,currentPole, variant);
                    if(potentialPole==null)continue;
                    FXPole actualPole = allFXPoles.get(new FXPole(potentialPole));
                    if(actualPole!=null&&hasJumped(fxpole,actualPole)&&(currentPole.getColor()==fxpole.BorderColor)){
                        neighbourList.insert(actualPole);
                    }
                }
            }
        }else if(fxpole.getPole() instanceof StandardPole currentPole){
            for(Pole neighbour : currentPole.getNeighbours().getAll()){
                if(neighbour instanceof TrianglePole triangleNeighbour){
                    Pole potentialPole = checkForJump(triangleNeighbour,currentPole, variant);
                    if(potentialPole==null)continue;
                    FXPole actualPole = allFXPoles.get(new FXPole(potentialPole));
                    if(actualPole!=null&&(actualPole.BorderColor.getOppositeColor()==currentPole.getColor())&&hasJumped(fxpole,actualPole)){
                        neighbourList.insert(actualPole);
                    }
                }else{
                    Pole potentialPole = checkForJump(neighbour,currentPole, variant);
                    if(potentialPole==null)continue;
                    FXPole actualPole = allFXPoles.get(new FXPole(potentialPole));
                    if(actualPole!=null) {
                        if (actualPole.getPole() instanceof TrianglePole) {
                            if ((actualPole.BorderColor.getOppositeColor() == currentPole.getColor()) && hasJumped(fxpole, actualPole)) {
                                neighbourList.insert(actualPole);
                            }
                        } else {
                            if (hasJumped(fxpole, actualPole)){
                                neighbourList.insert(actualPole);
                            }
                        }
                    }
                }
            }
        }
        for(FXPole fxPole : parents.getAll()){
            neighbourList.delete(fxPole);
        }
        return neighbourList;
    }


    public FXPoleStorage findPossibleMoves(FXPole fxpole, FXPoleStorage allFXPoles, Variants variant){
        FXPoleStorage neighbourList = new FXPoleStorage();
        if (fxpole.getPole() instanceof TrianglePole currentPole){
            TrianglePoleLogic(fxpole,currentPole, variant, allFXPoles, neighbourList);
        }else if(fxpole.getPole() instanceof StandardPole currentPole){
            StdPoleLogic(currentPole, variant, allFXPoles, neighbourList);
        }else if(fxpole.getPole() instanceof InitPole currentPole){
            InitPoleLogic(currentPole, variant, allFXPoles, neighbourList);
        }
        return neighbourList;
    }

    private void TrianglePoleLogic(FXPole fxPole,TrianglePole currentPole, Variants variant, FXPoleStorage allFXPoles, FXPoleStorage neighbourList){
        for (Pole neighbour : currentPole.getNeighbours().getAll()){
            if(neighbour instanceof TrianglePole){ //inside a triangle
                Pole potentialPole = checkForJump(neighbour,currentPole, variant);
                if(potentialPole==null)continue;
                FXPole actualPole = allFXPoles.get(new FXPole(potentialPole));
                if(actualPole!=null){
                    neighbourList.insert(actualPole);
                }
            }else if(neighbour instanceof StandardPole){//from triangle to baseboard
                Pole potentialPole = checkForJump(neighbour,currentPole, variant);
                if(potentialPole==null)continue;
                FXPole actualPole = allFXPoles.get(new FXPole(potentialPole));
                if(actualPole!=null && currentPole.getColor()==fxPole.BorderColor){
                    neighbourList.insert(actualPole);
                }
            }
        }
    }

    private void StdPoleLogic(Pole currentPole, Variants variant, FXPoleStorage allFXPoles, FXPoleStorage neighbourList){
        for(Pole neighbour : currentPole.getNeighbours().getAll()){
            if(neighbour instanceof TrianglePole triangleNeighbour){
                Pole potentialPole = checkForJump(triangleNeighbour,currentPole, variant);
                if(potentialPole==null)continue;
                FXPole actualPole = allFXPoles.get(new FXPole(potentialPole));
                if(actualPole!=null&&(actualPole.BorderColor.getOppositeColor()==currentPole.getColor())){
                    if(actualPole.getPole() instanceof TrianglePole)System.out.println("chuj");
                    neighbourList.insert(actualPole);
                }
            }else{
                Pole potentialPole = checkForJump(neighbour,currentPole, variant);
                if(potentialPole==null)continue;
                FXPole actualPole = allFXPoles.get(new FXPole(potentialPole));
                if(actualPole!=null) {
                    if (actualPole.getPole() instanceof TrianglePole) {
                        if ((actualPole.BorderColor.getOppositeColor() == currentPole.getColor())) {
                            neighbourList.insert(actualPole);
                        }
                    } else {
                        neighbourList.insert(actualPole);
                    }
                }
            }
        }
    }

    private void InitPoleLogic(Pole currentPole, Variants variant, FXPoleStorage allFXPoles, FXPoleStorage neighbourList){
        for(Pole neighbour : currentPole.getNeighbours().getAll()){
            if(neighbour instanceof TrianglePole triangleNeighbour){
                Pole potentialPole = checkForJump(triangleNeighbour,currentPole, variant);
                if(potentialPole==null)continue;
                FXPole actualPole = allFXPoles.get(new FXPole(potentialPole));
                if(actualPole!=null&&(actualPole.BorderColor.getOppositeColor()==currentPole.getColor())){
                    neighbourList.insert(actualPole);
                }
            }else{
                Pole potentialPole = checkForJump(neighbour,currentPole, variant);
                if(potentialPole==null)continue;
                FXPole actualPole = allFXPoles.get(new FXPole(potentialPole));
                if(actualPole!=null){
                    neighbourList.insert(actualPole);
                }
            }
        }
    }

    private Pole jump(Pole pole, Directions direction){
        return pole.getNeighbourByDirection(direction);
    }

    public boolean hasJumped(FXPole parent, FXPole current){
        //if the current pole is a neighbour of our source then no jump has been made
        return !parent.getPole().getNeighbours().contains(current.getPole());
    }

    private Pole checkForJump(Pole pole, Pole source, Variants variant ){//potential variant
        if(isAvailable(pole)){
            return pole;
        }
        Directions direction = source.getDirectionOfNeighbour(pole);
        Pole potential = jump(pole,direction);
        if(isAvailable(potential)){
            return potential;
        }else if(variant==Variants.TwoJumps && potential!=null){
            return checkForJump(potential,pole,Variants.OneJump);
        }
        return null;
    }

    private boolean isAvailable(Pole pole){
        if(pole==null){
            return false;
        }
        return pole.getColor() == Colors.Grey;
    }

}
