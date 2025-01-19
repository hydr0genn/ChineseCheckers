package sem3tp.GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sem3tp.Client.ClientHandler;
import sem3tp.LayoutBuilder.WrapperLayoutBuilder;

public class App extends Application {

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
            ClientHandler handler = new ClientHandler(this);
            new Thread(handler).start();
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
