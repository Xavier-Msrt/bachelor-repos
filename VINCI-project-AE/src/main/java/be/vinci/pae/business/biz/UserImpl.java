package be.vinci.pae.business.biz;

import be.vinci.pae.business.dto.SchoolYearDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Date;
import java.time.LocalDate;
import org.mindrot.jbcrypt.BCrypt;

/**
 * The UserImpl class implements the User interface. This class represents a user in the system. It
 * contains methods for managing the user's information.
 */
public class UserImpl implements User {

  private int idUser;
  private int versionUser;
  private String lastNameUser;
  private String firstNameUser;
  private String phoneNumberUser;
  private String emailUser;
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;
  private Date registrationDate;
  private int schoolYearIdUser;
  private SchoolYearDTO schoolYearUser;
  private Role role;
  private Date internshipSignatureDate;

  @Override
  public int getIdUser() {
    return idUser;
  }

  @Override
  public void setIdUser(int newIdUser) {
    this.idUser = newIdUser;
  }

  @Override
  public int getVersionUser() {
    return versionUser;
  }

  @Override
  public void setVersionUser(int versionUser) {
    this.versionUser = versionUser;
  }

  @Override
  public String getLastNameUser() {
    return lastNameUser;
  }

  @Override
  public void setLastNameUser(String lastName) {
    this.lastNameUser = lastName;
  }

  @Override
  public String getFirstNameUser() {
    return firstNameUser;
  }

  @Override
  public void setFirstNameUser(String firstName) {
    this.firstNameUser = firstName;
  }

  @Override
  public String getPhoneNumberUser() {
    return this.phoneNumberUser;
  }

  @Override
  public void setPhoneNumberUser(String phoneNumber) {
    this.phoneNumberUser = phoneNumber;
  }

  @Override
  public String getEmailUser() {
    return this.emailUser;
  }

  @Override
  public void setEmailUser(String email) {
    this.emailUser = email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public Date getRegistrationDate() {
    return this.registrationDate;
  }

  @Override
  public void setRegistrationDate(Date registrationDate) {
    this.registrationDate = registrationDate;
  }

  @Override
  public SchoolYearDTO getSchoolYearUser() {
    return schoolYearUser;
  }

  @Override
  public void setSchoolYearUser(SchoolYearDTO schoolYear) {
    this.schoolYearUser = schoolYear;
  }

  public int getSchoolYearIdUser() {
    return schoolYearIdUser;
  }

  public void setSchoolYearIdUser(int idSchoolYear) {
    this.schoolYearIdUser = idSchoolYear;
  }

  @Override
  public Role getRole() {
    return this.role;
  }

  @Override
  public void setRole(Role role) {
    this.role = role;
  }

  @Override
  public boolean checkPassword(String password) {
    return BCrypt.checkpw(password, this.password);
  }

  @Override
  public String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }

  @Override
  public Date getInternshipSignatureDate() {
    return internshipSignatureDate;
  }

  @Override
  public void setInternshipSignatureDate(Date internshipSignatureDate) {
    this.internshipSignatureDate = internshipSignatureDate;
  }

  @Override
  public boolean checkEmailRole() {
    Role r = this.getRole();

    if (r.toString().equalsIgnoreCase("STUDENT")
        && this.getEmailUser().matches("^[a-z]+\\.[a-z]+@student\\.vinci\\.be$")) {
      return true;
    }

    return (r.toString().equalsIgnoreCase("TEACHER") || r.toString()
        .equalsIgnoreCase("ADMINISTRATIVE"))
        && this.getEmailUser().matches("^[a-z]+\\.[a-z]+@vinci\\.be$");
  }

  @Override
  public void setupRegistrationDate() {
    LocalDate now = LocalDate.now();
    Date date = Date.valueOf(now);
    this.setRegistrationDate(date);
  }

}
