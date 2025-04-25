package be.vinci.pae.dal.dao;

import be.vinci.pae.business.dto.CompanyDTO;
import be.vinci.pae.dal.DALService;
import be.vinci.pae.utils.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * CompanyDAOImpl is an implementation of the CompanyDAO interface and provides methods to interact
 * with companies in the database.
 */
public class CompanyDAOImpl implements CompanyDAO {

  @Inject
  private DALService dalService;


  @Override
  public CompanyDTO getOneById(int id) {
    try (PreparedStatement ps = dalService.getPreparedStatement(
        "SELECT * FROM pae.companies c WHERE c.idCompany = ?;")) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return dalService.mapperRsToObject(rs, CompanyDTO.class, true);
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
  public CompanyDTO getOneByEmail(String email) {
    try (PreparedStatement ps = dalService.getPreparedStatement(
        "SELECT * FROM pae.companies c WHERE c.emailCompany = ?;")) {
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return dalService.mapperRsToObject(rs, CompanyDTO.class, true);
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
  public List<CompanyDTO> getAllCompanies() {
    try (PreparedStatement ps = dalService.getPreparedStatement(
        "SELECT * FROM pae.companies c")) {
      return getCompaniesDTO(ps);
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalService.releaseConnectionNoTx();
    }
  }

  @Override
  public List<CompanyDTO> getAllWithNumberInternshipWithoutYear() {
    try (PreparedStatement ps = dalService.getPreparedStatement(
        "SELECT c.*, COUNT(i.studentIdInternship) as internshipCount "
            + "FROM pae.companies c "
            + "LEFT JOIN pae.internships i ON c.idCompany = i.companyIdInternship "
            + "GROUP BY c.idCompany")) {
      return getCompaniesDTO(ps);
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalService.releaseConnectionNoTx();
    }
  }

  @Override
  public List<CompanyDTO> getAllWithNumberInternship(int yearId) {
    try (PreparedStatement ps = dalService.getPreparedStatement(
        "SELECT c.*, COUNT(i.studentIdInternship) as internshipCount "
            + "FROM pae.companies c "
            + "LEFT JOIN pae.internships i ON c.idCompany = i.companyIdInternship "
            + "AND i.schoolYearIdInternship = ? "
            + "GROUP BY c.idCompany")) {
      ps.setInt(1, yearId);
      return getCompaniesDTO(ps);
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalService.releaseConnectionNoTx();
    }
  }

  private List<CompanyDTO> getCompaniesDTO(PreparedStatement ps) {
    List<CompanyDTO> companies = new ArrayList<>();
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        CompanyDTO companyDTO = dalService.mapperRsToObject(rs, CompanyDTO.class, true);
        companies.add(companyDTO);
      }
      return companies;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public CompanyDTO insertCompany(CompanyDTO companyDTO) {
    try (PreparedStatement ps = dalService.getPreparedStatement(
        "INSERT INTO pae.companies(versionCompany, tradeName, designation, street, city, "
            + "postCode, boxNumber, phoneNumberCompany, emailCompany, blacklistMotivation,"
            + " isBlacklisted) VALUES (?,?,?,?,?,?,?,?,?,?,?) RETURNING *;"
    )) {
      ps.setInt(1, companyDTO.getVersionCompany());
      ps.setString(2, companyDTO.getTradeName());
      ps.setString(3, companyDTO.getDesignation());
      ps.setString(4, companyDTO.getStreet());
      ps.setString(5, companyDTO.getCity());
      ps.setString(6, companyDTO.getPostCode());
      ps.setString(7, companyDTO.getBoxNumber());
      ps.setString(8, companyDTO.getPhoneNumberCompany());
      ps.setString(9, companyDTO.getEmailCompany());
      ps.setString(10, companyDTO.getBlackListMotivation());
      ps.setBoolean(11, companyDTO.getIsBlacklisted());

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return dalService.mapperRsToObject(rs, CompanyDTO.class, false);
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
  public CompanyDTO getOneByTradeNameDesignation(String tradeName, String designation) {

    try (PreparedStatement ps = dalService.getPreparedStatement(
        "SELECT * FROM pae.companies c WHERE  c.tradename = ?"
            + (designation != null ? " AND c.designation = ?" : "")
            + ";")) {

      ps.setString(1, tradeName);
      if (designation != null) {
        ps.setString(2, designation);
      }

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          System.out.println(rs.getInt(1));
          return dalService.mapperRsToObject(rs, CompanyDTO.class, true);
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
  public CompanyDTO blacklistCompany(CompanyDTO companyDTO) {
    try (PreparedStatement ps = dalService.getPreparedStatement(
        "UPDATE pae.companies "
            + "SET isBlacklisted = true, blackListMotivation = ?, "
            + "versionCompany = versionCompany + 1 "
            + "WHERE idCompany = ? AND versionCompany = ? "
            + "RETURNING *;")) {

      ps.setString(1, companyDTO.getBlackListMotivation());
      ps.setInt(2, companyDTO.getIdCompany());
      ps.setInt(3, companyDTO.getVersionCompany());

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return dalService.mapperRsToObject(rs, CompanyDTO.class, false);
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
