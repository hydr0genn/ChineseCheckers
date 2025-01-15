package sem3tp;

import org.junit.jupiter.api.Test;
import sem3tp.Board.Board;
import sem3tp.Board.Triangle;
import sem3tp.Creator.Creator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardTest {
    @Test
    public void testPoles(){
        Creator creator = Creator.getInstance();
        Board board = creator.createBoardBuilder(5,6).build();
        assertEquals(61,board.getAllFXPoles().getSize());
        assertEquals(61, board.getAllPoles().getSize());
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
