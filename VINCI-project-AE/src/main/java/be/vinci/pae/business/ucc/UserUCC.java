package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.UserDTO;
import java.util.List;

/**
 * The UserUCC interface represents a user Use Case Controller (UCC) in the system. This class is
 * responsible for handling user-related operations.
 */
public interface UserUCC {

  /**
   * This login method Authenticates a user by verifying the provided email and password.
   *
   * @param email    the user email
   * @param password the user password
   * @return the UserDTO object representing the authenticated user, or null
   */
  UserDTO login(String email, String password);

  /**
   * Retrieves a user by the given id.
   *
   * @param id the id of the searched user
   * @return the UserDTO object representing the found user, or null
   */
  UserDTO getOneById(int id);

  /**
   * Retrieves all users from the database.
   *
   * @return a list of all users in the database.
   */
  List<UserDTO> getAllUsers();

  /**
   * Retrieves the total number of users.
   *
   * @return The total number of users.
   */
  int getUsersCount();

  /**
   * Retrieves the total number of users for a specific year.
   *
   * @param yearId The year for which the user count is to be retrieved.
   * @return The total number of users for the specified year.
   */
  int getUsersCountByYear(int yearId);

  /**
   * Registers a new user based on the provided UserDTO.
   *
   * @param userDTO The UserDTO containing user information.
   * @return A UserDTO representing the registered user if successful, or null if fails.
   */
  UserDTO register(UserDTO userDTO);

  /**
   * Updates a user based on the provided UserDTO.
   *
   * @param userDTOToUpdate The UserDTO containing new user information.
   * @return A UserDTO representing the updated user if successful, or null if fails.
   */
  UserDTO updateUserPhoneNumber(UserDTO userDTOToUpdate);

  /**
   * Updates a user based on the provided UserDTO.
   *
   * @param userDTO The UserDTO containing user id and hashed password.
   * @param newPwd  The new password of the user.
   * @param oldPwd  The old password of the user.
   * @return A UserDTO representing the updated user if successful, null if fails.
   */
  UserDTO updatePassword(UserDTO userDTO, String newPwd, String oldPwd);
}
