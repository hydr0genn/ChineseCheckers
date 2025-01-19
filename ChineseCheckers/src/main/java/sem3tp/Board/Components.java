package sem3tp.Board;

import sem3tp.Storage.PoleStorage;

import java.io.Serializable;

public abstract class Components implements ComponentInterface, Serializable {
    PoleStorage poleList;

    public void setStorage(PoleStorage poleList){
        this.poleList=poleList;
    }

    public PoleStorage getStorage() {
        return poleList;
    }
}
