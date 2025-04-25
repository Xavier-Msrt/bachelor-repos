package be.vinci.pae.dal.dao;

import be.vinci.pae.business.dto.UserDTO;
import java.util.List;

/**
 * UserDAO provides methods to interact with users in the database.
 */
public interface UserDAO {

  /**
   * Get UserDTO By giving an email.
   *
   * @param email the user email
   * @return a complete UserDTO if found in db, or null
   */
  UserDTO getOneByEmail(String email);

  /**
   * Get UserDTO By giving an id.
   *
   * @param id the user id
   * @return a complete UserDTO if found in db, or null
   */
  UserDTO getOneById(int id);

  /**
   * Get all users from the database.
   *
   * @return a list of all users in the database.
   */
  List<UserDTO> getAllUsers();

  /**
   * Get the total number of users.
   *
   * @return The total number of users.
   */
  int getUsersCount();

  /**
   * Get the total number of users for a specific year.
   *
   * @param yearId The year for which the user count is to be retrieved.
   * @return The total number of users for the specified year.
   */
  int getUsersCountByYear(int yearId);

  /**
   * Inserts a new user into the database, based on the provided UserDTO.
   *
   * @param userDTO The UserDTO containing user information.
   * @return The UserDTO getUsersCountByYear has been inserted in db.
   */
  UserDTO insertUser(UserDTO userDTO);

  /**
   * Update provided user phone number in the database, based on the provided UserDTO.
   *
   * @param userDTOToUpdate The UserDTO containing user's phone number.
   * @return The UserDTO which has been updated in db.
   */
  UserDTO updateUserPhoneNumber(UserDTO userDTOToUpdate);

  /**
   * Update provided user phone number in the database, based on the provided UserDTO.
   *
   * @param dto The UserDTO containing user's password.
   * @return The UserDTO which has been updated in db.
   */
  UserDTO updateUserPassword(UserDTO dto);
}
