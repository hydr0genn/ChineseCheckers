package sem3tp.Poles;

import sem3tp.Board.Colors;
import sem3tp.Creator.Creator;
import sem3tp.Mover.Directions;
import sem3tp.Storage.PoleStorage;

import java.util.ArrayList;

public abstract class Pole implements Comparable<Pole>{
    private int xCord, yCord, zCord;
    private Colors color = null;
//    private int warstwa;
//    private int max;
    PoleStorage neighbours = new PoleStorage();


    public void setColor(Colors color) {
        this.color = color;
    }

    public Colors getColor() {
        return color;
    }

    public void setzCord(int zCord) {
        this.zCord = zCord;
    }

    public int getzCord() {
        return zCord;
    }

    public void setxCord(int xCord) {
        this.xCord = xCord;
    }
    public void setyCord(int yCord) {
        this.yCord = yCord;
    }

    public int getxCord() {
        return xCord;
    }

    public int getyCord() {
        return yCord;
    }

    public Pole getNeighbourByDirection(Directions direction){
        Creator creator = Creator.getInstance();
        return creator.createNeighbour(this,direction);
    }

    public Directions getDirectionOfNeighbour(Pole pole){
        Creator creator = Creator.getInstance();
        Directions tempDirection = Directions.West;
        for (int i=0;i<5;i++){
            if(pole.equals(getNeighbourByDirection(tempDirection))){
                return tempDirection;
            }
            tempDirection = tempDirection.nextDirection;
        }
        return null;
    }

    public PoleStorage getNeighbours() {
        return neighbours;
    }

    public void addNeighbour(Pole pole){
        if(!neighbours.contains(pole))this.neighbours.insert(pole);
//        pole.neighbours.add(this);
    }

    @Override
    public int compareTo(Pole pole2) {
        return Double.compare(Math.sqrt(Math.pow(this.getxCord(),2)+Math.pow(this.getyCord(),2))+Math.pow(this.getzCord(),2), Math.sqrt(Math.pow(pole2.getxCord(),2)+Math.pow(pole2.getyCord(),2)+Math.pow(pole2.getyCord(),2)));
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Pole temp){
            return temp.getxCord() == this.xCord && temp.getyCord() == this.yCord && temp.getzCord()==this.zCord;
        }
        return false;
    }

}
