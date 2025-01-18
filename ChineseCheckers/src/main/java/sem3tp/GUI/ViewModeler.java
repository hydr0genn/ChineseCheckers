package sem3tp.GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import sem3tp.Board.Colors;
import sem3tp.Client.ClientHandler;
import sem3tp.Client.ClientHandler;
import sem3tp.Creator.Creator;
import sem3tp.Game;
import sem3tp.Mover.Directions;
import sem3tp.Mover.Variants;
import sem3tp.Player;
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


    private Pole jump(Pole pole, Directions direction){
        return pole.getNeighbourByDirection(direction);
    }

    private boolean isAvailable(Pole pole){
        return pole.getColor() == Colors.Grey;
    }

    private Pole checkForJump(Pole pole, Pole source, Variants variant ){//potential variant
        if(isAvailable(pole)){
            return pole;
        }
        Directions direction = source.getDirectionOfNeighbour(pole);
        if(isAvailable(jump(pole,direction))){
            return jump(pole, direction);
        }else if(variant==Variants.TwoJumps){
            checkForJump(jump(pole,direction),pole,Variants.OneJump);
        }
        return null;
    }

    private FXPoleStorage findPossibleMoves(FXPole fxpole, FXPoleStorage allFXPoles, Variants variant){
        FXPoleStorage neighbourList = new FXPoleStorage();
        if (fxpole.getPole() instanceof TrianglePole currentPole){
            for (Pole neighbour : currentPole.getNeighbours().getAll()){
                if(neighbour instanceof TrianglePole){ //inside a triangle
                    Pole potentialPole = checkForJump(neighbour,currentPole, variant);
                    if(potentialPole==null)continue;
                    FXPole actualPole = allFXPoles.get(new FXPole(potentialPole));
                    if(actualPole!=null){
                        neighbourList.insert(actualPole);
                    }
                }else if(neighbour instanceof StandardPole){//from triangle to baseboard
                    Pole potentialPole = checkForJump(neighbour,currentPole, variant);
                    if(potentialPole==null)continue;
                    FXPole actualPole = allFXPoles.get(new FXPole(potentialPole));
                    if(actualPole!=null){
                        neighbourList.insert(actualPole);
                    }
                }
            }
        }else if(fxpole.getPole() instanceof StandardPole currentPole){
            for(Pole neighbour : currentPole.getNeighbours().getAll()){
                if(neighbour instanceof TrianglePole triangleNeighbour){
                    Pole potentialPole = checkForJump(neighbour,currentPole, variant);
                    if(potentialPole==null)continue;
                    FXPole actualPole = allFXPoles.get(new FXPole(potentialPole));
                    if(actualPole!=null){
                        neighbourList.insert(actualPole);
                    }
                }else{
                    Pole potentialPole = checkForJump(neighbour,currentPole, variant);
                    if(potentialPole==null)continue;
                    FXPole actualPole = allFXPoles.get(new FXPole(potentialPole));
                    if(actualPole!=null){
                        neighbourList.insert(actualPole);
                    }
                }
            }
        }
        return neighbourList;
    }

    private boolean hasJumped(FXPole parent, FXPole current){
        //if the current pole is a neighbour of our source then no jump has been made
        return !parent.getPole().getNeighbours().contains(current.getPole());
    }

    private void oneMovementSequence(FXPole fxpole, FXPoleStorage allFXPoles, Variants variant, ClientHandler handler){
        Creator creator = Creator.getInstance();
        FXPoleStorage possibleMoves = findPossibleMoves(fxpole, allFXPoles, variant);
        FXMarkedPoleStorage allMarkedPoles = new FXMarkedPoleStorage();
        for (FXPole pole : possibleMoves.getAll()) {
            FXMarkedPole fxMarkedPole = creator.createMarkedPole(pole, fxpole, allMarkedPoles);
            allMarkedPoles.insert(fxMarkedPole);
            handler.pane.getChildren().add(fxMarkedPole.draw());
            initializeMarkedPoleHandler(handler,fxMarkedPole,allFXPoles);
        }
    }

    private void giveTurn(ClientHandler clientHandler) throws IOException {
        clientHandler.sendMessageString("CHNGTURN");
        clientHandler.getGame().nextTurn();
    }


    public void deleteMarked(FXMarkedPoleStorage otherMoves, Pane pane){
        for (FXMarkedPole fxMarkedPole : otherMoves.getAll()){
            pane.getChildren().remove(fxMarkedPole);
        }
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
                if(game.getCurrentPlayer().equals(currentPlayer)) {
                    oneMovementSequence(fxpole,allFXPoles, game.getVariant(), handler);
                }
            }
        });
    }

    public void initializeMarkedPoleHandler(ClientHandler clientHandler, FXMarkedPole fxMarkedPole, FXPoleStorage allFXPoles){
        fxMarkedPole.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    FXPole parent = fxMarkedPole.getPoleParent();
                    FXPole current = fxMarkedPole.getPole();
                    clientHandler.sendMove("CSNDMOVE", parent, current);
                    deleteMarked(fxMarkedPole.getOtherMoves(), clientHandler.pane);
                    if(hasJumped(fxMarkedPole.getPoleParent(), fxMarkedPole.getPole())){
                        oneMovementSequence(current, allFXPoles, clientHandler.getGame().getVariant(), clientHandler);
                    }
                    giveTurn(clientHandler);
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

    //do wyjebania
    public void initializeChooseCreateGameButton(ClientHandler clientHandler, Button button){
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    clientHandler.sendMessageString("CRTDGAME");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
