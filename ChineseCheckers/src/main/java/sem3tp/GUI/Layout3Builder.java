package sem3tp.GUI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

public class Layout3Builder implements Builder<Region> {
    private final Runnable sceneSwapper;

    public Layout3Builder(Runnable sceneSwapper) {
        this.sceneSwapper = sceneSwapper;
    }

    @Override
    public Region build() {
        Button button = new Button("Change to Scene 1");
        button.setOnAction(evt -> sceneSwapper.run());
        VBox results = new VBox(20, new Label("Welcome to Scene 3"), button);
        results.setPadding(new Insets(50));
        return results;
    }
}