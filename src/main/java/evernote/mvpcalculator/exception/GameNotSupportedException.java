package evernote.mvpcalculator.exception;

public class GameNotSupportedException extends RuntimeException {
    public GameNotSupportedException() {
        super("Game not supported!");
    }
}
