package devices.domain;

public class InvalidMacAddressException extends RuntimeException{

    public InvalidMacAddressException(String message) {
        super(message);
    }
}