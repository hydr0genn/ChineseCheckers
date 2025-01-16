package sem3tp.GUI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import sem3tp.Client.ClientHandler;
import sem3tp.Player;

public class LobbyLayoutBuilder implements Builder<Region> {
    private final Runnable sceneSwapper;
    public ClientHandler handler;

    public LobbyLayoutBuilder(Runnable sceneSwapper, ClientHandler handler) {
        this.sceneSwapper = sceneSwapper;
        this.handler = handler;
    }

    @Override
    public Region build() {
        Button ready = new Button("Ready");
        Button unready = new Button("Unready");
        //button.setOnAction(evt -> sceneSwapper.run());
        Label czekanie = new Label("Czekanie na start gry");

        ViewModeler viewModeler = new ViewModeler();
        Player player = handler.getClient().getUser();
        viewModeler.initializeReadyButton(handler, ready, player);

        viewModeler.initializeNotReadyButton(handler, unready, player);






        VBox results = new VBox(20, czekanie, ready, unready);
        results.setPadding(new Insets(50));
        return results;
    }
}
