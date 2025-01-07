package sem3tp.Creator;

import sem3tp.Board.BoardBase;
import sem3tp.Board.Colors;
import sem3tp.Board.Triangle;
import sem3tp.Builder.BaseBuilder;
import sem3tp.Builder.BoardBuilder;
import sem3tp.Builder.TriangleBuilder;
import sem3tp.Game;
import sem3tp.Mover.Mover;
import sem3tp.Player;
import sem3tp.Poles.InitPole;
import sem3tp.Poles.Pole;
import sem3tp.Poles.StandardPole;
import sem3tp.Poles.TrianglePole;
import sem3tp.Storage.PlayerStorage;
import sem3tp.Storage.PoleStorage;
import sem3tp.Storage.TriangleStorage;
import sem3tp.Storage.UserStorage;
import sem3tp.User;

import java.util.ArrayList;

public class Creator implements Create{

    private static Creator instance;

    private Creator(){}

    public static Creator getInstance() {
        if(instance==null){
            synchronized (Creator.class) {
                if(instance==null){
                    instance= new Creator();
                }
            }
        }
        return instance;
    }

    @Override
    public PlayerStorage createPlayerStorage() {
        return new PlayerStorage();
    }

    /*Triangle BEGIN*/
    @Override
    public Triangle createTriangle() {
        return new Triangle();
    }

    @Override
    public TriangleBuilder createTriangleBuilder(int layers, Colors color, Pole pole, BoardBase base) {
        return new TriangleBuilder(layers,color,pole,base);
    }



    @Override
    public TrianglePole createTrianglePole(int x, int y, int z) {
        return new TrianglePole(x,y,z);
    }
    /*Triangle END*/


    @Override
    public Player createPlayer(String username) {
        return new Player(username);
    }

    @Override
    public Game createGame(int id){return new Game(id);}

    @Override
    public StandardPole createSTDPole(int xCord, int yCord, int zCord) {
        return new StandardPole(xCord, yCord, zCord);
    }

    @Override
    public InitPole createInitPole() {
        return new InitPole();
    }

    @Override
    public BoardBuilder createBoardBuilder(int layers, int playersNum) {
        return new BoardBuilder(layers,playersNum);
    }

    @Override
    public BaseBuilder createBaseBuilder(int x) {
        return new BaseBuilder(x);
    }

    @Override
    public Mover createMover() {
        return Mover.getInstance();
    }

    @Override
    public PoleStorage createPoleStorage() {
        return new PoleStorage();
    }

    @Override
    public UserStorage createUserStorage() {
        return new UserStorage();
    }

    @Override
    public TriangleStorage createTriangleStorage() {
        return new TriangleStorage();
    }

    @Override
    public User createUser(String username, String pwd) {
        return new User(username,pwd);
    }


}
