package sem3tp.Storage;

import sem3tp.GUI.FXPole;
import sem3tp.Poles.Pole;

import java.util.ArrayList;
import java.util.List;

public class PoleStorage extends  Storage<Pole>{
    public PoleStorage(){
        this.list=new ArrayList<Pole>();
    }

    /*Only to be used after the allPoles storage is completed*/
    public FXPoleStorage initializeAllFXPoles() {
        FXPoleStorage allFXPoles = new FXPoleStorage();
        for (Pole pole: this.getAll()){
            allFXPoles.insert(new FXPole(pole));
        }
        return allFXPoles;
    }

    @Override
    public boolean contains(Pole o) {
        return super.contains(o);
    }

    @Override
    public int getSize() {
        return super.getSize();
    }

    @Override
    public void insert(Pole pole) {
        super.insert(pole);
    }

    @Override
    public Pole get(Pole pole) {
        return super.get(pole);
    }

    @Override
    public void delete(Pole pole) {
        super.delete(pole);
    }

    @Override
    public ArrayList<Pole> getAll() {
        return list;
    }

    @Override
    public void update(Pole pole) {
        super.update(pole);
    }
}
