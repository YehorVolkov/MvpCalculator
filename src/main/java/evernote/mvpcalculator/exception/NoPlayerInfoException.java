package evernote.mvpcalculator.exception;

public class NoPlayerInfoException extends RuntimeException {
    public NoPlayerInfoException() {
        super("No player info provided in game file!");
    }
}
