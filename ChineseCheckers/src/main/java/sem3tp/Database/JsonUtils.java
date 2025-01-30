package sem3tp.Database;

import com.fasterxml.jackson.databind.ObjectMapper;
import sem3tp.Board.Board;

public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Serialize Board to JSON
    public static String serializeBoard(Board board) throws Exception {
        return objectMapper.writeValueAsString(board);
    }

    // Deserialize JSON to Board
    public static Board deserializeBoard(String json) throws Exception {
        return objectMapper.readValue(json, Board.class);
    }
}
