package sem3tp;

public class Player {
    String username;
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
}
