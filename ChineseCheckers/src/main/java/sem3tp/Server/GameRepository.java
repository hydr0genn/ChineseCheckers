package sem3tp.Server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class GameRepository {

    private final JdbcTemplate jdbcTemplate;

    // Correct constructor-based injection
    @Autowired
    public GameRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void blabla(){
        System.out.println("bla bla");
    }

    public void saveMove(int gameID, String actionType, int initialX, int initialY, int initialZ, int finalX, int finalY, int finalZ) {
        String sql = "INSERT INTO Actions (GameID, ActionType, InitialX, InitialY, InitialZ, FinalX, FinalY, FinalZ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, gameID, actionType, initialX, initialY, initialZ, finalX, finalY, finalZ);
        System.out.println("Move saved successfully!");
    }

    public void saveGame(int numberOfPlayers, int gameVariant, int numberOfBots) {
        String sql = "INSERT INTO Games (NumberOfPlayers, GameVariant, NumberOfBots) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, numberOfPlayers);
            ps.setInt(2, gameVariant);
            ps.setInt(3, numberOfBots);
            return ps;
        }, keyHolder);

        // Retrieve the generated GameID
        keyHolder.getKey().intValue();
    }

    public int NumberOfPlayers(int gameID) {
        String sql = "SELECT NumberOfPlayers FROM Games WHERE GameID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, gameID);
        } catch (Exception e) {
            System.out.println("Game with ID " + gameID + " not found.");
            return -1;
        }
    }

    public boolean doesGameExist(int gameID) {
        String sql = "SELECT 1 FROM Games WHERE GameID = ?";
        try {
            Integer result = jdbcTemplate.queryForObject(sql, Integer.class, gameID);
            return result != null;
        } catch (Exception e) {
            System.out.println("Game with ID " + gameID + " does not exist.");
            return false;
        }
    }
}
