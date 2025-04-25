package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.ContactDTO;
import be.vinci.pae.business.dto.ContactDTO.State;
import be.vinci.pae.business.dto.InternshipDTO;
import be.vinci.pae.dal.DALBizService;
import be.vinci.pae.dal.dao.ContactDAO;
import be.vinci.pae.dal.dao.InternshipDAO;
import be.vinci.pae.dal.dao.InternshipSupervisorDAO;
import be.vinci.pae.utils.exceptions.ConflictException;
import be.vinci.pae.utils.exceptions.ContractException;
import be.vinci.pae.utils.exceptions.NotFoundException;
import jakarta.inject.Inject;

/**
 * This class is an implementation of the InternshipUCC interface and represents an internship Use
 * Case Controller (UCC) in the system. This interface is responsible for handling
 * internship-related operations.
 */
public class InternshipUCCImpl implements InternshipUCC {

  @Inject
  private InternshipDAO internshipDAO;
  @Inject
  private DALBizService bizService;
  @Inject
  private ContactDAO contactDAO;
  @Inject
  private InternshipSupervisorDAO internshipSupervisorDAO;


  @Override
  public InternshipDTO getOneByStudentId(int idStudent) {
    return internshipDAO.getOneByStudentId(idStudent);
  }

  @Override
  public int getInternshipsCount() {
    return internshipDAO.getInternshipsCount();
  }

  @Override
  public int getInternshipsCountByYear(int yearId) {
    return internshipDAO.getInternshipsCountByYear(yearId);
  }

  @Override
  public InternshipDTO createOneInternship(InternshipDTO internshipDTO) {
    InternshipDTO internshipDTOCreated = null;
    ContactDTO contactDTOToUpdate = internshipDTO.getContactInternship();

    try {
      bizService.startTransaction();

      if (internshipDAO.getOneByStudentId(internshipDTO.getStudentIdInternship()) != null) {
        throw new ContractException("INTERNSHIP_EXISTS");
      }

      ContactDTO contactDTOBeforeUpdate = contactDAO.getOneWithContactID(
          contactDTOToUpdate.getStudentIdContact(),
          contactDTOToUpdate.getCompanyIdContact());

      if (contactDTOBeforeUpdate == null) {
        throw new NotFoundException();
      }

      if (!contactDTOBeforeUpdate.getState().equals(State.ADMITTED)) {
        throw new ContractException("ADMITTED_STATE");
      }

      ContactDTO contactDTOAfterUpdate = contactDAO.updateStateWithContactID(contactDTOToUpdate);

      if (contactDTOAfterUpdate == null) {
        throw new ConflictException("CONFLICT_ERROR");
      }

      contactDAO.updateStateOnHold(contactDTOToUpdate);

      if (internshipSupervisorDAO.getOneISWithID(internshipDTO.getInternSupervisorId()) == null) {
        throw new NotFoundException();
      }

      internshipDTOCreated = internshipDAO.createOneInternship(internshipDTO);

      bizService.commitTransaction();
    } catch (Exception e) {
      bizService.rollbackTransaction();
      throw e;
    }
    return internshipDTOCreated;
  }

  @Override
  public InternshipDTO updateInternShipProject(InternshipDTO internshipDTOToUpdate) {
    InternshipDTO internshipDTOUpdated = null;
    try {
      bizService.startTransaction();

      InternshipDTO internshipDTO = internshipDAO.getOneByStudentId(
          internshipDTOToUpdate.getStudentIdInternship());

      if (internshipDTO == null) {
        throw new NotFoundException();
      }

      if (internshipDTO.getInternSupervisorId() != internshipDTOToUpdate.getInternSupervisorId()
          || internshipDTO.getCompanyIdInternship()
          != internshipDTOToUpdate.getCompanyIdInternship()
          || internshipDTO.getStudentIdInternship()
          != internshipDTOToUpdate.getStudentIdInternship()) {
        throw new ContractException("INCORRECT_VALUE");
      }

      ContactDTO contactDTO = contactDAO.getOneWithContactID(
          internshipDTOToUpdate.getStudentIdInternship(),
          internshipDTOToUpdate.getCompanyIdInternship());

      if (contactDTO == null) {
        throw new NotFoundException("CONTACT_ATTACH");
      }

      if (!contactDTO.getState().equals(State.ACCEPTED)) {
        throw new ContractException("ACCEPTED_STATE");
      }

      internshipDTOUpdated = internshipDAO.updateInternShipProject(internshipDTOToUpdate);

      if (internshipDTOUpdated == null) {
        throw new ConflictException("CONFLICT_ERROR");
      }

      bizService.commitTransaction();
    } catch (Exception e) {
      bizService.rollbackTransaction();
      throw e;
    }
    return internshipDTOUpdated;
  }
}
