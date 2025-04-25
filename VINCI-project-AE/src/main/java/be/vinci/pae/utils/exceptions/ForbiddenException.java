package be.vinci.pae.utils.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * A custom exception extended from WebApplicationException that provides information on an
 * authorization error (status code 403). This exception occurs when a not authorized user tries to
 * access a protected resource.
 */
public class ForbiddenException extends WebApplicationException {

  /**
   * Constructs a ForbiddenException object with a default coded message.
   */
  public ForbiddenException() {
    super("ACCESS_ERROR", Status.FORBIDDEN);
  }

  /**
   * Constructs a ForbiddenException object with a given reason.
   *
   * @param reason a description of the exception. The reason will be used as a coded message.
   */
  public ForbiddenException(String reason) {
    super(reason, Status.FORBIDDEN);
  }

  /**
   * Constructs a ForbiddenException object with a given cause and a default coded message.
   *
   * @param cause the underlying exception that caused this ForbiddenException (which is saved for
   *              the stack trace).
   */
  public ForbiddenException(Throwable cause) {
    super("ACCESS_ERROR", cause, Status.FORBIDDEN);
  }

  /**
   * Constructs a ForbiddenException object with a given reason and cause.
   *
   * @param reason a description of the exception. The reason will be used as a coded message.
   * @param cause  the underlying exception that caused this ForbiddenException (which is saved for
   *               the stack trace).
   */
  public ForbiddenException(String reason, Throwable cause) {
    super(reason, cause, Status.FORBIDDEN);
  }

}


