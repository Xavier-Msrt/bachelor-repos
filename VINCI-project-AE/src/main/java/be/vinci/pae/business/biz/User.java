package be.vinci.pae.business.biz;

import be.vinci.pae.business.dto.UserDTO;

/**
 * The {@code User} interface represents not only a data transfer object (DTO) for user-related data
 * and clever function. This interface is primarily used within the use case controller (UCC).
 */
public interface User extends UserDTO {

  /**
   * Returns boolean.
   *
   * @param password The user's hashing password to check.
   * @return true if password is checked or false.
   */
  boolean checkPassword(String password);

  /**
   * Returns the hashing password for the user.
   *
   * @param password The user's password to hash.
   * @return user password hashing in parameter.
   */
  String hashPassword(String password);

  /**
   * Check if the user's email match with the role of the user.
   *
   * @return true if email match with the role of the user or false.
   */
  boolean checkEmailRole();

  /**
   * Set the registration date of the user.
   */
  void setupRegistrationDate();
}
