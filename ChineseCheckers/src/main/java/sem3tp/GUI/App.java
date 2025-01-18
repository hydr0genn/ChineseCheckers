package sem3tp.GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sem3tp.Client.ClientHandler;
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
    public void start(Stage primaryStage) throws Exception {
        new FirstStage();
    }


    class FirstStage extends Stage{
        Button openKlient = new Button("Otworz klienta");
        HBox x = new HBox();

        FirstStage(){
            x.getChildren().add(openKlient);
            this.setScene(new Scene(x, 300, 300));
            this.show();

            openKlient.setOnAction(t -> new SecondStage());
        }
    }

    class SecondStage extends Stage {
        SecondStage(){
            ClientHandler handler = new ClientHandler();
            new Thread(handler).start();
            //handler.setClient(this);
            // handler.run();
            this.setTitle("Chinese Checkers");
            this.setHeight(500);
            this.setWidth(500);
            this.setResizable(false);
            this.show();
            this.setScene(new Scene(new WrapperLayoutBuilder(handler).build()));
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}
