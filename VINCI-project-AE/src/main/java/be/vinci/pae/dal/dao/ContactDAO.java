package be.vinci.pae.dal.dao;

import be.vinci.pae.business.dto.CompanyDTO;
import be.vinci.pae.business.dto.ContactDTO;
import java.util.List;

/**
 * ContactDAO provides methods to interact with users in the database.
 */
public interface ContactDAO {

  /**
   * Retrieves a ContactDTO object based on the provided student and company IDs.
   *
   * @param idStudent The ID of the student.
   * @param idCompany The ID of the company.
   * @return A ContactDTO object containing contact information.
   */
  ContactDTO getOneWithContactID(int idStudent, int idCompany);

  /**
   * Updates the state of a contact based on the provided student and company IDs.
   *
   * @param contactDTO The student.
   * @return The updated ContactDTO.
   */
  ContactDTO updateStateWithContactID(ContactDTO contactDTO);

  /**
   * Get a List of ContactDTO By giving a user id.
   *
   * @param id the user id.
   * @return a list of ContactDTO if found in db, or null.
   */
  List<ContactDTO> getAllFromId(int id);

  /**
   * Add a new contact in the database.
   *
   * @param contactDTO the contact to add
   * @return the contact added
   */
  ContactDTO addNewContact(ContactDTO contactDTO);

  /**
   * Updates the state of a contact on hold based on the current contact.
   *
   * @param contactDTO The student.
   */
  void updateStateOnHold(ContactDTO contactDTO);

  /**
   * Updates the state of all contact blacklist based on a company.
   *
   * @param companyDTO The company.
   */
  void updateStateBlacklistFromCompany(CompanyDTO companyDTO);

  /**
   * Get a List of ContactDTO By giving the company id.
   *
   * @param companyId the company id
   * @return a list of ContactDTO if found in db, or null.
   */
  List<ContactDTO> getAllContactByComapnyId(int companyId);

}
