package sem3tp;

import sem3tp.Board.Board;
import sem3tp.Creator.Creator;
import sem3tp.Storage.PlayerStorage;

public class Game implements Runnable{
    Board board;
    boolean hasEnded, isOn = false;
    public int id, players_num;
    PlayerStorage playersList;

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
    public void processMoves(){

    }

    @Override
    public void run() {
        while(!hasEnded){
            processMoves();
        }
        System.out.println("Gra " + id + " zakonczona\n");
    }
}
