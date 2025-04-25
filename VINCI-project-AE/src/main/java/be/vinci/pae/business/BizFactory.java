package be.vinci.pae.business;

import be.vinci.pae.business.dto.CompanyDTO;
import be.vinci.pae.business.dto.ContactDTO;
import be.vinci.pae.business.dto.InternshipDTO;
import be.vinci.pae.business.dto.InternshipSupervisorDTO;
import be.vinci.pae.business.dto.SchoolYearDTO;
import be.vinci.pae.business.dto.UserDTO;

/**
 * This interface represents a factory for creating biz objects.
 */
public interface BizFactory {

  /**
   * Creates a new UserDTO object.
   *
   * @return a new UserDTO object
   */
  UserDTO getUser();

  /**
   * Creates a new SchoolYears object.
   *
   * @return a new SchoolYears object
   */
  SchoolYearDTO getSchoolYear();

  /**
   * Creates a new ContactDTO object.
   *
   * @return a new ContactDTO object
   */
  ContactDTO getContact();

  /**
   * Creates a new CompanyDTO object.
   *
   * @return a new CompanyDTO object
   */
  CompanyDTO getCompany();

  /**
   * Creates a new InternshipDTO object.
   *
   * @return a new InternshipDTO object
   */
  InternshipDTO getInternship();

  /**
   * Creates a new InternSupervisorDTO object.
   *
   * @return a new InternSupervisorDTO object
   */
  InternshipSupervisorDTO getInternshipSupervisor();
}
