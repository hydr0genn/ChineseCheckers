package sem3tp.Creator;

import sem3tp.Board.Board;
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

public interface Create {
    public StandardPole createSTDPole(int xCord, int yCord, int zCord);
    public InitPole createInitPole();
    public BoardBuilder createBoardBuilder(int layers, int playersNum);
    public BaseBuilder createBaseBuilder(int x);
    public Mover createMover();
    public PoleStorage createPoleStorage();
    public UserStorage createUserStorage();
    public TriangleStorage createTriangleStorage();
    public User createUser(String username, String pwd);
    public Game createGame(int id);
    public Player createPlayer(String username);
    public PlayerStorage createPlayerStorage();

    /*All create functions regarding triangles*/
    public Triangle createTriangle();
    public TriangleBuilder createTriangleBuilder(int layers, Colors color, Pole pole, BoardBase base);
    public TrianglePole createTrianglePole(int x, int y, int z);
}
