package sem3tp.GUI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.util.Builder;
import sem3tp.Client.ClientHandler;
import sem3tp.Player;
import sem3tp.Storage.FXPoleStorage;

public class GameLayoutBuilder implements Builder<Region> {
    private final Runnable sceneSwapper;
    public ClientHandler handler;

    public GameLayoutBuilder(Runnable sceneSwapper, ClientHandler handler) {
        this.sceneSwapper = sceneSwapper;
        this.handler = handler;
    }

    @Override
    public Region build() {
        Pane pane = new Pane();
        ViewModeler viewModeler = new ViewModeler();
//        FXPoleStorage fxstorage = handler.getGame().getBoard().getAllFXPoles();
//
//        for (FXPole fxpole : fxstorage.getAll()) {
//            double x = 250 + 5 * (fxpole.getPole().getxCord() + 0.5 * fxpole.getPole().getyCord());
//            double y = 250 + 5 * 1.5 * fxpole.getPole().getyCord();
//
//            Circle circle = new Circle(x, y, 10);
//
//            pane.getChildren().add(circle);
//
//            //fxpole.draw();
//        }


//        Circle circle = new Circle(100, 100, 3);
//        Circle circle2 = new Circle(200, 200, 3);
//
//        pane.getChildren().add(circle);
//        pane.getChildren().add(circle2);

        return pane;
    }
}