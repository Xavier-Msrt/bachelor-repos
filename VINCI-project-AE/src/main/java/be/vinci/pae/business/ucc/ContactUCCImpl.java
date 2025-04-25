package be.vinci.pae.business.ucc;

import be.vinci.pae.business.BizFactory;
import be.vinci.pae.business.biz.Contact;
import be.vinci.pae.business.dto.CompanyDTO;
import be.vinci.pae.business.dto.ContactDTO;
import be.vinci.pae.business.dto.ContactDTO.State;
import be.vinci.pae.business.dto.SchoolYearDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.utils.Utils;
import be.vinci.pae.dal.DALBizService;
import be.vinci.pae.dal.dao.CompanyDAO;
import be.vinci.pae.dal.dao.ContactDAO;
import be.vinci.pae.dal.dao.SchoolYearDAO;
import be.vinci.pae.utils.exceptions.ConflictException;
import be.vinci.pae.utils.exceptions.ContractException;
import be.vinci.pae.utils.exceptions.FatalException;
import be.vinci.pae.utils.exceptions.NotFoundException;
import jakarta.inject.Inject;
import java.util.List;

/**
 * The ContactUCCImpl class is an implementation of the ContactUCC interface and represents a
 * contact Use Case Controller (UCC) in the system. This interface is responsible for handling
 * contact-related operations.
 */
public class ContactUCCImpl implements ContactUCC {

  @Inject
  private ContactDAO contactDAO;

  @Inject
  private SchoolYearDAO schoolYearDAO;

  @Inject
  private CompanyDAO companyDAO;

  @Inject
  private DALBizService bizServices;

  @Inject
  private BizFactory bizFactory;

  @Inject
  private Utils utils;

  @Override
  public ContactDTO getOneWithID(int idStudent, int idCompany) {
    return contactDAO.getOneWithContactID(idStudent, idCompany);
  }

  @Override
  public ContactDTO updateStateWithID(ContactDTO contactDTOToUpdate) {

    Contact contactToUpdate = (Contact) contactDTOToUpdate;

    if (contactToUpdate.checkTurnedDown()) {
      throw new ContractException("TURNED_DOWN_NO_REFUSAL");
    }

    if (contactToUpdate.checkAdmitted()) {
      throw new ContractException("ADMITTED_NO_MEETING");
    }

    if (contactToUpdate.getState().equals(State.ACCEPTED)) {
      throw new ContractException("ACCEPTED_INTERNSHIP");
    }

    ContactDTO contactDTOAfterUpdate = null;

    try {

      bizServices.startTransaction();

      ContactDTO contactDTOBeforeUpdate = getOneWithID(contactDTOToUpdate.getStudentIdContact(),
          contactDTOToUpdate.getCompanyIdContact());

      if (contactDTOBeforeUpdate == null) {
        throw new NotFoundException();
      }

      Contact contactBefore = (Contact) contactDTOBeforeUpdate;

      if (!contactBefore.isInProcess(contactBefore.getState())) {
        throw new ContractException("IN_PROCESS_STATE");

      }

      if (contactBefore.getState().equals(contactDTOToUpdate.getState())) {
        throw new ContractException("SAME_STATE");
      }

      contactDTOAfterUpdate = contactDAO.updateStateWithContactID(contactDTOToUpdate);

      if (contactDTOAfterUpdate == null) {
        throw new ConflictException("CONFLICT_ERROR");
      }

      bizServices.commitTransaction();

    } catch (Exception e) {
      bizServices.rollbackTransaction();
      throw e;
    }

    return contactDTOAfterUpdate;
  }

  @Override
  public List<ContactDTO> getAllFromId(int id) {
    List<ContactDTO> contacts = contactDAO.getAllFromId(id);
    if (contacts == null) {
      throw new NotFoundException();
    }
    return contacts;
  }

  @Override
  public ContactDTO createNewContact(int companyId, UserDTO userDTO) {
    //biz setup
    ContactDTO contactDTO = bizFactory.getContact();

    try {
      bizServices.startTransaction();

      CompanyDTO companyDTO = companyDAO.getOneById(companyId);
      if (companyDTO == null) {
        throw new ContractException("NOT_FOUND");
      }

      // Put the contact in the right state for creation
      Contact contact = (Contact) contactDTO;
      contact.setupNewContact(userDTO.getIdUser(), companyId);

      // check if contact idee exist
      ContactDTO findContact = contactDAO.getOneWithContactID(contactDTO.getStudentIdContact(),
          contactDTO.getCompanyIdContact());
      if (findContact != null) {
        throw new ConflictException("COMPANY_EXISTS");
      }

      // get school year id
      int startYear = utils.currentSchoolYearStart();

      SchoolYearDTO schoolYearDTO = schoolYearDAO.getOneByStartingDate(startYear);
      if (schoolYearDTO == null) {
        throw new FatalException("INTERNAL_SERVER_ERROR");
      }

      contactDTO.setSchoolYearIdContact(schoolYearDTO.getIdSchoolYear());

      // insert the new contact
      ContactDTO newContact = contactDAO.addNewContact(contactDTO);

      bizServices.commitTransaction();

      // return the new contact
      return newContact;
    } catch (Exception e) {
      bizServices.rollbackTransaction();
      throw e;
    }

  }

  @Override
  public List<ContactDTO> getAllFromCompanyId(int companyId) {
    List<ContactDTO> contacts = null;
    try {
      bizServices.startTransaction();
      // check company exist
      CompanyDTO company = companyDAO.getOneById(companyId);
      if (company == null) {
        throw new NotFoundException();
      }
      contacts = contactDAO.getAllContactByComapnyId(companyId);

      bizServices.commitTransaction();
    } catch (Exception e) {
      bizServices.rollbackTransaction();
      throw e;
    }
    return contacts;
  }

}
