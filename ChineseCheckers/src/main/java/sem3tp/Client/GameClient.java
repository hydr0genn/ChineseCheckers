package sem3tp.Client;

import sem3tp.GUI.FXPole;
import sem3tp.Game;
import sem3tp.Mover.Mover;
import sem3tp.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {

    private static final String SERVER_ADDRESS = "localhost"; 
    private static final int SERVER_PORT = 1989; 
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Scanner consoleInput;
    private Player user;
    private Game game;

    private void move(FXPole source, FXPole destination){
        Mover mover = Mover.getInstance();
        mover.move(source, destination, game);
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

    public void run() throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
//            consoleInput = new Scanner(System.in);

            System.out.println("Połączono się z serwerem");

            while (true) {
                String serverMessage = (String) in.readObject();
                if(serverMessage.startsWith("SNDPLAYR")){
                    this.user= (Player) in.readObject();
                }
                if(serverMessage.startsWith("CRTDGAME")){
                    this.game= (Game) in.readObject();
                }
                if(serverMessage.startsWith("oJOINGME")){
                    this.game= (Game) in.readObject();
                }
                if(serverMessage.equals("SSNDMOVE")){
                    FXPole source = (FXPole) in.readObject();
                    FXPole destination = (FXPole) in.readObject();
                    move(source,destination);
                }
            }
        } finally {
            //TODO end
        }
    }

    public static void main(String[] args) {
        try {
            GameClient client = new GameClient();
            client.run();
        } catch (IOException e) {
            System.err.println("Nie udało się połączyć z serwerem, upewnij się, ze działa yś");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}