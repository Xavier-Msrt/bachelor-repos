package be.vinci.pae.utils.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * A custom exception extended from WebApplicationException that provides information on an
 * authentication error (status code 401). This exception occurs when a not authenticated user tries
 * to access a protected resource.
 */
public class UnauthorizedException extends WebApplicationException {

  /**
   * Constructs an UnauthorizedException object with a default coded message.
   */
  public UnauthorizedException() {
    super("UNAUTHORIZED", Status.UNAUTHORIZED);
  }

  /**
   * Constructs an UnauthorizedException object with a given reason.
   *
   * @param reason a description of the exception. The reason will be used as a coded message.
   */
  public UnauthorizedException(String reason) {
    super(reason, Status.UNAUTHORIZED);
  }

  /**
   * Constructs an UnauthorizedException object with a given cause and a default coded message.
   *
   * @param cause the underlying exception that caused this UnauthorizedException (which is saved
   *              for the stack trace).
   */
  public UnauthorizedException(Throwable cause) {
    super("UNAUTHORIZED", cause, Status.UNAUTHORIZED);
  }

  /**
   * Constructs an UnauthorizedException object with a given reason and cause.
   *
   * @param reason a description of the exception. The reason will be used as a coded message.
   * @param cause  the underlying exception that caused this UnauthorizedException (which is saved
   *               for the stack trace).
   */
  public UnauthorizedException(String reason, Throwable cause) {
    super(reason, cause, Status.UNAUTHORIZED);
  }

}


