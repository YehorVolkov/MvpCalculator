package evernote.mvpcalculator.exception;

public class WrongFileExtensionException extends RuntimeException {
    public WrongFileExtensionException(String required, String provided) {
        super("Wrong game file extension! Required: " + required + ", provided: " + provided);
    }
}
