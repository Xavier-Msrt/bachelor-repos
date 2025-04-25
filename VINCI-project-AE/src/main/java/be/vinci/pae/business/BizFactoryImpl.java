package be.vinci.pae.business;

import be.vinci.pae.business.biz.CompanyImpl;
import be.vinci.pae.business.biz.ContactImpl;
import be.vinci.pae.business.biz.InternshipImpl;
import be.vinci.pae.business.biz.InternshipSupervisorImpl;
import be.vinci.pae.business.biz.SchoolYearImpl;
import be.vinci.pae.business.biz.UserImpl;
import be.vinci.pae.business.dto.CompanyDTO;
import be.vinci.pae.business.dto.ContactDTO;
import be.vinci.pae.business.dto.InternshipDTO;
import be.vinci.pae.business.dto.InternshipSupervisorDTO;
import be.vinci.pae.business.dto.SchoolYearDTO;
import be.vinci.pae.business.dto.UserDTO;

/**
 * This class is an implementation of the BizFactory interface.
 */
public class BizFactoryImpl implements BizFactory {

  @Override
  public UserDTO getUser() {
    return new UserImpl();
  }

  @Override
  public ContactDTO getContact() {
    return new ContactImpl();
  }

  @Override
  public CompanyDTO getCompany() {
    return new CompanyImpl();
  }

  @Override
  public InternshipDTO getInternship() {
    return new InternshipImpl();
  }

  @Override
  public InternshipSupervisorDTO getInternshipSupervisor() {
    return new InternshipSupervisorImpl();
  }

  @Override
  public SchoolYearDTO getSchoolYear() {
    return new SchoolYearImpl();
  }

}
