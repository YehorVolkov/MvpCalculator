package evernote.mvpcalculator.exception;

public class WrongPlayerDataException extends RuntimeException {
    public WrongPlayerDataException() {
        super("One or more elements of player data is in unacceptable format!");
    }
}
