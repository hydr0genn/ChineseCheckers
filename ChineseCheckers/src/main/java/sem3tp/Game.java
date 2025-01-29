package sem3tp;

import sem3tp.Board.Board;
import sem3tp.Board.Colors;
import sem3tp.Creator.Creator;
import sem3tp.Mover.Directions;
import sem3tp.Mover.Variants;
import sem3tp.Storage.PlayerStorage;
import sem3tp.Storage.UserStorage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game implements Serializable {
    Board board;
    boolean hasEnded, isOn = false;
    int i=0;
    public int id, players_num;
    ArrayList<Colors> colorsArrayList;
    UserStorage playersList;
    User currentPlayer;
    Variants variant;

    public Variants getVariant() {
        return variant;
    }

    public void setVariant(Variants variant) {
        this.variant = variant;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    /*We start the game when all players have joined and are ready */
    public boolean checkReadiness(){
        for(int i = 0 ;i <playersList.getSize();i++){
            User temp = playersList.getByIndex(i);
            if(!temp.isReady()){
                return false;
            }
        }
        if(playersList.getSize()==players_num){
            setFirstPlayer();
            return true;
        }
        return false;
    }

    public void setFirstPlayer(){
        currentPlayer=playersList.getByIndex(0);
    }

    public void nextTurn(){
        int currentindex = playersList.indexOf(currentPlayer);
        currentPlayer = playersList.getByIndex((currentindex+1)%players_num);
    }

    private void initialiseColorArray(){
        this.colorsArrayList.add(Colors.Black);
        if(players_num%2==0){
            this.colorsArrayList.add(Colors.White);
        }
        if(players_num%3==0){
            this.colorsArrayList.add(Colors.Green);
            this.colorsArrayList.add(Colors.Yellow);
        }
        if(players_num==4){
            this.colorsArrayList.add(Colors.Red);
            this.colorsArrayList.add(Colors.Red.getOppositeColor());
        }
        if(players_num==6){
            this.colorsArrayList.add(Colors.Blue);
            this.colorsArrayList.add(Colors.Red);
        }
    }

    public User getCurrentPlayer() {
        return currentPlayer;
    }


    public boolean isOn() {
        return isOn;
    }

    public boolean hasEnded(){
        return this.hasEnded;
    }

    public Game(int id){
        this.id=id;
        this.playersList= new UserStorage();
        this.colorsArrayList=new ArrayList<>();
    }

    public void turnOn(){
        this.isOn = true;
    }

    public void turnOff(){
        this.isOn=false;
    }

    public void addNewPlayer(User newplayer){
        System.out.println("Player added"+newplayer);
        playersList.insert(newplayer);
        newplayer.playerColor=colorsArrayList.get(i);
        i++;
    }

    public void setPlayers_num(int x){
        this.players_num=x;
        initialiseColorArray();
    }

    public UserStorage getPlayersList() {
        return playersList;
    }

    public int getPlayersNumber() {
        return this.players_num;
    }

}
