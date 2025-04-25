package be.vinci.pae.utils.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * A custom exception extended from WebApplicationException that provides information on a business
 * contract compliance error (status code 412). This exception occurs when a user does not respect a
 * business contract condition or pre-condition.
 */
public class ContractException extends WebApplicationException {

  /**
   * Constructs a ContractException object with a default coded message.
   */
  public ContractException() {
    super("INCORRECT_VALUE", Status.PRECONDITION_FAILED);
  }

  /**
   * Constructs a ContractException object with a given reason.
   *
   * @param reason a description of the exception. The reason will be used as a coded message.
   */
  public ContractException(String reason) {
    super(reason, Status.PRECONDITION_FAILED);
  }

  /**
   * Constructs a ContractException object with a given cause and a default coded message.
   *
   * @param cause the underlying exception that caused this ContractException (which is saved for
   *              the stack trace).
   */
  public ContractException(Throwable cause) {
    super("INCORRECT_VALUE", cause, Status.PRECONDITION_FAILED);
  }

  /**
   * Constructs a ContractException object with a given reason and cause.
   *
   * @param reason a description of the exception. The reason will be used as a coded message.
   * @param cause  the underlying exception that caused this ContractException (which is saved for
   *               the stack trace).
   */
  public ContractException(String reason, Throwable cause) {
    super(reason, cause, Status.PRECONDITION_FAILED);
  }

}


