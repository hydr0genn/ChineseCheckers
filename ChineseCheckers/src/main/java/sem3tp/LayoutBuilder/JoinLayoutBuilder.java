package sem3tp.LayoutBuilder;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import sem3tp.Client.ClientHandler;
import sem3tp.GUI.ViewModeler;

public class JoinLayoutBuilder implements Builder<Region> {
    private final Runnable sceneSwapper;
    public ClientHandler handler;

    public JoinLayoutBuilder(Runnable sceneSwapper, ClientHandler handler) {
        this.sceneSwapper = sceneSwapper;
        this.handler = handler;
    }

    @Override
    public Region build() {
//        Button button = new Button("Change to Scene 1");
//        button.setOnAction(evt -> sceneSwapper.run());
        Button joinButton = new Button("Dołącz do gry o id:");
        Label iloscGraczyLabel = new Label("podaj id");
        TextField iloscGraczy = new TextField();

        ViewModeler viewModeler = new ViewModeler();

        viewModeler.initializeJoinGameButton(handler, joinButton, iloscGraczy, sceneSwapper);


        VBox results = new VBox(20, new Label("Dołączanie"), joinButton, iloscGraczyLabel, iloscGraczy);
        results.setPadding(new Insets(50));
        return results;
    }
}