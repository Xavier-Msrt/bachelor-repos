package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.InternshipDTO;

/**
 * This interface represents an internship Use Case Controller (UCC) in the system. This class is
 * responsible for handling internship-related operations.
 */
public interface InternshipUCC {

  /**
   * This method is responsible for getting an internship by the id of the student.
   *
   * @param idStudent the id of the student to get his internship
   * @return the InternshipDTO object representing the internship of the student id or null
   */
  InternshipDTO getOneByStudentId(int idStudent);

  /**
   * Retrieves the total number of internships.
   *
   * @return The total number of internships.
   */
  int getInternshipsCount();

  /**
   * Retrieves the total number of internships for a specific year.
   *
   * @param yearId The year for which the internships count is to be retrieved.
   * @return The total number of internships for the specified year.
   */
  int getInternshipsCountByYear(int yearId);


  /**
   * This method is responsible for creating an internship.
   *
   * @param internshipDTO the internship's information
   * @return the InternshipDTO object representing the internship
   */
  InternshipDTO createOneInternship(InternshipDTO internshipDTO);

  /**
   * This method is responsible for updating an internship project.
   *
   * @param internshipDTOToUpdate the internship's information to update
   * @return the InternshipDTO object representing the updated internship
   */
  InternshipDTO updateInternShipProject(InternshipDTO internshipDTOToUpdate);
}
