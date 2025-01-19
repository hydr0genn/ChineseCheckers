package sem3tp.Builder;

import sem3tp.Board.Board;
import sem3tp.Game;

public class GameBuilder {
    Board board;
    int players_num, id;
    public GameBuilder(Board board, int id, int players_num){
        this.board=board;
        this.id=id;
        this.players_num=players_num;
    }

    public Game build() {
        Game game = new Game(id);
        game.setPlayers_num(players_num);
        game.setBoard(board);
        return game;
    }
}
