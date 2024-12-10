package sem3tp.Exceptions;

public class IncorrectPlayersNumber extends RuntimeException {
    public IncorrectPlayersNumber(String message) {
        super(message);
    }
}
