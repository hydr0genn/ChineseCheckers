package sem3tp.GUI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

//select, create game

public class Layout2Builder implements Builder<Region> {
    private final Runnable sceneSwapper;

    public Layout2Builder(Runnable sceneSwapper) {
        this.sceneSwapper = sceneSwapper;
    }

    @Override
    public Region build() {
        Button joinGameButton = new Button("Dołącz do gry");
        Button createGameButton = new Button("Stwórz gre");


        Button button = new Button("Change to Scene 1");
        button.setOnAction(evt -> sceneSwapper.run());
        VBox results = new VBox(20, new Label("Welcome to Scene 2"), button);
        results.setPadding(new Insets(50));
        return results;
    }
}