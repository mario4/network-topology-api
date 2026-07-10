package devices.adapter.in;

import devices.model.DevicesNetwork;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import devices.adapter.in.NetworkController.InvalidDeviceRegistrationParameters;

@ControllerAdvice
public class NetworkControllerExceptionHandler {

    @ExceptionHandler(DevicesNetwork.DuplicateDeviceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateDeviceException(DevicesNetwork.DuplicateDeviceException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Bad Request",
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DevicesNetwork.CyclicUplinkReferenceException.class)
    public ResponseEntity<ErrorResponse> handleCyclicUplinkReferenceException(DevicesNetwork.CyclicUplinkReferenceException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDeviceRegistrationParameters.class)
    public ResponseEntity<ErrorResponse> handleBadParameters(InvalidDeviceRegistrationParameters ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
