package sem3tp;

import org.junit.jupiter.api.Test;
import sem3tp.Board.Board;
import sem3tp.Board.Triangle;
import sem3tp.Creator.Creator;
import sem3tp.GUI.FXPole;
import sem3tp.Poles.Pole;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardTest {
    @Test
    public void testPoles(){
        Creator creator = Creator.getInstance();
        Board board = creator.createBoardBuilder(5,6).build();
        assertEquals(121,board.getAllFXPoles().getSize());
        HashSet<Pole> hashSet = new HashSet<>(board.getAllPoles().getAll());
        System.out.println(hashSet.size());
        assertEquals(121, board.getAllPoles().getSize());
    }

    @Test
    public void testTriangles(){
        Creator creator = Creator.getInstance();
        Board board = creator.createBoardBuilder(5,6).build();
        assertEquals(6, board.getTriangles().getSize());
        for(Triangle triangle: board.getTriangles().getAll()){
            assertEquals(10, triangle.getStorage().getSize());
        }
    }


}
