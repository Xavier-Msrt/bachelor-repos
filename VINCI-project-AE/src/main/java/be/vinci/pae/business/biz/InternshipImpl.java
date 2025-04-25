package be.vinci.pae.business.biz;

import be.vinci.pae.business.dto.CompanyDTO;
import be.vinci.pae.business.dto.ContactDTO;
import be.vinci.pae.business.dto.InternshipSupervisorDTO;
import be.vinci.pae.business.dto.SchoolYearDTO;
import be.vinci.pae.business.dto.UserDTO;
import java.sql.Date;

/**
 * The InternshipImpl class implements the Internship interface. This class represents an internship
 * in the system. It contains methods for managing an internship's information.
 */
public class InternshipImpl implements Internship {

  private int versionInternship;
  private int idInternship;
  private int studentIdInternship;
  private UserDTO student;
  private int companyIdInternship;
  private CompanyDTO company;
  private int schoolYearIdInternship;
  private SchoolYearDTO schoolYearInternship;
  private int internSupervisorId;
  private InternshipSupervisorDTO internSupervisor;
  private Date signatureDate;
  private String internShipProject;
  private ContactDTO contactInternship;

  @Override
  public int getVersionInternship() {
    return versionInternship;
  }

  @Override
  public void setVersionInternship(int versionInternship) {
    this.versionInternship = versionInternship;
  }

  @Override
  public int getIdInternship() {
    return idInternship;
  }

  @Override
  public void setIdInternship(int idInternship) {
    this.idInternship = idInternship;
  }

  @Override
  public int getStudentIdInternship() {
    return studentIdInternship;
  }

  @Override
  public void setStudentIdInternship(int studentIdInternship) {
    this.studentIdInternship = studentIdInternship;
  }

  @Override
  public UserDTO getStudent() {
    return student;
  }

  @Override
  public void setStudent(UserDTO student) {
    this.student = student;
  }

  @Override
  public int getCompanyIdInternship() {
    return companyIdInternship;
  }

  @Override
  public void setCompanyIdInternship(int companyIdInternship) {
    this.companyIdInternship = companyIdInternship;
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
  public int getSchoolYearIdInternship() {
    return schoolYearIdInternship;
  }

  @Override
  public void setSchoolYearIdInternship(int schoolYearIdInternship) {
    this.schoolYearIdInternship = schoolYearIdInternship;
  }

  @Override
  public SchoolYearDTO getSchoolYearInternship() {
    return schoolYearInternship;
  }

  @Override
  public void setSchoolYearInternship(SchoolYearDTO schoolYearInternship) {
    this.schoolYearInternship = schoolYearInternship;
  }

  @Override
  public int getInternSupervisorId() {
    return internSupervisorId;
  }

  @Override
  public void setInternSupervisorId(int internSupervisorId) {
    this.internSupervisorId = internSupervisorId;
  }

  @Override
  public InternshipSupervisorDTO getInternSupervisor() {
    return internSupervisor;
  }

  @Override
  public void setInternSupervisor(InternshipSupervisorDTO internSupervisor) {
    this.internSupervisor = internSupervisor;
  }

  @Override
  public Date getSignatureDate() {
    return signatureDate;
  }

  @Override
  public void setSignatureDate(Date signatureDate) {
    this.signatureDate = signatureDate;
  }

  @Override
  public String getInternShipProject() {
    return internShipProject;
  }

  @Override
  public void setInternShipProject(String internShipProject) {
    this.internShipProject = internShipProject;
  }

  @Override
  public ContactDTO getContactInternship() {
    return contactInternship;
  }

  @Override
  public void setContactInternship(ContactDTO contactInternship) {
    this.contactInternship = contactInternship;
  }

}
