package evernote.mvpcalculator.exception;

public class NoFileHeaderException extends RuntimeException {
    public NoFileHeaderException() {
        super("No game name provided!");
    }
}
