package sem3tp;

import sem3tp.Board.Board;
import sem3tp.Board.BoardBase;
import sem3tp.Board.Colors;
import sem3tp.Builder.BaseBuilder;
import sem3tp.Builder.BoardBuilder;
import sem3tp.Builder.TriangleBuilder;
import sem3tp.Creator.Creator;
import sem3tp.Poles.TrianglePole;

public class Main {
    public static void main(String[] args) {
        Creator creator = Creator.getInstance();
        BoardBuilder builder = creator.createBoardBuilder(3, 2);
        Board base = builder.build();

        System.out.println("udalo sie stworzyc baze boarda");
    }
}