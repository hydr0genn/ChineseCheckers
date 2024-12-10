package sem3tp.Server;

import sem3tp.Exceptions.IncorrectPlayersNumber;
import sem3tp.Game;

import java.util.Arrays;

public class Communicator {
    public synchronized  void setupMenu(){

    }


    public synchronized void setMaxClients(int max, Game game) {
        if (Arrays.asList(2, 3, 4, 6).contains(max)) {
            game.players_num = max;
        }
        throw new IncorrectPlayersNumber("The game can be played by: 2, 3 ,4 and 6 players - ur input was not correct");
    }
}
