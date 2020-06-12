package org.feeder.api.gateway.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class GatewayException extends RuntimeException {

  private static final long serialVersionUID = 487009990144383663L;

  private final HttpStatus status;

  private final String message;

  public GatewayException(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
