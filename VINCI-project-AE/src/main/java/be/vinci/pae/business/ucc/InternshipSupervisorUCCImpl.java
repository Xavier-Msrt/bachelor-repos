package be.vinci.pae.business.ucc;

import be.vinci.pae.business.biz.InternshipSupervisor;
import be.vinci.pae.business.dto.CompanyDTO;
import be.vinci.pae.business.dto.InternshipSupervisorDTO;
import be.vinci.pae.dal.DALBizService;
import be.vinci.pae.dal.dao.CompanyDAO;
import be.vinci.pae.dal.dao.InternshipSupervisorDAO;
import be.vinci.pae.utils.exceptions.ConflictException;
import be.vinci.pae.utils.exceptions.NotFoundException;
import jakarta.inject.Inject;
import java.util.List;

/**
 * This class is an implementation of the InternshipSupervisorUCC interface and represents an
 * internship supervisor Use Case Controller (UCC) in the system. This interface is responsible for
 * handling internship-supervisor-related operations.
 */
public class InternshipSupervisorUCCImpl implements InternshipSupervisorUCC {

  @Inject
  private InternshipSupervisorDAO supervisorDAO;

  @Inject
  private CompanyDAO companyDAO;

  @Inject
  private DALBizService dalBizServices;

  @Override
  public InternshipSupervisorDTO addInternshipSupervisors(InternshipSupervisorDTO supervisorDTO) {

    InternshipSupervisor internSupervisor = (InternshipSupervisor) supervisorDTO;

    InternshipSupervisorDTO returnSupervisor = null;
    try {
      dalBizServices.startTransaction();

      // Check if Internship Supervisors exist
      InternshipSupervisorDTO supervisorsExist = supervisorDAO.getOneInternshipSupervisorByEmail(
          supervisorDTO.getEmailSupervisor());
      if (supervisorsExist != null) {
        throw new ConflictException("SUPERVISOR_EXISTS");
      }

      // Check if the company exist
      CompanyDTO companyDTO = companyDAO.getOneById(supervisorDTO.getCompanyIdSupervisor());
      if (companyDTO == null) {
        throw new NotFoundException();
      }

      // Setup default version
      internSupervisor.setVersionSupervisor(1);

      // Insert the Intern
      returnSupervisor = supervisorDAO.insertInternshipSupervisor(supervisorDTO);

      dalBizServices.commitTransaction();
    } catch (Exception e) {
      dalBizServices.rollbackTransaction();
      throw e;
    }
    return returnSupervisor;

  }

  @Override
  public List<InternshipSupervisorDTO> getAllInternshipSupervisors(int idCompany) {

    // Check if the company exist
    CompanyDTO companyDTO = companyDAO.getOneById(idCompany);
    if (companyDTO == null) {
      throw new NotFoundException();
    }

    return supervisorDAO.getAllISWithCompanyID(idCompany);
  }

}
