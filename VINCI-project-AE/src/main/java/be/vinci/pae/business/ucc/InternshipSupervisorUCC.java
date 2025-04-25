package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.InternshipSupervisorDTO;
import java.util.List;

/**
 * This interface represents an internship supervisor Use Case Controller (UCC) in the system. This
 * class is responsible for handling internship-related operations.
 */
public interface InternshipSupervisorUCC {


  /**
   * This method add a new Internship Supervisor based on the provided InternshipSupervisorDTO.
   *
   * @param supervisorDTO the InternshipSupervisorDTO to add
   * @return the Internship Supervisor added
   */
  InternshipSupervisorDTO addInternshipSupervisors(InternshipSupervisorDTO supervisorDTO);

  /**
   * This method get all Internship Supervisor based on the company's id.
   *
   * @param idCompany the company's id
   * @return the Internship Supervisor added
   */
  List<InternshipSupervisorDTO> getAllInternshipSupervisors(int idCompany);
}
