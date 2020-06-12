package org.feeder.api.gateway.exception;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CustomErrorController extends BasicErrorController {

  public CustomErrorController(
      ErrorAttributes errorAttributes,
      ServerProperties serverProperties,
      List<ErrorViewResolver> errorViewResolvers) {
    super(errorAttributes, serverProperties.getError(), errorViewResolvers);
  }

  @Override
  public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {

    HttpStatus status = getStatus(request);
    String message = "";

    switch (status) {
      case NOT_FOUND:
        message = "No routing found for provided requested URL";
        break;
      case NO_CONTENT:
        message = "No content";
        break;
      case INTERNAL_SERVER_ERROR:
        message = "Internal server error";
    }

    log.warn("Error occurred {}", message);

    throw new GatewayException(status, message);
  }
}
