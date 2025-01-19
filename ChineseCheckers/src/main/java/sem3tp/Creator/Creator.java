package sem3tp.Creator;

import sem3tp.Board.Board;
import sem3tp.Board.BoardBase;
import sem3tp.Board.Colors;
import sem3tp.Board.Triangle;
import sem3tp.Builder.BaseBuilder;
import sem3tp.Builder.BoardBuilder;
import sem3tp.Builder.GameBuilder;
import sem3tp.Builder.TriangleBuilder;
import sem3tp.GUI.FXMarkedPole;
import sem3tp.GUI.FXPole;
import sem3tp.Game;
import sem3tp.Mover.Directions;
import sem3tp.Mover.Mover;
import sem3tp.Player;
import sem3tp.Poles.InitPole;
import sem3tp.Poles.Pole;
import sem3tp.Poles.StandardPole;
import sem3tp.Poles.TrianglePole;
import sem3tp.Storage.*;
import sem3tp.User;

import java.util.ArrayList;
import java.util.concurrent.TransferQueue;

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
    public StandardPole createSTDPole(int xCord, int yCord, int zCord) {
        StandardPole stdPole = new StandardPole(xCord,yCord,zCord);
        stdPole.setColor(Colors.Grey);
        return stdPole;
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

    private boolean isWithinBoundary(int x, int y, int z, int layers){
        return Math.abs(x)<layers && Math.abs(y)<layers && Math.abs(z)<layers;
    }

    public Pole createBasePole(Pole pole, Directions direction, int layers){
        int newX = pole.getxCord()+ direction.addXCord();
        int newY = pole.getyCord() + direction.addYCord();
        int newZ = pole.getzCord()+ direction.addZCord();
        if(isWithinBoundary(newX,newY,newZ, layers)) {return createSTDPole(newX, newY, newZ);}

        return null;
    }

    public Game createGame(Board board, int id, int players_num){
        return new GameBuilder(board,id,players_num).build();
    }

    public FXMarkedPole createMarkedPole(FXPole fxPole, FXPole parent){
        FXMarkedPole fxMarkedPole = new FXMarkedPole();
        fxMarkedPole.setPole(fxPole);
        fxMarkedPole.setParent(parent);
        return fxMarkedPole;
    }

    /*Adding a neighbour logic - it is responsible for creating a triangle pole
     * it does not verify whether one should be created*/
    public TrianglePole createNeighbour(Pole current, Directions direction){
        int newX = current.getxCord()+direction.addXCord();
        int newY = current.getyCord()+direction.addYCord();
        int newZ = current.getzCord()+direction.addZCord();
        return createTrianglePole(newX,newY,newZ);
    }

    /*GUI LOGIC GUI LOGIC GUI LOGIC GUI LOGIC GUI LOGIC GUI LOGIC GUI LOGIC GUI LOGIC*/
    public FXPole createFXPole(Pole pole){
        return new FXPole(pole);
    }
}
