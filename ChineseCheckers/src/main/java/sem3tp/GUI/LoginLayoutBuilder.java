package sem3tp.GUI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import sem3tp.Client.ClientHandler;

//login

public class LoginLayoutBuilder implements Builder<Region> {
    private final Runnable sceneSwapper;
    public ClientHandler handler;

    public void setClient(ClientHandler handler) {
        this.handler = handler;
    }

    public LoginLayoutBuilder(Runnable sceneSwapper, ClientHandler handler) {
        this.sceneSwapper = sceneSwapper;
        this.handler = handler;
    }

    @Override
    public Region build() {

        Label label = new Label("Witaj, podaj nazwe yś:");
        TextField textField = new TextField();
        Button button = new Button("Submit");

        ViewModeler viewModeler = new ViewModeler();

        //button.setOnAction(evt -> sceneSwapper.run());
        viewModeler.initializeLoginButton(handler, button, textField, sceneSwapper);


        VBox results = new VBox(20, label, textField, button);
        results.setPadding(new Insets(50));
        return results;
    }
}