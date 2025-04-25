package be.vinci.pae.dal.dao;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.dal.DALService;
import be.vinci.pae.utils.exceptions.FatalException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * UserDAO provides methods to interact with users in the database.
 */
@Singleton
public class UserDAOImpl implements UserDAO {

  @Inject
  private DALService dalService;


  @Override
  public UserDTO getOneByEmail(String email) {
    PreparedStatement oneByEmail = null;
    try {
      oneByEmail = dalService.getPreparedStatement(
          "SELECT * "
              + "FROM pae.users u, pae.schoolYears aa  "
              + "WHERE u.schoolYearIdUser = aa.idSchoolYear AND u.emailUser = ? ");
      oneByEmail.setString(1, email);

      try (ResultSet rs = oneByEmail.executeQuery()) {
        if (rs.next()) {
          return dalService.mapperRsToObject(rs, UserDTO.class, true);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalService.releaseConnectionNoTx();
    }
    return null;
  }

  @Override
  public UserDTO getOneById(int id) {
    PreparedStatement oneById = null;
    try {
      oneById = dalService.getPreparedStatement(
          "SELECT * "
              + "FROM pae.users u, pae.schoolYears aa  "
              + "WHERE u.schoolYearIdUser = aa.idSchoolYear AND u.idUser = ? ");
      oneById.setInt(1, id);

      try (ResultSet rs = oneById.executeQuery()) {
        if (rs.next()) {
          return dalService.mapperRsToObject(rs, UserDTO.class, true);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalService.releaseConnectionNoTx();
    }
    return null;
  }

  @Override
  public List<UserDTO> getAllUsers() {
    try (PreparedStatement ps = dalService.getPreparedStatement(
        "SELECT *, i.signatureDate AS internshipSignatureDate "
            + "FROM pae.users u "
            + "LEFT OUTER JOIN pae.schoolYears s "
            + "ON u.schoolYearIdUser = s.idSchoolYear "
            + "LEFT OUTER JOIN pae.internships i "
            + "ON u.idUser = i.studentIdInternship;")) {

      try (ResultSet rs = ps.executeQuery()) {
        List<UserDTO> users = new ArrayList<>();
        while (rs.next()) {
          UserDTO userDTO = dalService.mapperRsToObject(rs, UserDTO.class, true);
          users.add(userDTO);
        }
        return users;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalService.releaseConnectionNoTx();
    }
  }

  @Override
  public int getUsersCount() {
    try (PreparedStatement ps = dalService.getPreparedStatement(
        "SELECT COUNT(*) FROM pae.users WHERE role = 'STUDENT';")) {

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getInt(1);
        }
        return 0;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalService.releaseConnectionNoTx();
    }
  }

  @Override
  public int getUsersCountByYear(int yearId) {
    try (PreparedStatement ps = dalService.getPreparedStatement(
        "SELECT COUNT(*) FROM pae.users WHERE schoolYearIdUser = ? AND role = 'STUDENT';")) {

      ps.setInt(1, yearId);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getInt(1);
        }
        return 0;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalService.releaseConnectionNoTx();
    }
  }

  @Override
  public UserDTO insertUser(UserDTO userDTO) {
    PreparedStatement ps = null;
    try {
      ps = dalService.getPreparedStatement(
          "INSERT INTO pae.users"
              + "(lastNameUser, firstNameUser, phoneNumberUser, emailUser, password,"
              + " registrationDate, schoolYearIdUser, role, versionUser) "
              + "VALUES (?,?,?,?,?,?,?,?,?) "
              + "RETURNING *;");

      ps.setString(1, userDTO.getLastNameUser());
      ps.setString(2, userDTO.getFirstNameUser());
      ps.setString(3, userDTO.getPhoneNumberUser());
      ps.setString(4, userDTO.getEmailUser());
      ps.setString(5, userDTO.getPassword());
      ps.setDate(6, userDTO.getRegistrationDate()); // java.utils.Date = java.sql.Date
      ps.setInt(7, userDTO.getSchoolYearIdUser());
      ps.setString(8, userDTO.getRole().toString());
      ps.setInt(9, userDTO.getVersionUser());

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return dalService.mapperRsToObject(rs, UserDTO.class, false);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalService.releaseConnectionNoTx();
    }
    return null;
  }

  @Override
  public UserDTO updateUserPhoneNumber(UserDTO userDTO) {
    PreparedStatement ps = null;
    try {
      ps = dalService.getPreparedStatement(
          "UPDATE pae.users "
              + "SET phoneNumberUser = ?, versionUser = versionUser + 1 "
              + "WHERE idUser = ? AND versionUser = ? "
              + "RETURNING *;");

      ps.setString(1, userDTO.getPhoneNumberUser());
      ps.setInt(2, userDTO.getIdUser());
      ps.setInt(3, userDTO.getVersionUser());

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return dalService.mapperRsToObject(rs, UserDTO.class, false);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalService.releaseConnectionNoTx();
    }
    return null;
  }

  @Override
  public UserDTO updateUserPassword(UserDTO userDTO) {
    PreparedStatement ps = null;
    try {
      ps = dalService.getPreparedStatement(
          "UPDATE pae.users "
              + "SET password = ? "
              + "WHERE idUser = ? "
              + "RETURNING *;");

      ps.setString(1, userDTO.getPassword());
      ps.setInt(2, userDTO.getIdUser());

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return dalService.mapperRsToObject(rs, UserDTO.class, false);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalService.releaseConnectionNoTx();
    }
    return null;
  }

}
