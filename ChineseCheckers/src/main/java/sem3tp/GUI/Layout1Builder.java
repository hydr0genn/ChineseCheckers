package sem3tp.GUI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import sem3tp.Client.GameClient;

import java.io.IOException;

//login

public class Layout1Builder implements Builder<Region> {
    private final Runnable sceneSwapper;
    private GameClient gameClient;

    public Layout1Builder(Runnable sceneSwapper, GameClient gameClient) {
        this.sceneSwapper = sceneSwapper;
    }

    @Override
    public Region build() {

        Label label = new Label("Witaj, podaj nazwe yś:");
        TextField textField = new TextField();
        Button button = new Button("Submit");
        button.setOnAction(evt -> sceneSwapper.run()

        );
        VBox results = new VBox(20, label, textField, button);
        results.setPadding(new Insets(50));
        return results;
    }
}