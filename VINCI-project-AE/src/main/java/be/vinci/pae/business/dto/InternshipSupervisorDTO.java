package be.vinci.pae.business.dto;

import be.vinci.pae.business.biz.InternshipSupervisorImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * This interface represents a data transfer object (DTO) for InternSupervisor-related data. This
 * interface is primarily used within the user interface layer (IHM) and the data access layer
 * (DAL).
 */
@JsonDeserialize(as = InternshipSupervisorImpl.class)
public interface InternshipSupervisorDTO {

  /**
   * Gets the id of the supervisor.
   *
   * @return the id of the supervisor
   */
  int getIdSupervisor();

  /**
   * Sets the id of the supervisor.
   *
   * @param idSupervisor the new id of the supervisor
   */
  void setIdSupervisor(int idSupervisor);

  /**
   * Gets the last name of the supervisor.
   *
   * @return the last name of the supervisor
   */
  String getLastNameSupervisor();

  /**
   * Sets the last name of the supervisor.
   *
   * @param lastNameSupervisor the new last name of the supervisor
   */
  void setLastNameSupervisor(String lastNameSupervisor);

  /**
   * Gets the first name of the supervisor.
   *
   * @return the first name of the supervisor
   */
  String getFirstNameSupervisor();

  /**
   * Sets the first name of the supervisor.
   *
   * @param firstNameSupervisor the new first name of the supervisor
   */
  void setFirstNameSupervisor(String firstNameSupervisor);

  /**
   * Gets the phone number of the supervisor.
   *
   * @return the phone number of the supervisor
   */
  String getPhoneNumberSupervisor();

  /**
   * Sets the phone number of the supervisor.
   *
   * @param phoneNumberSupervisor the new phone number of the supervisor
   */
  void setPhoneNumberSupervisor(String phoneNumberSupervisor);

  /**
   * Gets the email of the supervisor.
   *
   * @return the email of the supervisor
   */
  String getEmailSupervisor();

  /**
   * Sets the email of the supervisor.
   *
   * @param emailSupervisor the new email of the supervisor
   */
  void setEmailSupervisor(String emailSupervisor);

  /**
   * Gets the company id of the supervisor.
   *
   * @return the company id of the supervisor
   */
  int getCompanyIdSupervisor();

  /**
   * Sets the company id of the supervisor.
   *
   * @param companyIdSupervisor the new company id of the supervisor
   */
  void setCompanyIdSupervisor(int companyIdSupervisor);

  /**
   * Gets the company of the supervisor.
   *
   * @return the company of the supervisor
   */
  CompanyDTO getCompany();

  /**
   * Sets the company of the supervisor.
   *
   * @param company the new company of the supervisor
   */
  void setCompany(CompanyDTO company);

  /**
   * Gets the version of the supervisor.
   *
   * @return the version of the supervisor
   */
  int getVersionSupervisor();

  /**
   * Sets the version of the supervisor.
   *
   * @param versionSupervisor the new version of the supervisor
   */
  void setVersionSupervisor(int versionSupervisor);
}