package be.vinci.pae.business.biz;

import be.vinci.pae.business.dto.CompanyDTO;
import be.vinci.pae.business.dto.SchoolYearDTO;
import be.vinci.pae.business.dto.UserDTO;

/**
 * The ContactImpl class implements the Contact interface. This class represents a contact in the
 * system. It contains methods for managing the contact's information.
 */
public class ContactImpl implements Contact {

  private int versionContact;
  private int studentIdContact;
  private UserDTO student;
  private int companyIdContact;
  private CompanyDTO company;
  private int schoolYearIdContact;
  private SchoolYearDTO schoolYearContact;
  private State state;
  private MeetingPlace meetingPlace;
  private String reasonRefusal;

  @Override
  public int getVersionContact() {
    return versionContact;
  }

  @Override
  public void setVersionContact(int versionContact) {
    this.versionContact = versionContact;
  }

  @Override
  public int getStudentIdContact() {
    return studentIdContact;
  }

  @Override
  public void setStudentIdContact(int studentIdContact) {
    this.studentIdContact = studentIdContact;
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
  public int getCompanyIdContact() {
    return companyIdContact;
  }

  @Override
  public void setCompanyIdContact(int companyIdContact) {
    this.companyIdContact = companyIdContact;
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
  public SchoolYearDTO getSchoolYearContact() {
    return schoolYearContact;
  }

  @Override
  public void setSchoolYearContact(SchoolYearDTO schoolYear) {
    this.schoolYearContact = schoolYear;
  }

  @Override
  public int getSchoolYearIdContact() {
    return schoolYearIdContact;
  }

  @Override
  public void setSchoolYearIdContact(int schoolYearId) {
    this.schoolYearIdContact = schoolYearId;
  }

  @Override
  public State getState() {
    return state;
  }

  @Override
  public void setState(State state) {
    this.state = state;
  }

  @Override
  public MeetingPlace getMeetingPlace() {
    return meetingPlace;
  }

  @Override
  public void setMeetingPlace(MeetingPlace meetingPlace) {
    this.meetingPlace = meetingPlace;
  }

  @Override
  public String getReasonRefusal() {
    return reasonRefusal;
  }

  @Override
  public void setReasonRefusal(String reasonRefusal) {
    this.reasonRefusal = reasonRefusal;
  }

  @Override
  public boolean isInProcess(State state) {
    return state.equals(State.STARTED) || state.equals(State.ADMITTED);
  }

  @Override
  public void setupNewContact(int idStudent, int idCompany) {
    this.setCompanyIdContact(idCompany);
    this.setVersionContact(1);
    this.setState(State.STARTED);
    this.setStudentIdContact(idStudent);
  }

  @Override
  public boolean checkTurnedDown() {
    return state.equals(State.TURNED_DOWN) && (reasonRefusal == null || reasonRefusal.isBlank());
  }

  @Override
  public boolean checkAdmitted() {
    return state.equals(State.ADMITTED) && meetingPlace == null;
  }
}
