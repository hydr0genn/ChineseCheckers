package sem3tp.GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import sem3tp.Board.Colors;
import sem3tp.Bot.Validator;
import sem3tp.Client.ClientHandler;
import sem3tp.Client.ClientHandler;
import sem3tp.Creator.Creator;
import sem3tp.Game;
import sem3tp.Mover.Directions;
import sem3tp.Mover.Variants;
import sem3tp.Player;
import sem3tp.Poles.InitPole;
import sem3tp.Poles.Pole;
import sem3tp.Poles.StandardPole;
import sem3tp.Poles.TrianglePole;
import sem3tp.Storage.FXMarkedPoleStorage;
import sem3tp.Storage.FXPoleStorage;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/*Class responsible for initialising polygons, buttons etc*/
public class ViewModeler {

    public boolean hasJumped(FXPole parent, FXPole current){
        //if the current pole is a neighbour of our source then no jump has been made
        return !parent.getPole().getNeighbours().contains(current.getPole());
    }

    private void oneMovementSequence(FXPole fxpole, FXPoleStorage allFXPoles, Variants variant, ClientHandler handler){
        Creator creator = Creator.getInstance();
        Validator validator = Validator.getInstance();
        FXPoleStorage possibleMoves = validator.findPossibleMoves(fxpole, allFXPoles, variant);
        FXPoleStorage parents = new FXPoleStorage();
        for (FXPole pole : possibleMoves.getAll()) {
            FXMarkedPole fxMarkedPole = creator.createMarkedPole(pole, fxpole);
            handler.getGame().getBoard().markedPoleStorage.insert(fxMarkedPole);
            handler.pane.getChildren().add(fxMarkedPole.draw());
            initializeMarkedPoleHandler(handler,fxMarkedPole,allFXPoles, parents);
        }
    }

    private void JumpMovementSequence(FXPole fxpole, FXPoleStorage possibleMoves, FXPoleStorage allFXPoles, ClientHandler handler, FXPoleStorage parents){
        Creator creator = Creator.getInstance();
        for (FXPole pole : possibleMoves.getAll()) {
            FXMarkedPole fxMarkedPole = creator.createMarkedPole(pole, fxpole);
            handler.getGame().getBoard().markedPoleStorage.insert(fxMarkedPole);
            handler.pane.getChildren().add(fxMarkedPole.draw());
            initializeMarkedPoleHandler(handler,fxMarkedPole,allFXPoles, parents);
        }
    }

    private void giveTurn(ClientHandler clientHandler) throws IOException {
        clientHandler.sendMessageString("CHNGTURN");
    }

    public void deleteMarked(ClientHandler handler){
        for (FXMarkedPole fxMarkedPole : handler.getGame().getBoard().markedPoleStorage.getAll()){
            handler.pane.getChildren().remove(fxMarkedPole);
        }
        handler.getGame().getBoard().markedPoleStorage.getAll().clear();
    }

    //BUTTON INITIALIZATION BUTTON INITIALIZATION BUTTON INITIALIZATION BUTTON INITIALIZATION BUTTON INITIALIZATION
    /*
     * 4 cases of possible moves have to be considered:
     * inside the baseboard
     * from the baseboard to a triangle
     * from a triangle to the baseboard
     * inside a triangle
     * */
    public void initializePoleHandler(FXPole fxpole, FXPoleStorage allFXPoles, Game game, Player currentPlayer, ClientHandler handler){
        fxpole.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(fxpole.equals(new FXPole(new InitPole()))){
                    System.out.println("handler dla inita");
                }
                deleteMarked(handler);
                if(game.getCurrentPlayer().equals(currentPlayer)&&(handler.getUser().playerColor==fxpole.FXColor)) {
                    oneMovementSequence(fxpole,allFXPoles, game.getVariant(), handler);
                }
            }
        });
    }

    public void initializeMarkedPoleHandler(ClientHandler clientHandler, FXMarkedPole fxMarkedPole, FXPoleStorage allFXPoles, FXPoleStorage parents){
        fxMarkedPole.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    FXPole parent = fxMarkedPole.getPoleParent();
                    parents.insert(parent);
                    FXPole current = fxMarkedPole.getPole();
                    clientHandler.sendMove("CSNDMOVE", parent, current);
                    deleteMarked(clientHandler);
                    FXPoleStorage possibleMoves = Validator.getInstance().findPossibleJumps(current, allFXPoles, clientHandler.getGame().getVariant(), parents);
                    if(hasJumped(parent, current)&&possibleMoves.getSize()>0){
                        JumpMovementSequence(current, possibleMoves ,allFXPoles, clientHandler, parents);
                    }else{
                        giveTurn(clientHandler);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void initializeReadyButton(ClientHandler clientHandler, Button button){
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    clientHandler.sendMessageObject("READYXXX", clientHandler.getUser());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                clientHandler.getUser().setReady(true);
            }
        });
    }

    public void initializeNotReadyButton(ClientHandler clientHandler,Button button){
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    clientHandler.sendMessageObject("NOTREADY", clientHandler.getUser());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                clientHandler.getUser().setReady(false);
            }
        });
    }

    public void initializeChooseJoinGameButton(Button button, Runnable sceneSwapper){
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                    sceneSwapper.run();
            }
        });
    }


    //texfield to id gry
    public void initializeJoinGameButton(ClientHandler clientHandler, Button button, TextField textField, Runnable sceneSwapper){
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    clientHandler.sendMessageString("JOINGAME"+textField.getText());//wysylamy liczbe graczy tu kwesgtia czy cos chcemy jeszcze na przycisku bo jak tak to przyps ;p
                    sceneSwapper.run();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void initializeCreateGameButtonVariant1(ClientHandler clientHandler, Button button, TextField textField){
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    clientHandler.sendMessageString("CRTGAMV1"+textField.getText());//wysylamy liczbe graczy
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void initializeCreateGameButtonVariant2(ClientHandler clientHandler, Button button, TextField textField){
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    clientHandler.sendMessageString("CRTGAMV2"+textField.getText());//wysylamy liczbe graczy
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void initializeLoginButton(ClientHandler clientHandler, Button button, TextField textField, Runnable sceneSwapper){
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    clientHandler.sendMessageString("LOGINXXX"+textField.getText());//lets assume that all initial wordls like loginxxx must be of length 8
                    sceneSwapper.run();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
