package org.dmencia.examentcs.exception;

import org.dmencia.examentcs.dto.response.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(
            ResourceNotFoundException.class
    )
    public ResponseEntity<ErrorResponseDTO> handleNotFound(
            ResourceNotFoundException ex
    ) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

    @ExceptionHandler(
            DuplicateResourceException.class
    )
    public ResponseEntity<ErrorResponseDTO> handleConflict(
            DuplicateResourceException ex
    ) {

        return buildResponse(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
    }

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<ErrorResponseDTO> handleValidation(
            MethodArgumentNotValidException ex
    ) {

        String message = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField()
                                + ": "
                                + error.getDefaultMessage()
                )
                .collect(Collectors.joining("; "));

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                message
        );
    }

    @ExceptionHandler(
            IllegalArgumentException.class
    )
    public ResponseEntity<ErrorResponseDTO> handleBadRequest(
            IllegalArgumentException ex
    ) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }

    private ResponseEntity<ErrorResponseDTO> buildResponse(
            HttpStatus status,
            String message
    ) {

        ErrorResponseDTO response =
                new ErrorResponseDTO(
                        LocalDateTime.now(),
                        status.value(),
                        status.getReasonPhrase(),
                        message
                );

        return ResponseEntity
                .status(status)
                .body(response);
    }
}
