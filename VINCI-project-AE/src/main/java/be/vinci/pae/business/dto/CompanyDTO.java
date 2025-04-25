package be.vinci.pae.business.dto;

import be.vinci.pae.business.biz.CompanyImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * This interface represents a data transfer object (DTO) for company-related data. It encapsulates
 * attributes contact relevant information. This interface is primarily used within the user
 * interface layer (IHM) and the data access layer (DAL).
 */
@JsonDeserialize(as = CompanyImpl.class)
public interface CompanyDTO {

  /**
   * Gets the company ID.
   *
   * @return the company ID.
   */
  int getIdCompany();

  /**
   * Sets the company ID.
   *
   * @param id the company ID.
   */
  void setIdCompany(int id);


  /**
   * Gets the company's version.
   *
   * @return the company's version.
   */
  int getVersionCompany();

  /**
   * Sets the company version.
   *
   * @param versionCompany The company's version.
   */
  void setVersionCompany(int versionCompany);

  /**
   * Gets the company trade name.
   *
   * @return the company trade name.
   */
  String getTradeName();

  /**
   * Sets the company trade name.
   *
   * @param tradeName the company trade name.
   */
  void setTradeName(String tradeName);

  /**
   * Gets the company designation.
   *
   * @return the company designation.
   */
  String getDesignation();

  /**
   * Sets the company designation.
   *
   * @param designation the company designation.
   */
  void setDesignation(String designation);

  /**
   * Gets the company street.
   *
   * @return the company street.
   */
  String getStreet();

  /**
   * Sets the company street.
   *
   * @param street the company street.
   */
  void setStreet(String street);

  /**
   * Gets the company city.
   *
   * @return the company city.
   */
  String getCity();

  /**
   * Sets the company city.
   *
   * @param city the company city.
   */
  void setCity(String city);

  /**
   * Gets the company postal code.
   *
   * @return the company postal code.
   */
  String getPostCode();

  /**
   * Sets the company postal code.
   *
   * @param postCode the company postal code.
   */
  void setPostCode(String postCode);

  /**
   * Gets the company box number.
   *
   * @return the company box number.
   */
  String getBoxNumber();

  /**
   * Sets the company box number.
   *
   * @param boxNumber the company box number.
   */
  void setBoxNumber(String boxNumber);

  /**
   * Gets the company phone number.
   *
   * @return the company phone number.
   */
  String getPhoneNumberCompany();

  /**
   * Sets the company phone number.
   *
   * @param phoneNumber the company phone number.
   */
  void setPhoneNumberCompany(String phoneNumber);

  /**
   * Gets the company email.
   *
   * @return the company email.
   */
  String getEmailCompany();

  /**
   * Sets the company email.
   *
   * @param email the company email.
   */
  void setEmailCompany(String email);

  /**
   * Gets the company blackList motivation.
   *
   * @return the company blackList motivation.
   */
  String getBlackListMotivation();

  /**
   * Sets the company blackList motivation.
   *
   * @param blackListMotivation the company blackList motivation.
   */
  void setBlackListMotivation(String blackListMotivation);

  /**
   * Gets the company blackList state.
   *
   * @return the company blackList state.
   */
  boolean getIsBlacklisted();

  /**
   * Sets the company blackList state.
   *
   * @param isBlacklisted the company blackList state.
   */
  void setIsBlacklisted(boolean isBlacklisted);

  /**
   * Gets the internship count.
   *
   * @return the internship count.
   */
  int getInternshipCount();

  /**
   * Sets the internship count.
   *
   * @param internshipCount the internship count.
   */
  void setInternshipCount(int internshipCount);
}
