package org.feeder.api.gateway.exception;

import java.util.List;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiError {

  private final HttpStatus status;

  private final String message;

  private final List<String> errors;

  public ApiError(HttpStatus status, String message, String error) {
    this.status = status;
    this.message = message;
    this.errors = List.of(error);
  }
}
