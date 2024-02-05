package evernote.mvpcalculator.exception;

public class EmptyFileException extends RuntimeException {
    public EmptyFileException() {
        super("Game file appears to be empty!");
    }
}
