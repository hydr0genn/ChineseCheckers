package sem3tp.Creator;

import sem3tp.Board.BoardBase;
import sem3tp.Board.Colors;
import sem3tp.Board.Triangle;
import sem3tp.Builder.BaseBuilder;
import sem3tp.Builder.BoardBuilder;
import sem3tp.Builder.TriangleBuilder;
import sem3tp.Player;
import sem3tp.Poles.InitPole;
import sem3tp.Poles.Pole;
import sem3tp.Poles.StandardPole;
import sem3tp.Poles.TrianglePole;
import sem3tp.Storage.PlayerStorage;
import sem3tp.Storage.PoleStorage;
import sem3tp.Storage.TriangleStorage;

public interface Create {
    public StandardPole createSTDPole(int xCord, int yCord, int zCord);
    public InitPole createInitPole();
    public BoardBuilder createBoardBuilder(int layers, int playersNum);
    public BaseBuilder createBaseBuilder(int x);
    public Player createPlayer(String username);

    /*All create functions regarding storages*/
    public TriangleStorage createTriangleStorage();
    public PlayerStorage createPlayerStorage();
    public PoleStorage createPoleStorage();
    /*All create functions regarding storages*/

    /*All create functions regarding triangles*/
    public Triangle createTriangle();
    public TriangleBuilder createTriangleBuilder(int layers, Colors color, Pole pole, BoardBase base);
    public TrianglePole createTrianglePole(int x, int y, int z);
    /*All create functions regarding triangles*/
}
