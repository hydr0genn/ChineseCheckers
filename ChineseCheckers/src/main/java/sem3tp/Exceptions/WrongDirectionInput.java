package sem3tp.Exceptions;

public class WrongDirectionInput extends RuntimeException {
    public WrongDirectionInput(String message) {
        super(message);
    }
}
