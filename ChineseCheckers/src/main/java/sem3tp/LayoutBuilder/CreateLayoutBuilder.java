package sem3tp.LayoutBuilder;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import sem3tp.Client.ClientHandler;
import sem3tp.GUI.ViewModeler;

import java.io.IOException;
import java.util.Set;

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

        Set<String> dozwoloneLiczby = Set.of("2", "3", "4", "6");
        ViewModeler viewModeler = new ViewModeler();


        viewModeler.initializeChooseJoinGameButton(joinGameButton, sceneSwapper);


        Button CreateVariant1Button = new Button("stwórz gre 1 wariantu");
        Button CreateVariant2Button = new Button("stwórz gre 2 wariantu");
        Label iloscGraczyLabel = new Label("podaj ilosc graczy");
        TextField iloscGraczy = new TextField();
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        iloscGraczy.textProperty().addListener((observable, oldValue, newValue) -> {
            if (dozwoloneLiczby.contains(newValue)) {
                errorLabel.setText("");
            } else {
                errorLabel.setText("Zła liczba");
            }
        });

        TextField loadGameField = new TextField();

        Button loadGameButton = new Button("Wczytaj gre");

        HBox loud = new HBox(loadGameField, loadGameButton);


        viewModeler.initializeCreateGameButtonVariant1(handler, CreateVariant1Button, iloscGraczy);
        viewModeler.initializeCreateGameButtonVariant2(handler, CreateVariant2Button, iloscGraczy);
        viewModeler.initializeJoinGameButton(handler, loadGameButton, loadGameField);


        VBox results = new VBox(20, new Label("Tworzenie gry"), joinGameButton, loud, CreateVariant1Button, CreateVariant2Button, iloscGraczyLabel, iloscGraczy, errorLabel);
        results.setPadding(new Insets(50));
        return results;
    }
}