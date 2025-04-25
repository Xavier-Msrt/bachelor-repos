package be.vinci.pae.business.dto;

import be.vinci.pae.business.biz.InternshipImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.sql.Date;

/**
 * This interface represents a data transfer object (DTO) for Internship-related data. This
 * interface is primarily used within the user interface layer (IHM) and the data access layer
 * (DAL).
 */
@JsonDeserialize(as = InternshipImpl.class)
public interface InternshipDTO {

  /**
   * Gets the version of the internship.
   *
   * @return The internship's version.
   */
  int getVersionInternship();

  /**
   * Sets the version of the internship.
   *
   * @param versionInternship The internship's version to set.
   */
  void setVersionInternship(int versionInternship);

  /**
   * Gets the unique identifier for the internship.
   *
   * @return The internship's version.
   */
  int getIdInternship();

  /**
   * Sets the unique identifier for the internship.
   *
   * @param idInternship The internship's ID to set.
   */
  void setIdInternship(int idInternship);

  /**
   * Gets the student id associated with the internship.
   *
   * @return the student id
   */
  int getStudentIdInternship();

  /**
   * Sets the student id associated with the internship.
   *
   * @param studentIdInternship the new student id
   */
  void setStudentIdInternship(int studentIdInternship);

  /**
   * Gets the student associated with the internship.
   *
   * @return the student
   */
  UserDTO getStudent();

  /**
   * Sets the student associated with the internship.
   *
   * @param student the new student
   */
  void setStudent(UserDTO student);

  /**
   * Gets the company id associated with the internship.
   *
   * @return the company id
   */
  int getCompanyIdInternship();

  /**
   * Sets the company id associated with the internship.
   *
   * @param companyIdInternship the new company id
   */
  void setCompanyIdInternship(int companyIdInternship);

  /**
   * Gets the company associated with the internship.
   *
   * @return the company
   */
  CompanyDTO getCompany();

  /**
   * Sets the company associated with the internship.
   *
   * @param company the new company
   */
  void setCompany(CompanyDTO company);

  /**
   * Gets the school year id associated with the internship.
   *
   * @return the school year id
   */
  int getSchoolYearIdInternship();

  /**
   * Sets the school year id associated with the internship.
   *
   * @param schoolYearIdInternship the new school year id
   */
  void setSchoolYearIdInternship(int schoolYearIdInternship);

  /**
   * Gets the school year associated with the internship.
   *
   * @return the school year
   */
  SchoolYearDTO getSchoolYearInternship();

  /**
   * Sets the school year associated with the internship.
   *
   * @param schoolYearInternship the new school year
   */
  void setSchoolYearInternship(SchoolYearDTO schoolYearInternship);

  /**
   * Gets the supervisor id associated with the internship.
   *
   * @return the supervisor id
   */
  int getInternSupervisorId();

  /**
   * Sets the supervisor id associated with the internship.
   *
   * @param internSupervisorId the new supervisor id
   */
  void setInternSupervisorId(int internSupervisorId);

  /**
   * Gets the supervisor associated with the internship.
   *
   * @return the supervisor
   */
  InternshipSupervisorDTO getInternSupervisor();

  /**
   * Sets the supervisor associated with the internship.
   *
   * @param internSupervisor the new supervisor
   */
  void setInternSupervisor(InternshipSupervisorDTO internSupervisor);

  /**
   * Gets the signature date of the internship.
   *
   * @return the signature date
   */
  Date getSignatureDate();

  /**
   * Sets the signature date of the internship.
   *
   * @param signatureDate the new signature date
   */
  void setSignatureDate(Date signatureDate);

  /**
   * Gets the project associated with the internship.
   *
   * @return the project
   */
  String getInternShipProject();

  /**
   * Sets the project associated with the internship.
   *
   * @param internShipProject the new project
   */
  void setInternShipProject(String internShipProject);

  /**
   * Gets the contact associated with the internship.
   *
   * @return the contact
   */
  ContactDTO getContactInternship();

  /**
   * Sets the contact associated with the internship.
   *
   * @param contactInternship the new project
   */
  void setContactInternship(ContactDTO contactInternship);
}
