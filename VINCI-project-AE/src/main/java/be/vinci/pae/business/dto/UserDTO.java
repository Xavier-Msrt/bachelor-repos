package be.vinci.pae.business.dto;

import be.vinci.pae.business.biz.UserImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.sql.Date;

/**
 * This interface represents a data transfer object (DTO) for user-related data. It encapsulates
 * attributes such as user first name, last name, email and other relevant information. This
 * interface is primarily used within the user interface layer (IHM) and the data access layer
 * (DAL).
 */
@JsonDeserialize(as = UserImpl.class)
public interface UserDTO {

  /**
   * Returns the unique identifier for the user.
   *
   * @return The user's ID.
   */
  int getIdUser();

  /**
   * Sets the unique identifier for the user.
   *
   * @param id The user's ID to set.
   */
  void setIdUser(int id);

  /**
   * Returns the user's version.
   *
   * @return The user's version
   */
  int getVersionUser();

  /**
   * Set the version of user.
   *
   * @param versionUser The user's version
   */
  void setVersionUser(int versionUser);

  /**
   * Returns the last name for the user.
   *
   * @return The user's last name.
   */
  String getLastNameUser();

  /**
   * Sets the last name for the user.
   *
   * @param lastName The user's last name to set.
   */
  void setLastNameUser(String lastName);

  /**
   * Returns the first name for the user.
   *
   * @return The user's first name.
   */
  String getFirstNameUser();

  /**
   * Sets the first name for the user.
   *
   * @param firstName The user's first name to set.
   */
  void setFirstNameUser(String firstName);

  /**
   * Returns the phone number for the user.
   *
   * @return The user's phone number.
   */
  String getPhoneNumberUser();

  /**
   * Sets the phone number for the user.
   *
   * @param phoneNumber The user's phone number to set.
   */
  void setPhoneNumberUser(String phoneNumber);

  /**
   * Returns the email for the user.
   *
   * @return The user's email.
   */
  String getEmailUser();

  /**
   * Sets the email for the user.
   *
   * @param email The user's email to set.
   */
  void setEmailUser(String email);

  /**
   * Returns the password for the user.
   *
   * @return The user's password.
   */
  String getPassword();

  /**
   * Sets the password for the user.
   *
   * @param password The user's password to set.
   */
  void setPassword(String password);

  /**
   * Returns the local date of inscription for the user.
   *
   * @return The user's local date of inscription.
   */
  Date getRegistrationDate();

  /**
   * Sets the local date of inscription for the user.
   *
   * @param registrationDate The user's local date of inscription to set.
   */
  void setRegistrationDate(Date registrationDate);

  /**
   * Returns the year academic for the user.
   *
   * @return The user's year academic.
   */
  SchoolYearDTO getSchoolYearUser();

  /**
   * Sets the year academic for the user.
   *
   * @param schoolYear The user's year academic to set.
   */
  void setSchoolYearUser(SchoolYearDTO schoolYear);

  /**
   * Returns the identifier of the school year.
   *
   * @return The identifier of the school year.
   */
  int getSchoolYearIdUser();

  /**
   * Sets the identifier of the school year.
   *
   * @param idSchoolYear The identifier of the school year to set.
   */
  void setSchoolYearIdUser(int idSchoolYear);


  /**
   * Returns the role for the user.
   *
   * @return The user's role.
   */
  Role getRole();

  /**
   * Sets the role for the user.
   *
   * @param role The user's role to set.
   */
  void setRole(Role role);

  /**
   * Returns the internship signature date for the user if he is a student.
   *
   * @return The student's internship signature date.
   */
  Date getInternshipSignatureDate();

  /**
   * Sets the internship signature date for the user if he is a student.
   *
   * @param internshipSignatureDate The user's internship signature date to set.
   */
  void setInternshipSignatureDate(Date internshipSignatureDate);

  /**
   * The {@code Role} enum represents different user roles within the system. It is used for various
   * purposes, including access control, authorization, and user-specific behavior.
   * -{@link #STUDENT}: Represents a student user. -{@link #TEACHER}: Represents a teacher or
   * instructor. -{@link #ADMINISTRATIVE}: Represents administrative staff.
   */
  enum Role {
    /**
     * Represents a student user.
     */
    STUDENT,

    /**
     * Represents a teacher.
     */
    TEACHER,

    /**
     * Represents administrative staff.
     */
    ADMINISTRATIVE;


    @Override
    public String toString() {
      return super.toString().toUpperCase();
    }
  }
}
