package devices.adapter.in.web.exceptions;

import devices.adapter.in.web.dto.ErrorResponse;
import devices.domain.DevicesNetwork;
import devices.domain.exceptions.CyclicUplinkReferenceException;
import devices.domain.exceptions.DuplicateDeviceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import devices.adapter.in.web.NetworkController.InvalidDeviceRegistrationParameters;

@ControllerAdvice
public class NetworkControllerExceptionHandler {

    @ExceptionHandler(DuplicateDeviceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateDeviceException(DuplicateDeviceException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Bad Request",
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CyclicUplinkReferenceException.class)
    public ResponseEntity<ErrorResponse> handleCyclicUplinkReferenceException(CyclicUplinkReferenceException ex) {
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

    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDeviceNotFound(DeviceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Device not found",
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
