package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.ContactDTO;
import be.vinci.pae.business.dto.UserDTO;
import java.util.List;

/**
 * This interface defines methods for retrieving contact information based on student and company
 * IDs.
 */
public interface ContactUCC {

  /**
   * Retrieves a ContactDTO object based on the provided student and company IDs.
   *
   * @param idStudent The ID of the student.
   * @param idCompany The ID of the company.
   * @return A ContactDTO object containing contact information.
   */
  ContactDTO getOneWithID(int idStudent, int idCompany);

  /**
   * Updates the state of a contact with the specified student ID and company ID.
   *
   * @param contactDTOToUpdate The student to update.
   * @return The updated ContactDTO.
   */
  ContactDTO updateStateWithID(ContactDTO contactDTOToUpdate);

  /**
   * This method retrieves a list of contacts associated with the provided user ID.
   *
   * @param id the ID of the user for whom contacts are being retrieved
   * @return a list of ContactDTO objects representing the contacts associated with the user ID
   */
  List<ContactDTO> getAllFromId(int id);

  /**
   * This method creates a new contact based on the provided ContactDTO and UserDTO objects.
   *
   * @param companyId the company ID associated with the new contact
   * @param userDTO   the UserDTO object representing the user associated with the new contact
   * @return the newly created ContactDTO object
   */
  ContactDTO createNewContact(int companyId, UserDTO userDTO);

  /**
   * This method retrieves a list of contacts associated with the provided company id.
   *
   * @param companyId the company id
   * @return a list of ContactDTO objects representing the contacts associated with the user ID
   */
  List<ContactDTO> getAllFromCompanyId(int companyId);
}



