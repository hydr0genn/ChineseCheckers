package sem3tp.GUI;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import sem3tp.Client.ClientHandler;
import sem3tp.Client.GameClient;

public class WrapperLayoutBuilder implements Builder<Region> {
    private final ClientHandler clientHandler;
    Region customComponent2;
    Region customComponent3;

    public WrapperLayoutBuilder(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public Region build() {
        BorderPane results = new BorderPane();
        results.setTop(new Label("This is The Wrapper"));
        Region customComponent1 = new Layout1Builder(() -> results.setCenter(customComponent2), clientHandler).build();
        customComponent2 = new Layout2Builder(() -> results.setCenter(customComponent3)).build();
        customComponent3 = new Layout3Builder(() -> results.setCenter(customComponent1)).build();
        results.setCenter(customComponent1);
        return results;
    }
}