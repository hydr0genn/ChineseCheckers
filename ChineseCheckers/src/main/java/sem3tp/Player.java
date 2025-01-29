package sem3tp;

import sem3tp.Board.Colors;

import java.io.Serializable;

public class Player extends User{

    public Player(String username){
        this.username=username;
    }

    public String getReady(){
        if(isReady){
            return "Ready";
        }else {
            return "Not ready";
        }
    }


    public void setReady(boolean ready) {
        isReady = ready;
    }
}
