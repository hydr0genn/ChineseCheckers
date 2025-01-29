package sem3tp.Storage;

import sem3tp.GUI.FXPole;

import java.util.ArrayList;

public class FXPoleStorage extends Storage<FXPole>{
    public FXPoleStorage(){
        this.list=new ArrayList<FXPole>();
    }

    @Override
    public FXPole get(FXPole fxpole){
        for(FXPole object:this.getAll()){
            if(fxpole.equals(object)){
                return object;
            }
        }
        return null;
    }


}
