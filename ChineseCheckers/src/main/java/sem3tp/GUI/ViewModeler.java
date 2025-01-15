package sem3tp.GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sem3tp.Client.GameClient;
import sem3tp.Creator.Creator;
import sem3tp.Game;
import sem3tp.Mover.Directions;
import sem3tp.Player;
import sem3tp.Poles.Pole;
import sem3tp.Poles.StandardPole;
import sem3tp.Poles.TrianglePole;
import sem3tp.Storage.FXPoleStorage;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/*Class responsible for initialising polygons, buttons etc*/
public class ViewModeler {

    private Pole jump(Pole pole, Directions direction){
        return pole.getNeighbourByDirection(direction);
    }

    private boolean isAvailable(Pole pole){
        return pole.getColor() == null;
    }

    private Pole checkForJump(Pole pole, Pole source){//potential variant
        if(isAvailable(pole)){
            return pole;
        }
        Directions direction = source.getDirectionOfNeighbour(pole);
        if(isAvailable(jump(pole,direction))){
            return jump(pole, direction);
        }
        return null;
    }

    private FXPoleStorage findPossibleMoves(FXPole fxpole, FXPoleStorage allFXPoles){
        FXPoleStorage neighbourList = new FXPoleStorage();
        if (fxpole.getPole() instanceof TrianglePole currentPole){
            for (Pole neighbour : currentPole.getNeighbours().getAll()){
                if(neighbour instanceof TrianglePole){ //inside a triangle
                    neighbourList.insert(allFXPoles.get(new FXPole(checkForJump(neighbour,currentPole))));
                }else if(neighbour instanceof StandardPole){//from triangle to baseboard
                    if(currentPole.getColor().equals(currentPole.getParent().getColor())){
                        neighbourList.insert(allFXPoles.get(new FXPole(checkForJump(neighbour,currentPole))));
                    }
                }
            }
        }else if(fxpole.getPole() instanceof StandardPole currentPole){
            for(Pole neighbour : currentPole.getNeighbours().getAll()){
                if(neighbour instanceof TrianglePole triangleNeighbour){
                    if(triangleNeighbour.getParent().getColor()==currentPole.getColor().getOppositeColor()){
                        neighbourList.insert(allFXPoles.get(new FXPole(checkForJump(neighbour,currentPole))));
                    }
                }else{
                    neighbourList.insert(allFXPoles.get(new FXPole(checkForJump(neighbour,currentPole))));
                }
            }
        }
        return neighbourList;
    }

    /*
     * 4 cases of possible moves have to be considered:
     * inside the baseboard
     * from the baseboard to a triangle
     * from a triangle to the baseboard
     * inside a triangle
     * */
    public void initializePoleHandler(FXPole fxpole, FXPoleStorage allFXPoles, Game game, Player currentPlayer){
        fxpole.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                if(game.getCurrentPlayer().equals(currentPlayer)) {
                    FXPoleStorage possibleMoves = findPossibleMoves(fxpole, allFXPoles);
                    for (FXPole pole : possibleMoves.getAll()) {
                        //TODO jak chcemy to zrobic? czy zmieniamy jakis background czy moze robimy nowy typ obiektu
                    }
                }
            }
        });
    }

    public void initializeReadyButton(Button button, Player player){
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                player.setReady(true);
            }
        });
    }

    public void initializeNotReadyButton(Button button, Player player){
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                player.setReady(false);
            }
        });
    }

    public void initializeChooseJoinGameButton(Button button){
        //TODO SCENE WHERE usER can choose which game to join
    }

    public void initializeChooseCreateGameButton(GameClient client, Button button){
       //TODO SCENE where user can set number of players
    }

    public void initializeJoinGameButton(GameClient client, Button button, TextField textField){
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    client.sendMessageString("JOINGAME"+textField.getText());//wysylamy liczbe graczy tu kwesgtia czy cos chcemy jeszcze na przycisku bo jak tak to przyps ;p
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void initializeCreateGameButton(GameClient client, Button button, TextField textField){
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    client.sendMessageString("CREATGAME"+textField.getText());//wysylamy liczbe graczy
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void initializeLoginButton(GameClient client, Button button, TextField textField){
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    client.sendMessageString("LOGINXXX"+textField.getText());//lets assume that all initial wordls like loginxxx must be of length 8
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
