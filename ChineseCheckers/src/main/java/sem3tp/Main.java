package sem3tp;

import sem3tp.Board.Board;
import sem3tp.Board.BoardBase;
import sem3tp.Board.Colors;
import sem3tp.Builder.BaseBuilder;
import sem3tp.Builder.BoardBuilder;
import sem3tp.Builder.TriangleBuilder;
import sem3tp.Creator.Creator;
import sem3tp.GUI.FXPole;
import sem3tp.Poles.Pole;
import sem3tp.Poles.TrianglePole;

public class Main {
    public static void main(String[] args) {
        Creator creator = Creator.getInstance();
//        BoardBuilder builder = creator.createBoardBuilder(3, 2);
//        Board base = builder.build();

        Pole pole1 = creator.createSTDPole(0,1,2);
        Pole pole2 = creator.createSTDPole(0,1,2);

        FXPole fxPole1 = new FXPole(pole1);
        FXPole fxPole2 = new FXPole(pole2);


        System.out.println(fxPole1.equals(fxPole2));
    }
}