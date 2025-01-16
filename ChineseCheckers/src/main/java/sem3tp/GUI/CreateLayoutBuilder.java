package sem3tp.GUI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import sem3tp.Client.ClientHandler;

//select, create game

public class CreateLayoutBuilder implements Builder<Region> {
    private final Runnable sceneSwapper;
    public ClientHandler handler;

    public CreateLayoutBuilder(Runnable sceneSwapper, ClientHandler handler) {
        this.sceneSwapper = sceneSwapper;
        this.handler = handler;
    }


    @Override
    public Region build() {
        Button joinGameButton = new Button("Dołącz do gry");
        Button createGameButton = new Button("Stwórz gre");

        ViewModeler viewModeler = new ViewModeler();

        //viewModeler.initializeChooseCreateGameButton(handler, createGameButton);

        viewModeler.initializeChooseJoinGameButton(joinGameButton, sceneSwapper);


        Button gameCreatorButton = new Button("stwórz gre");
        TextField iloscGraczy = new TextField("podaj ilosc graczy");


        viewModeler.initializeCreateGameButton(handler, gameCreatorButton, iloscGraczy);


        Button button = new Button("Change to Scene 1");
        button.setOnAction(evt -> sceneSwapper.run());
        VBox results = new VBox(20, new Label("Tworzenie gry"), button, joinGameButton, gameCreatorButton, iloscGraczy);
        results.setPadding(new Insets(50));
        return results;
    }
}