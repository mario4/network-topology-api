package devices.adapter.in.web.exceptions;

import devices.adapter.in.web.dto.ErrorResponse;
import devices.domain.DevicesNetwork;
import devices.domain.InvalidMacAddressException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import devices.adapter.in.web.NetworkController.InvalidDeviceRegistrationParameters;

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

    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDeviceNotFound(DeviceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Device not found",
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidMacAddressException.class)
    public ResponseEntity<ErrorResponse> handleInvalidMACAddress(InvalidMacAddressException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid MAC address",
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
