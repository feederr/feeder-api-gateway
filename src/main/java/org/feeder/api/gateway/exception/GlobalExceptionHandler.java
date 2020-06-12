package org.feeder.api.gateway.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handle(Exception exception) {

    ApiError error = new ApiError(
        INTERNAL_SERVER_ERROR,
        "Something went wrong",
        exception.getMessage()
    );

    return buildResponseEntity(error);
  }

  @ExceptionHandler(GatewayException.class)
  public ResponseEntity<ApiError> handle(GatewayException exception) {

    ApiError error = new ApiError(
        exception.getStatus(),
        "Error occurred",
        exception.getMessage()
    );

    return buildResponseEntity(error);
  }

  private ResponseEntity<ApiError> buildResponseEntity(ApiError error) {
    return ResponseEntity.status(error.getStatus())
        .contentType(MediaType.APPLICATION_JSON)
        .body(error);
  }
}
