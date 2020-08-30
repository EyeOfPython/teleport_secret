package cash.be.nfc;

public class ISO7816Exception extends Exception {
    public ISO7816Exception(String message) {
        super(message);
    }

    public ISO7816Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public ISO7816Exception(Throwable cause) {
        super(cause);
    }
}
