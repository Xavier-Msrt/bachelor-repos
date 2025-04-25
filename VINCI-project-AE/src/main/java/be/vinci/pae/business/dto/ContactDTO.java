package be.vinci.pae.business.dto;

import be.vinci.pae.business.biz.ContactImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * This interface represents a data transfer object (DTO) for contact-related data. It encapsulates
 * attributes contact relevant information. This interface is primarily used within the user
 * interface layer (IHM) and the data access layer (DAL).
 */
@JsonDeserialize(as = ContactImpl.class)
public interface ContactDTO {

  /**
   * Gets the contact version.
   *
   * @return the contact version.
   */
  int getVersionContact();

  /**
   * Sets the contact version.
   *
   * @param versionContact the contact version.
   */
  void setVersionContact(int versionContact);

  /**
   * Gets the student user id.
   *
   * @return the student user id.
   */
  int getStudentIdContact();

  /**
   * Sets the student user id.
   *
   * @param studentId the student user id
   */
  void setStudentIdContact(int studentId);

  /**
   * Gets the student user.
   *
   * @return the student user.
   */
  UserDTO getStudent();

  /**
   * Sets the student user.
   *
   * @param student the student user.
   */
  void setStudent(UserDTO student);

  /**
   * Gets the company ID.
   *
   * @return the company ID.
   */
  int getCompanyIdContact();

  /**
   * Sets the company ID.
   *
   * @param companyIdContact the company ID.
   */
  void setCompanyIdContact(int companyIdContact);

  /**
   * Gets the company ID.
   *
   * @return the company ID.
   */
  CompanyDTO getCompany();

  /**
   * Sets the company ID.
   *
   * @param company the company ID.
   */
  void setCompany(CompanyDTO company);

  /**
   * Gets the school year id.
   *
   * @return the school year id.
   */
  int getSchoolYearIdContact();

  /**
   * Sets the school year id.
   *
   * @param schoolYearId the school year id.
   */
  void setSchoolYearIdContact(int schoolYearId);

  /**
   * Gets the school year.
   *
   * @return the school year.
   */
  SchoolYearDTO getSchoolYearContact();

  /**
   * Sets the school year.
   *
   * @param schoolYear the school year.
   */
  void setSchoolYearContact(SchoolYearDTO schoolYear);

  /**
   * Gets the state of the contact.
   *
   * @return the state of the contact.
   */
  State getState();

  /**
   * Sets the state of the contact.
   *
   * @param state the state of the contact.
   */
  void setState(State state);

  /**
   * Gets the meeting place.
   *
   * @return the meeting place.
   */
  MeetingPlace getMeetingPlace();

  /**
   * Sets the meeting place.
   *
   * @param meetingPlace the meeting place.
   */
  void setMeetingPlace(MeetingPlace meetingPlace);

  /**
   * Gets the reason for refusal.
   *
   * @return the reason for refusal.
   */
  String getReasonRefusal();

  /**
   * Sets the reason for refusal.
   *
   * @param reasonRefusal the reason for refusal.
   */
  void setReasonRefusal(String reasonRefusal);

  /**
   * Represents the state of the contact.
   */
  enum State {
    /**
     * Initial state when the process is initiated.
     */
    STARTED,

    /**
     * State indicating that the process has been taken or started.
     */
    ADMITTED,

    /**
     * State indicating that the process has been suspended.
     */
    ON_HOLD,

    /**
     * State indicating that the process is blacklisted or restricted.
     */
    BLACKLISTED,

    /**
     * State indicating that the process was interrupted or halted.
     */
    INTERRUPTED,

    /**
     * State indicating that the process has been accepted or approved.
     */
    ACCEPTED,

    /**
     * State indicating that the process has been refused or denied.
     */
    TURNED_DOWN;

    /**
     * Returns the string representation of the state.
     *
     * @return the string representation of the state
     */
    @Override
    public String toString() {
      return super.toString().toUpperCase();
    }
  }

  /**
   * Represents the meeting place of the contact.
   */
  enum MeetingPlace {
    /**
     * Online meeting.
     */
    REMOTE("REMOTE"),

    /**
     * Physical meeting.
     */
    ONSITE("ONSITE");

    private final String value;

    MeetingPlace(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value != null ? value.toUpperCase() : null;
    }
  }

}
