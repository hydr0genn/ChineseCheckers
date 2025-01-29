package sem3tp.LayoutBuilder;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import sem3tp.Client.ClientHandler;
import sem3tp.GUI.ViewModeler;

import java.io.IOException;

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
        Button bot = new Button("Add a bot");

        bot.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    handler.sendMessageString("ADDBOTXX");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Label czekanie = new Label("Czekanie na start gry");

        ViewModeler viewModeler = new ViewModeler();
        viewModeler.initializeReadyButton(handler, ready);

        viewModeler.initializeNotReadyButton(handler, unready);


        VBox results = new VBox(20, czekanie, ready, unready, bot);
        results.setPadding(new Insets(50));
        return results;
    }
}
