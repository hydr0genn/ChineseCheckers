package sem3tp.Bot;

import sem3tp.Board.Colors;
import sem3tp.Board.Triangle;
import sem3tp.GUI.FXPole;
import sem3tp.Game;
import sem3tp.Mover.Mover;
import sem3tp.Poles.StandardPole;
import sem3tp.Storage.FXPoleStorage;
import sem3tp.Storage.PoleStorage;
import sem3tp.User;

import java.util.AbstractMap;

public class Bot extends User {
    private FXPoleStorage poles;
    private Game game;
    public Bot() {
        this.isReady=true;
    }


    public void updatePoles(){
        this.poles = game.getBoard().getAllFXPolesOfColor(playerColor);
    }

    public FXPoleStorage getPoles() {
        return poles;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    private double cumulativeDistance(){
        BotStrategy strategy = BotStrategy.getInstance();
        double sum = 0;
        for (FXPole fxPole : poles.getAll()){
            sum+=strategy.getDistance(playerColor,fxPole);
        }
        return sum;
    }

    public AbstractMap.SimpleEntry<FXPole, FXPole> makeOptimalMove(){
        updatePoles();
        BotStrategy strategy = BotStrategy.getInstance();
        Result min = new Result(cumulativeDistance(),null);
        FXPole source = new FXPole(new StandardPole(0,0,0));


        for(FXPole BotPole : poles.getAll()){
            Result bestSequence = strategy.findBestMove(BotPole, game, playerColor, cumulativeDistance());
            if(bestSequence.compareTo(min)<0){
                min=bestSequence;
                source=BotPole;
            }
        }
        return new AbstractMap.SimpleEntry<FXPole, FXPole>(source, min.fxPole);
    }

}
