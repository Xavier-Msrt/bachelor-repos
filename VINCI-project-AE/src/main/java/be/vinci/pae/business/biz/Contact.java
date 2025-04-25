package be.vinci.pae.business.biz;

import be.vinci.pae.business.dto.ContactDTO;

/**
 * The {@code Contact} interface represents not only a data transfer object (DTO) for user-related
 * data and clever function. This interface is primarily used within the use case controller (UCC).
 */
public interface Contact extends ContactDTO {

  int getStudentIdContact();

  void setStudentIdContact(int studentIdContact);

  /**
   * Checks if the contact is in process based on the state.
   *
   * @param state the state of the contact.
   * @return true if the contact is in process, false otherwise.
   */
  boolean isInProcess(State state);


  /**
   * Check if turned down is associated with message of refusal.
   *
   * @return true if contact is turnDown otherwise.
   */
  boolean checkTurnedDown();

  /**
   * Check if admitted is associated with meeting place.
   *
   * @return true if contact is admitted otherwise false.
   */
  boolean checkAdmitted();

  /**
   * Set up the contact with default value for create a new contact.
   *
   * @param idStudent the id of the student for contact
   * @param idCompany the id of the company for contact
   */
  void setupNewContact(int idStudent, int idCompany);

}
