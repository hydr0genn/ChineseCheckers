package sem3tp.Client;

import sem3tp.GUI.FXPole;
import sem3tp.Game;
import sem3tp.Mover.Mover;
import sem3tp.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientHandler implements Runnable{
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean running = true;
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1989;
    private GameClient client;

    public void setClient(GameClient client) {
        this.client = client;
    }

    public GameClient getClient() {
        return client;
    }

    private void move(FXPole source, FXPole destination){
        Mover mover = Mover.getInstance();
        mover.move(source, destination, client.getGame());
    }

    public void sendMove(String prefix,Object source, Object destination) throws IOException {
        out.writeObject(prefix);
        out.writeObject(source);
        out.writeObject(destination);
        out.flush();
        move((FXPole) source,(FXPole) destination);
    }

    public void sendMessageObject(String prefix,Object object) throws IOException {
        out.writeObject(prefix);
        out.writeObject(object);
        out.flush();
    }

    public void sendMessageString(String message) throws IOException {
        out.writeObject(message);
        out.flush();
    }

    public ClientHandler(GameClient client) {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("chuj1");
            out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("chuj");
            in = new ObjectInputStream(socket.getInputStream());
            this.client = client;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
//        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
//            this.in = new ObjectInputStream(socket.getInputStream());
//            this.out = new ObjectOutputStream(socket.getOutputStream());
//
            System.out.println("Połączono się z serwerem");

            while (running) {
                String serverMessage = (String) in.readObject();
                if(serverMessage.startsWith("SNDPLAYR")){
                    this.client.setUser((Player) in.readObject());
                }
                if(serverMessage.startsWith("CRTDGAME")){
                    this.client.setGame((Game) in.readObject());
                }
                if(serverMessage.startsWith("oJOINGME")){
                     this.client.setGame((Game) in.readObject());
                }
                if(serverMessage.equals("SSNDMOVE")){
                    FXPole source = (FXPole) in.readObject();
                    FXPole destination = (FXPole) in.readObject();
                    move(source,destination);
                }
                if(serverMessage.equals("CHNGTURN")){
                    client.getGame().nextTurn();
                }
                if (serverMessage.equals("PLYRJIND")){
                    Player newPlayer = (Player) in.readObject();
                    client.getGame().addNewPlayer(newPlayer);
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
