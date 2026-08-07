package devices.adapter.in.web.exceptions;

import devices.adapter.in.web.dto.ErrorResponse;
import devices.domain.DevicesNetwork;
import devices.domain.InvalidMacAddressException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class DomainExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return new ResponseEntity<>(new ErrorResponse(400, "Validation failed", details), HttpStatus.BAD_REQUEST);
    }

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse error = new ErrorResponse(
                status.value(),
                "Internal Server Error",
                "An unexpected server error occurred: Please contact support");

        return new ResponseEntity<>(error, status);
    }
}
