package evernote.mvpcalculator.exception;

public class NoFileExtensionException extends RuntimeException {
    public NoFileExtensionException(String required) {
        super("No file extension provided! Required: " + required);
    }
}
