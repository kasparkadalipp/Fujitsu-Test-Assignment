package ee.fujitsu.deliveryfeeservice.exception;

public class InvalidDataException extends RuntimeException {

    public InvalidDataException(String message) {
        super(message);
    }
}
