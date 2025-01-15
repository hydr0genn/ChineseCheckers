package sem3tp;

public class User implements Comparable<User>{
    private String username;
    private String password;
    //BoardStorage maybeś

    public User(String username, String pwd){
        this.setUsername(username);
        this.setPassword(pwd);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof User newUser){
            return this.username.equals(newUser.getUsername());
        }
        return false;
    }

    @Override
    public int compareTo(User o) {
        return 0;
    }
}
