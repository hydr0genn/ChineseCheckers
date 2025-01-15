package sem3tp.GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sem3tp.Client.GameClient;
import sem3tp.Game;

import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class App extends Application {
    private Game game;
    GameClient client;
//    private Scanner in;
//    private PrintWriter out;
//
//    public App(Scanner in, PrintWriter out){
//        this.in=in;
//        this.out=out;
//    }

    public GameClient getClient() {
        return client;
    }

    public void setClient(GameClient client) {
        this.client = client;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new VBox(10),400,150);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
