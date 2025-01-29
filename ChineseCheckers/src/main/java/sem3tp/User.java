package sem3tp;

import sem3tp.Board.Colors;

import java.io.Serializable;

public abstract class User implements Comparable<Player>, Serializable {
    protected String username;
    public Colors playerColor;
    protected boolean isReady = false;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isReady() {
        return isReady;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  User player){
            return getUsername().equals(player.getUsername());
        }
        return false;
    }

    @Override
    public int compareTo(Player o) {
        return 0;
    }
}
