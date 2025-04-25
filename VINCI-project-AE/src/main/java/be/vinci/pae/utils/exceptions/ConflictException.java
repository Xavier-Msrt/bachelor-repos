package be.vinci.pae.utils.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * A custom exception extended from WebApplicationException that provides information on an internal
 * server error (status code 409).This exception occurs when a user tries to access a resource that
 * is already in use or is not up-to-date.
 */
public class ConflictException extends WebApplicationException {

  /**
   * Constructs a ConflictException object with a default coded message.
   */
  public ConflictException() {
    super("CONFLICT_ERROR", Status.CONFLICT);
  }

  /**
   * Constructs a ConflictException object with a given reason.
   *
   * @param reason a description of the exception. The reason will be used as a coded message.
   */
  public ConflictException(String reason) {
    super(reason, Status.CONFLICT);
  }

  /**
   * Constructs a ConflictException object with a given cause and a default coded message.
   *
   * @param cause the underlying exception that caused this ConflictException (which is saved for
   *              the stack trace).
   */
  public ConflictException(Throwable cause) {
    super("CONFLICT_ERROR", cause, Status.CONFLICT);
  }

  /**
   * Constructs a ConflictException object with a given reason and cause. The reason will be used as
   * the message send to the client, be careful to not include sensitive information.
   *
   * @param reason a description of the exception. The reason will be used as a coded message.
   * @param cause  the underlying exception that caused this ConflictException (which is saved for
   *               the stack trace).
   */
  public ConflictException(String reason, Throwable cause) {
    super(reason, cause, Status.CONFLICT);
  }

}
