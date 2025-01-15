package sem3tp.GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import sem3tp.Client.ClientHandler;
import sem3tp.Client.ClientHandler;
import sem3tp.Creator.Creator;
import sem3tp.Game;
import sem3tp.Mover.Directions;
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

    private boolean hasJumped(FXPole parent, FXPole current){
        //if the current pole is a neighbour of our source then no jump has been made
        return !parent.getPole().getNeighbours().contains(current.getPole());
    }

    private void oneMovementSequence(FXPole fxpole, FXPoleStorage allFXPoles){
        Creator creator = Creator.getInstance();
        FXPoleStorage possibleMoves = findPossibleMoves(fxpole, allFXPoles);
        FXMarkedPoleStorage allMarkedPoles = new FXMarkedPoleStorage();
        for (FXPole pole : possibleMoves.getAll()) {
            FXMarkedPole fxMarkedPole = creator.createMarkedPole(pole, fxpole, allMarkedPoles);
            allMarkedPoles.insert(fxMarkedPole);
            fxMarkedPole.draw();//TU jakas logika rosowania na panie
        }
    }

    private void giveTurn(ClientHandler clientHandler) throws IOException {
        clientHandler.sendMessageString("CHNGTURN");
        clientHandler.getClient().getGame().nextTurn();
    }


    public void deleteMarked(FXMarkedPoleStorage otherMoves){
        //delete from root all created marked poles
    }

    //BUTTON INITIALIZATION BUTTON INITIALIZATION BUTTON INITIALIZATION BUTTON INITIALIZATION BUTTON INITIALIZATION
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
                    oneMovementSequence(fxpole,allFXPoles);
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
                    deleteMarked(fxMarkedPole.getOtherMoves());
                    if(hasJumped(fxMarkedPole.getPoleParent(), fxMarkedPole.getPole())){
                        oneMovementSequence(current, allFXPoles);
                    }
                    giveTurn(clientHandler);
                } catch (IOException e) {
                    throw new RuntimeException(e);
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

    public void initializeChooseCreateGameButton(ClientHandler clientHandler, Button button){
       //TODO SCENE where user can set number of players
    }

    public void initializeJoinGameButton(ClientHandler clientHandler, Button button, TextField textField){
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    clientHandler.sendMessageString("JOINGAME"+textField.getText());//wysylamy liczbe graczy tu kwesgtia czy cos chcemy jeszcze na przycisku bo jak tak to przyps ;p
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void initializeCreateGameButton(ClientHandler clientHandler, Button button, TextField textField){
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    clientHandler.sendMessageString("CREATGAME"+textField.getText());//wysylamy liczbe graczy
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void initializeLoginButton(ClientHandler clientHandler, Button button, TextField textField){
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    clientHandler.sendMessageString("LOGINXXX"+textField.getText());//lets assume that all initial wordls like loginxxx must be of length 8
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
