package be.vinci.pae.business.utils;

import be.vinci.pae.business.dto.UserDTO;
import com.fasterxml.jackson.databind.node.ObjectNode;


/**
 * The Utils interface represents a set of utility functions.
 */
public interface Utils {

  /**
   * Get the current school year starting date.
   *
   * @return the current school year
   */
  int currentSchoolYearStart();

  /**
   * Creates a JWT (JSON Web Token) for the specified user.
   *
   * @param userDTO The UserDTO object containing user information.
   * @return An ObjectNode representing a JSON object with the token and user information.
   */
  ObjectNode makeToken(UserDTO userDTO);

  /**
   * Validates the phone number of the specified user.
   *
   * @param phoneNumber The phone number to validate.
   */
  void phoneNumberValidation(String phoneNumber);
}
