package devices.domain.exceptions;

public class CyclicUplinkReferenceException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Cyclic device connection is not accepted in network topology";
    }
}
