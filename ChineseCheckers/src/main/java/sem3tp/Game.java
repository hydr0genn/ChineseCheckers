package sem3tp;

import sem3tp.Board.Board;
import sem3tp.Creator.Creator;
import sem3tp.Mover.Directions;
import sem3tp.Storage.PlayerStorage;

public class Game {
    Board board;
    boolean hasEnded, isOn = false;
    public int id, players_num;
    PlayerStorage playersList;
    Player currentPlayer;

    /*We start the game when all players have joined and are ready */
    public void checkReadiness(){
        for(int i = 0 ;i <playersList.getSize();i++){
            Player temp = playersList.getByIndex(i);
            if(!temp.isReady()){
                return;
            }
        }
        if(playersList.getSize()==players_num){
            turnOn();
        }
    }

    public void nextTurn(){
        int currentindex = playersList.indexOf(currentPlayer);
        currentPlayer = playersList.getByIndex(currentindex+1);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer() {
        this.currentPlayer = currentPlayer;
    }

    public boolean isOn() {
        return isOn;
    }

    public boolean hasEnded(){
        return this.hasEnded;
    }

    public Game(int id){
        this.id=id;
    }

    public void turnOn(){
        this.isOn = true;
    }

    public void turnOff(){
        this.isOn=false;
    }

    public void addNewPlayer(Player newplayer){
        playersList.insert(newplayer);
    }

    public void setPlayers_num(int x){
        this.players_num=x;
        createBoard();
    }

    public void createBoard(){
        if (this.players_num==2){
            //tworzy odpowedni rodzaj boarda
        }
    }

    /*In the furture proper move logic will be added here containing Pole that is to be affected*/
    public void processMoves(Directions direction){
        nextTurn();
    }

//    @Override
//    public void run() {
//        while(!hasEnded){
//            processMoves();
//        }
//        System.out.println("Gra " + id + " zakonczona\n");
//    }
}
