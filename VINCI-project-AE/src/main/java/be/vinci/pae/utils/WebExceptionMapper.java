package be.vinci.pae.utils;

import be.vinci.pae.utils.exceptions.CodeExceptionMapper;
import be.vinci.pae.utils.loggers.MyLogger;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * A class that provides a mapping between all exceptions and the appropriate HTTP response.
 */
@Provider
public class WebExceptionMapper implements ExceptionMapper<Throwable> {

  @Inject
  private MyLogger myLogger;

  @Inject
  private CodeExceptionMapper codeExceptionMapper;

  /**
   * Convert an exception to a http response (which contains a status code and an error message).
   *
   * @param exception the exception to convert.
   * @return the http response containing a status code and an error message.
   */
  @Override
  public Response toResponse(Throwable exception) {

    myLogger.error(exception.getMessage(), exception);

    if (exception instanceof WebApplicationException) {
      return Response.status(((WebApplicationException) exception).getResponse().getStatus())
          .entity(codeExceptionMapper.codeMapper(exception.getMessage())).type("text/plain")
          .build();
    }

    return Response.status(Status.INTERNAL_SERVER_ERROR)
        .entity(codeExceptionMapper.codeMapper("INTERNAL_SERVER_ERROR"))
        .type("text/plain").build();
  }
}
