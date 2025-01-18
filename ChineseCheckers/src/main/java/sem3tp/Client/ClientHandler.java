package sem3tp.Client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import sem3tp.GUI.FXPole;
import sem3tp.LayoutBuilder.WrapperLayoutBuilder;
import sem3tp.Game;
import sem3tp.Mover.Mover;
import sem3tp.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean running = true;
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1989;
    private HashSet<Integer> ids;
    private Player user;
    private Game game;
    private Stage stage;

    public void setUser(Player user) {
        this.user = user;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public Player getUser() {
        return user;
    }

    private void move(FXPole source, FXPole destination){
        Mover mover = Mover.getInstance();
        mover.move(source, destination, getGame());
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

    public ClientHandler(Stage stage) {
        this.stage=stage;
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Pane createGameScene(){
        Pane pane = new Pane();
        for (FXPole fxPole :game.getBoard().getAllFXPoles().getAll()){
            pane.getChildren().add(fxPole.draw());
        }
        return pane;
    }

    @Override
    public void run() {
        try {
            System.out.println("Połączono się z serwerem");

            while (running) {
                String serverMessage = (String) in.readObject();
                if(serverMessage.startsWith("SNDPLAYR")){
                    setUser((Player) in.readObject());
                }
                if(serverMessage.startsWith("CRTDGAME")){
                   setGame((Game) in.readObject());
                }
                if(serverMessage.startsWith("oJOINGME")){
                     setGame((Game) in.readObject());
                }
                if(serverMessage.equals("SSNDMOVE")){
                    FXPole source = (FXPole) in.readObject();
                    FXPole destination = (FXPole) in.readObject();
                    move(source,destination);
                }
                if(serverMessage.equals("CHNGTURN")){
                    getGame().nextTurn();
                }
                if (serverMessage.equals("PLYRJIND")){
                    Player newPlayer = (Player) in.readObject();
                    getGame().addNewPlayer(newPlayer);
                }
                if (serverMessage.equals("SNDGAMES")){
                    @SuppressWarnings("unchecked")
                    HashSet<Integer> ids = (HashSet<Integer>) in.readObject();
                    this.ids=ids;
                }
                if(serverMessage.equals("TURNONXX")){
                    System.out.println("dotarlem");
                    getGame().turnOn();
                    System.out.println(getGame().isOn());
                    Pane pane = createGameScene();
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
