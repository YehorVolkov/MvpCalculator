package evernote.mvpcalculator.exception;

public class WrongNumberOfPlayerDataException extends RuntimeException {
    public WrongNumberOfPlayerDataException() {
        super("Wrong number of player data provided!");
    }
}
