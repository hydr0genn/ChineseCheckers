package sem3tp;

import java.io.Serializable;

public class Player implements Comparable<Player>, Serializable {
    private String username;
    boolean isReady = false;
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

    public boolean isReady() {
        return isReady;
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
