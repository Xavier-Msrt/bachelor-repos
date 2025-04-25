package be.vinci.pae.business.biz;

import be.vinci.pae.business.dto.CompanyDTO;

/**
 * The InternSupervisorImpl class implements the InternSupervisor interface. This class represents
 * an internship supervisor in the system. It contains methods for managing a supervisor's
 * information.
 */
public class InternshipSupervisorImpl implements InternshipSupervisor {

  private int idSupervisor;
  private String lastNameSupervisor;
  private String firstNameSupervisor;
  private String phoneNumberSupervisor;
  private String emailSupervisor;
  private int companyIdSupervisor;
  private CompanyDTO company;
  private int versionSupervisor;

  @Override
  public int getIdSupervisor() {
    return idSupervisor;
  }

  @Override
  public void setIdSupervisor(int idSupervisor) {
    this.idSupervisor = idSupervisor;
  }

  @Override
  public String getLastNameSupervisor() {
    return lastNameSupervisor;
  }

  @Override
  public void setLastNameSupervisor(String lastNameSupervisor) {
    this.lastNameSupervisor = lastNameSupervisor;
  }

  @Override
  public String getFirstNameSupervisor() {
    return firstNameSupervisor;
  }

  @Override
  public void setFirstNameSupervisor(String firstNameSupervisor) {
    this.firstNameSupervisor = firstNameSupervisor;
  }

  @Override
  public String getPhoneNumberSupervisor() {
    return phoneNumberSupervisor;
  }

  @Override
  public void setPhoneNumberSupervisor(String phoneNumberSupervisor) {
    this.phoneNumberSupervisor = phoneNumberSupervisor;
  }

  @Override
  public String getEmailSupervisor() {
    return emailSupervisor;
  }

  @Override
  public void setEmailSupervisor(String emailSupervisor) {
    this.emailSupervisor = emailSupervisor;
  }

  @Override
  public int getCompanyIdSupervisor() {
    return companyIdSupervisor;
  }

  @Override
  public void setCompanyIdSupervisor(int companyIdSupervisor) {
    this.companyIdSupervisor = companyIdSupervisor;
  }

  @Override
  public CompanyDTO getCompany() {
    return company;
  }

  @Override
  public void setCompany(CompanyDTO company) {
    this.company = company;
  }

  @Override
  public int getVersionSupervisor() {
    return versionSupervisor;
  }

  @Override
  public void setVersionSupervisor(int versionSupervisor) {
    this.versionSupervisor = versionSupervisor;
  }
}
