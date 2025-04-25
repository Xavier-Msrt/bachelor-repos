package be.vinci.pae.dal.dao;

import be.vinci.pae.business.dto.InternshipSupervisorDTO;
import java.util.List;

/**
 * InternshipSupervisorDAO interface provides methods to interact with InternshipSupervisor in the
 * database.
 */
public interface InternshipSupervisorDAO {

  /**
   * Insert a new InternshipSupervisor in the database.
   *
   * @param supervisor the complete InternshipSupervisor
   * @return the InternshipSupervisor added
   */
  InternshipSupervisorDTO insertInternshipSupervisor(InternshipSupervisorDTO supervisor);


  /**
   * Get an InternshipSupervisor by his email.
   *
   * @param email the email of the internshipSupervisor
   * @return a full internshipSupervisor
   */
  InternshipSupervisorDTO getOneInternshipSupervisorByEmail(String email);

  /**
   * Get an internship by the id of the student.
   *
   * @param internshipSupervisorID the id of the internship's supervisor to get the supervisor
   * @return an InternshipSupervisorDTO object representing the found internshipSupervisor, or null
   */
  InternshipSupervisorDTO getOneISWithID(int internshipSupervisorID);

  /**
   * Get all internship by the id of the company.
   *
   * @param idCompany The company's ID
   * @return a List of InternshipSupervisorsDTO object or null
   */
  List<InternshipSupervisorDTO> getAllISWithCompanyID(int idCompany);

}
