package sem3tp.Client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sem3tp.GUI.FXPole;
import sem3tp.GUI.WrapperLayoutBuilder;
import sem3tp.Game;
import sem3tp.Mover.Mover;
import sem3tp.Player;
import sem3tp.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameClient extends Application {

//    private static final String SERVER_ADDRESS = "localhost";
//    private static final int SERVER_PORT = 1989;
//    private ObjectInputStream in;
//    private ObjectOutputStream out;
    private Player user;
    private Game game;

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

//    private void move(FXPole source, FXPole destination){
//        Mover mover = Mover.getInstance();
//        mover.move(source, destination, game);
//    }
//
//    public void sendMove(String prefix,Object source, Object destination) throws IOException {
//        out.writeObject(prefix);
//        out.writeObject(source);
//        out.writeObject(destination);
//        out.flush();
//        move((FXPole) source,(FXPole) destination);
//    }
//
//    public void sendMessageObject(String prefix,Object object) throws IOException {
//        out.writeObject(prefix);
//        out.writeObject(object);
//        out.flush();
//    }
//
//    public void sendMessageString(String message) throws IOException {
//        out.writeObject(message);
//        out.flush();
//    }
//
//    public void run() throws IOException, ClassNotFoundException {
//        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
//            in = new ObjectInputStream(socket.getInputStream());
//            out = new ObjectOutputStream(socket.getOutputStream());
////            consoleInput = new Scanner(System.in);
//
//            System.out.println("Połączono się z serwerem");
//
//            while (true) {
//                String serverMessage = (String) in.readObject();
//                if(serverMessage.startsWith("SNDPLAYR")){
//                    this.user= (Player) in.readObject();
//                }
//                if(serverMessage.startsWith("CRTDGAME")){
//                    this.game= (Game) in.readObject();
//                }
//                if(serverMessage.startsWith("oJOINGME")){
//                    this.game= (Game) in.readObject();
//                }
//                if(serverMessage.equals("SSNDMOVE")){
//                    FXPole source = (FXPole) in.readObject();
//                    FXPole destination = (FXPole) in.readObject();
//                    move(source,destination);
//                }
//                if(serverMessage.equals("CHNGTURN")){
//                    game.nextTurn();
//                }
//                if (serverMessage.equals("PLYRJIND")){
//                    Player newPlayer = (Player) in.readObject();
//                    game.addNewPlayer(newPlayer);
//                }
//            }
//        } finally {
//            //TODO end
//        }
//    }

//    private ObjectInputStream in;
//    private ObjectOutputStream out;
//    private static final String SERVER_ADDRESS = "localhost";
//    private static final int SERVER_PORT = 1989;
//
//    public GameClient() throws IOException {
//        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT
//        this.in = new ObjectInputStream(socket.getInputStream());
//        this.out = new ObjectOutputStream(socket.getOutputStream());
//
//        ClientHandler listener = new ClientHandler();
//
//        listener.start();
//    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("chuj0");
        ClientHandler handler = new ClientHandler(this);
        new Thread(handler).start();
        System.out.println("chuj1");
        //handler.setClient(this);
        System.out.println("chuj2");
       // handler.run();
        System.out.println("chuj3");
        System.out.println("chuj4");
        primaryStage.setTitle("Chinese Checkers");
//        primaryStage.setHeight(1000);
//        primaryStage.setWidth(1000);
        primaryStage.show();
        System.out.println("chuj5");
        primaryStage.setScene(new Scene(new WrapperLayoutBuilder(handler).build()));
    }

    public static void main(String[] args) {
        launch(args);

    }
}