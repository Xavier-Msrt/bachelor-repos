package be.vinci.pae.dal.dao;

import be.vinci.pae.business.dto.InternshipSupervisorDTO;
import be.vinci.pae.dal.DALService;
import be.vinci.pae.utils.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * InternshipSupervisorDAOImpl class provides methods to interact with InternshipSupervisor in the
 * database.
 */
public class InternshipSupervisorDAOImpl implements InternshipSupervisorDAO {

  @Inject
  private DALService dalService;

  @Override
  public InternshipSupervisorDTO insertInternshipSupervisor(InternshipSupervisorDTO supervisor) {
    try (PreparedStatement ps = dalService.getPreparedStatement(
        "INSERT INTO pae.internshipsupervisors "
            + "(versionsupervisor, lastnamesupervisor, firstnamesupervisor,"
            + " phonenumbersupervisor, emailsupervisor, companyidsupervisor) "
            + "VALUES (?,?,?,?,?,?) RETURNING *;"
    )) {
      ps.setInt(1, supervisor.getVersionSupervisor());
      ps.setString(2, supervisor.getLastNameSupervisor());
      ps.setString(3, supervisor.getFirstNameSupervisor());
      ps.setString(4, supervisor.getPhoneNumberSupervisor());
      ps.setString(5, supervisor.getEmailSupervisor());
      ps.setInt(6, supervisor.getCompanyIdSupervisor());

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return dalService.mapperRsToObject(rs, InternshipSupervisorDTO.class, false);
        }
      }

    } catch (SQLException e) {
      throw new FatalException();
    } finally {
      dalService.releaseConnectionNoTx();
    }
    return null;
  }

  @Override
  public InternshipSupervisorDTO getOneISWithID(int internshipSupervisorID) {
    try (PreparedStatement getOnIS = dalService.getPreparedStatement(
        "SELECT * "
            + "FROM pae.internshipSupervisors intSup , pae.companies c "
            + "WHERE intSup.companyIdSupervisor = c.idCompany"
            + "  AND intSup.idSupervisor = ? ;")) {

      getOnIS.setInt(1, internshipSupervisorID);
      try (ResultSet rs = getOnIS.executeQuery()) {
        if (rs.next()) {
          return dalService.mapperRsToObject(rs, InternshipSupervisorDTO.class, true);
        }
      }
    } catch (Exception e) {
      throw new FatalException(e);
    } finally {
      dalService.releaseConnectionNoTx();
    }
    return null;
  }


  @Override
  public InternshipSupervisorDTO getOneInternshipSupervisorByEmail(String email) {
    try (PreparedStatement ps = dalService.getPreparedStatement(
        "SELECT * "
            + "FROM pae.internshipsupervisors i, pae.companies p "
            + "WHERE i.companyidsupervisor = p.idcompany "
            + "  AND i.emailsupervisor = ?;"
    )) {
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return dalService.mapperRsToObject(rs, InternshipSupervisorDTO.class, true);
        }
      }

    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return null;
  }

  @Override
  public List<InternshipSupervisorDTO> getAllISWithCompanyID(int idCompany) {
    try (PreparedStatement getAllIS = dalService.getPreparedStatement(
        "SELECT * "
            + "FROM pae.internshipSupervisors intSup , pae.companies c "
            + "WHERE intSup.companyIdSupervisor = c.idCompany "
            + "AND intSup.companyIdSupervisor = ? ;")) {
      getAllIS.setInt(1, idCompany);
      try (ResultSet rs = getAllIS.executeQuery()) {
        List<InternshipSupervisorDTO> supervisorsDTOList = new ArrayList<>();
        while (rs.next()) {
          supervisorsDTOList.add(
              dalService.mapperRsToObject(rs, InternshipSupervisorDTO.class, true));
        }
        return supervisorsDTOList;
      }
    } catch (Exception e) {
      throw new FatalException(e);
    } finally {
      dalService.releaseConnectionNoTx();
    }
  }
}
