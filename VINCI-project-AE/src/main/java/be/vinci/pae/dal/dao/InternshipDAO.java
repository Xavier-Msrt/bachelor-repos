package be.vinci.pae.dal.dao;

import be.vinci.pae.business.dto.InternshipDTO;

/**
 * InternshipDAO interface provides methods to interact with Internship in the database.
 */
public interface InternshipDAO {

  /**
   * Get an internship by the id of the student.
   *
   * @param idStudent the id of the student to get his internship
   * @return an InternshipDTO object representing the found internship, or null
   */
  InternshipDTO getOneByStudentId(int idStudent);

  /**
   * Get the total number of internships.
   *
   * @return The total number of internships.
   */
  int getInternshipsCount();

  /**
   * Get the total number of internships for a specific year.
   *
   * @param yearId The year for which the internships count is to be retrieved.
   * @return The total number of internships for the specified year.
   */
  int getInternshipsCountByYear(int yearId);

  /**
   * Create an internship by the internshipDTO.
   *
   * @param internshipDTO the internship information
   * @return an InternshipDTO object representing the internship
   */
  InternshipDTO createOneInternship(InternshipDTO internshipDTO);

  /**
   * Update an internship project by the internshipDTO received.
   *
   * @param internshipDTOToUpdate the internship information to update
   * @return an InternshipDTO object representing the internship updated
   */
  InternshipDTO updateInternShipProject(InternshipDTO internshipDTOToUpdate);
}
