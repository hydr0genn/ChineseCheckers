package sem3tp;

import sem3tp.Board.Colors;

import java.io.Serializable;

public class Player implements Comparable<Player>, Serializable {
    private String username;
    public Colors playerColor;
    boolean isReady = false;
    public Player(String username){
        this.username=username;
    }
    public boolean hasWon = false;
    
    public String getReady(){
        if(isReady){
            return "Ready";
        }else {
            return "Not ready";
        }
    }

    public boolean isReady() {
        return isReady;
    }

    public void setHasWon(){
        hasWon = true;
    }

    public boolean getHasWon(){
        return hasWon;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  Player player){
            return getUsername().equals(player.getUsername());
        }
        return false;
    }

    @Override
    public int compareTo(Player o) {
        return 0;
    }
}
