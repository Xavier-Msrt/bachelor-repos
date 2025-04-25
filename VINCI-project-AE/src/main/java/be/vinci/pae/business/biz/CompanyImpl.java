package be.vinci.pae.business.biz;

/**
 * The CompanyImpl class implements the Company interface. This class represents a company in the
 * system. It contains methods for managing the company's information.
 */
public class CompanyImpl implements Company {

  private int idCompany;
  private int versionCompany;
  private String tradeName;
  private String designation;
  private String street;
  private String city;
  private String postCode;
  private String boxNumber;
  private String phoneNumberCompany;
  private String emailCompany;
  private String blackListMotivation;
  private boolean isBlacklisted;
  private int internshipCount;

  @Override
  public int getIdCompany() {
    return idCompany;
  }

  @Override
  public void setIdCompany(int idCompany) {
    this.idCompany = idCompany;
  }

  @Override
  public int getVersionCompany() {
    return versionCompany;
  }

  @Override
  public void setVersionCompany(int versionCompany) {
    this.versionCompany = versionCompany;
  }

  @Override
  public String getTradeName() {
    return tradeName;
  }

  @Override
  public void setTradeName(String tradeName) {
    this.tradeName = tradeName;
  }

  @Override
  public String getDesignation() {
    return designation;
  }

  @Override
  public void setDesignation(String designation) {
    this.designation = designation;
  }

  @Override
  public String getStreet() {
    return street;
  }

  @Override
  public void setStreet(String street) {
    this.street = street;
  }

  @Override
  public String getCity() {
    return city;
  }

  @Override
  public void setCity(String city) {
    this.city = city;
  }

  @Override
  public String getPostCode() {
    return postCode;
  }

  @Override
  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }

  @Override
  public String getBoxNumber() {
    return boxNumber;
  }

  @Override
  public void setBoxNumber(String boxNumber) {
    this.boxNumber = boxNumber;
  }

  @Override
  public String getPhoneNumberCompany() {
    return phoneNumberCompany;
  }

  @Override
  public void setPhoneNumberCompany(String phoneNumber) {
    this.phoneNumberCompany = phoneNumber;
  }

  @Override
  public String getEmailCompany() {
    return emailCompany;
  }

  @Override
  public void setEmailCompany(String email) {
    this.emailCompany = email;
  }

  @Override
  public String getBlackListMotivation() {
    return blackListMotivation;
  }

  @Override
  public void setBlackListMotivation(String blackListMotivation) {
    this.blackListMotivation = blackListMotivation;
  }

  @Override
  public boolean getIsBlacklisted() {
    return isBlacklisted;
  }

  @Override
  public void setIsBlacklisted(boolean isBlacklisted) {
    this.isBlacklisted = isBlacklisted;
  }

  @Override
  public int getInternshipCount() {
    return internshipCount;
  }

  @Override
  public void setInternshipCount(int internshipCount) {
    this.internshipCount = internshipCount;
  }


  @Override
  public void setupNewCompany() {
    this.setIsBlacklisted(false);
    this.setVersionCompany(1);
  }

}
