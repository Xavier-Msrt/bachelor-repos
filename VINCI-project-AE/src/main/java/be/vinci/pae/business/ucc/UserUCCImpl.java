package be.vinci.pae.business.ucc;

import be.vinci.pae.business.biz.User;
import be.vinci.pae.business.dto.SchoolYearDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.utils.Utils;
import be.vinci.pae.dal.DALBizService;
import be.vinci.pae.dal.dao.SchoolYearDAO;
import be.vinci.pae.dal.dao.UserDAO;
import be.vinci.pae.utils.exceptions.ConflictException;
import be.vinci.pae.utils.exceptions.ContractException;
import be.vinci.pae.utils.exceptions.NotFoundException;
import jakarta.inject.Inject;
import java.util.List;

/**
 * The UserUCC class is an implementation of the UserUCC interface and represents a user Use Case
 * Controller (UCC) in the system. This interface is responsible for handling user-related
 * operations.
 */
public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO userDAO;

  @Inject
  private DALBizService dalBizServices;

  @Inject
  private SchoolYearDAO schoolYearDAO;

  @Inject
  private Utils utils;

  @Override
  public UserDTO login(String email, String password) {

    UserDTO userFromDb = userDAO.getOneByEmail(email.toLowerCase());

    // check if user exist with this email
    if (userFromDb == null) {
      throw new NotFoundException("INCORRECT_EMAIL_PWD");
    }

    User userBiz = (User) userFromDb;

    // check user password
    if (!userBiz.checkPassword(password)) {
      throw new ContractException("INCORRECT_EMAIL_PWD");
    }

    return userFromDb;
  }

  @Override
  public UserDTO getOneById(int id) {
    return userDAO.getOneById(id);
  }

  @Override
  public List<UserDTO> getAllUsers() {
    return userDAO.getAllUsers();
  }

  @Override
  public int getUsersCount() {
    return userDAO.getUsersCount();
  }

  @Override
  public int getUsersCountByYear(int yearId) {
    return userDAO.getUsersCountByYear(yearId);
  }

  @Override
  public UserDTO register(UserDTO userDTO) {

    User user = (User) userDTO;

    // check biz email with good role
    if (!user.checkEmailRole()) {
      throw new ContractException("INVALID_ROLE_EMAIL");
    }

    // register date
    user.setupRegistrationDate();

    // hash password
    userDTO.setPassword(user.hashPassword(userDTO.getPassword()));

    UserDTO u = null;
    try {
      // check if user not already exist
      dalBizServices.startTransaction();
      if (userDAO.getOneByEmail(userDTO.getEmailUser()) != null) {
        throw new ContractException("EMAIL_USED");
      }

      //determineSchoolYear
      int startYear = utils.currentSchoolYearStart();

      SchoolYearDTO schoolYearDTO = schoolYearDAO.getOneByStartingDate(startYear);
      if (schoolYearDTO == null) {
        throw new NotFoundException();
      }

      userDTO.setSchoolYearIdUser(schoolYearDTO.getIdSchoolYear());
      userDTO.setVersionUser(1);

      u = userDAO.insertUser(userDTO);

      dalBizServices.commitTransaction();
    } catch (Exception e) {
      dalBizServices.rollbackTransaction();
      throw e;
    }
    return u;
  }

  @Override
  public UserDTO updateUserPhoneNumber(UserDTO userDTOToUpdate) {

    UserDTO u = null;

    try {
      // check if user not already exist
      dalBizServices.startTransaction();
      if (userDAO.getOneById(userDTOToUpdate.getIdUser()).getPhoneNumberUser()
          .equals(userDTOToUpdate.getPhoneNumberUser())) {
        throw new ContractException("INCORRECT_VALUE");
      }

      u = userDAO.updateUserPhoneNumber(userDTOToUpdate);

      if (u == null) {
        throw new ConflictException("CONFLICT_ERROR");
      }

      dalBizServices.commitTransaction();
    } catch (Exception e) {
      dalBizServices.rollbackTransaction();
      throw e;
    }
    return u;
  }

  @Override
  public UserDTO updatePassword(UserDTO userDTO, String newPassword, String oldPassword) {
    UserDTO u = null;

    User user = (User) getOneById(userDTO.getIdUser());

    if (!user.checkPassword(oldPassword)) {
      throw new ContractException("INCORRECT_EMAIL_PWD");
    }

    try {
      // check if user not already exist
      dalBizServices.startTransaction();

      String hashedPwd = user.hashPassword(newPassword);
      userDTO.setPassword(hashedPwd);
      u = userDAO.updateUserPassword(userDTO);

      dalBizServices.commitTransaction();

    } catch (Exception e) {
      dalBizServices.rollbackTransaction();
      throw e;
    }
    return u;
  }
}
