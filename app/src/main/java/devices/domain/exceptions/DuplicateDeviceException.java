package devices.domain.exceptions;

public class DuplicateDeviceException extends RuntimeException {
    @Override
    public String getMessage() {
        return "A device with the same macAddress is already deployed to network";
    }

}
