package sem3tp.GUI;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import sem3tp.Client.ClientHandler;

public class WrapperLayoutBuilder implements Builder<Region> {
    private final ClientHandler clientHandler;
    Region customComponent2;
    Region customComponent3;
    Region customComponent4;

    public WrapperLayoutBuilder(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public Region build() {
        BorderPane results = new BorderPane();
        //results.setTop(new Label("This is The Wrapper"));
        Region customComponent1 = new LoginLayoutBuilder(() -> results.setCenter(customComponent2), clientHandler).build();
        customComponent2 = new CreateLayoutBuilder(() -> results.setCenter(customComponent3), clientHandler).build();
        customComponent3 = new JoinLayoutBuilder(() -> results.setCenter(customComponent1), clientHandler).build();
        customComponent4 = new LobbyLayoutBuilder(() -> results.setCenter(customComponent1), clientHandler).build();
        results.setCenter(customComponent1);
        return results;
    }
}