package sem3tp.Board;

import sem3tp.Poles.Pole;
import sem3tp.Storage.PoleStorage;
import sem3tp.Storage.Storage;

public abstract class Components implements ComponentInterface{
    PoleStorage poleList;

    public void setStorage(PoleStorage poleList){
        this.poleList=poleList;
    }

    public PoleStorage getStorage() {
        return poleList;
    }
}
