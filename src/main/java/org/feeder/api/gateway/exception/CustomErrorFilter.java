package org.feeder.api.gateway.exception;

import static org.apache.commons.lang.CharEncoding.UTF_8;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.ERROR_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_ERROR_FILTER_ORDER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import com.google.gson.Gson;
import com.netflix.zuul.context.RequestContext;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomErrorFilter extends SendErrorFilter {

  protected static final String SEND_ERROR_FILTER_RAN = "sendErrorFilter.ran";

  @Override
  public String filterType() {
    return ERROR_TYPE;
  }

  @Override
  public int filterOrder() {
    return SEND_ERROR_FILTER_ORDER - 1; // to execute before SendErrorFilter
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }

  @Override
  @SneakyThrows
  public Object run() {

    RequestContext ctx = RequestContext.getCurrentContext();
    ExceptionHolder exception = findZuulException(ctx.getThrowable());
    HttpServletResponse response = ctx.getResponse();
    String body = buildJsonRepresentation(buildError(exception));

    log.warn("Error occured: {}", body);

    try (PrintWriter out = ctx.getResponse().getWriter()) {

      out.print(body);

      response.setContentType(APPLICATION_JSON_VALUE);
      response.setCharacterEncoding(UTF_8);
      response.setStatus(exception.getStatusCode());

      out.flush();
    }

    ctx.set(SEND_ERROR_FILTER_RAN); // to prevent execution of SendErrorFilter

    return Boolean.TRUE; // it can be null
  }

  private String buildJsonRepresentation(ApiError error) {
    return new Gson().toJson(error);
  }

  private ApiError buildError(ExceptionHolder exceptionHolder) {
    return new ApiError(
        HttpStatus.valueOf(exceptionHolder.getStatusCode()),
        exceptionHolder.getErrorCause(),
        exceptionHolder.getThrowable().getMessage()
    );
  }
}
