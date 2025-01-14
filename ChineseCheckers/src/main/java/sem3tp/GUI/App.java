package sem3tp.GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sem3tp.Game;

public class App extends Application {
    Game game;
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
