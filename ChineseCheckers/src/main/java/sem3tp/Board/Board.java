package sem3tp.Board;

import sem3tp.Storage.PoleStorage;
import sem3tp.Storage.TriangleStorage;

public class Board {
    private BoardBase base;
    private TriangleStorage triangles;

    public void setBase(BoardBase base) {
        this.base = base;
    }

    public BoardBase getBase() {
        return base;
    }

    public TriangleStorage getTriangles() {
        return triangles;
    }

    public void setTriangles(TriangleStorage triangles) {
        this.triangles = triangles;
    }

}
