package sem3tp.Database;

import javax.persistence.*;


@Entity
@Table(name = "games")
public class GameDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num_players", nullable = false)
    private int numPlayers;

    @Lob
    @Column(name = "board_json", nullable = false, columnDefinition = "TEXT")
    private String boardJson;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public String getBoardJson() {
        return boardJson;
    }

    public void setBoardJson(String boardJson) {
        this.boardJson = boardJson;
    }
}
