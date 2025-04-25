package be.vinci.pae.dal.dao;

import be.vinci.pae.business.dto.InternshipDTO;
import be.vinci.pae.dal.DALService;
import be.vinci.pae.utils.exceptions.FatalException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * InternshipDAOImpl class provides methods to interact with Internship in the database.
 */
@Singleton
public class InternshipDAOImpl implements InternshipDAO {

  @Inject
  private DALService dalService;

  @Override
  public InternshipDTO getOneByStudentId(int idStudent) {
    try (
        PreparedStatement oneByStudentId = dalService.getPreparedStatement(
            " SELECT * FROM pae.internships i, pae.users u, pae.companies cp, "
                + "pae.contacts ct , pae.schoolYears sy, pae.internshipSupervisors sup "
                + " WHERE i.studentIdInternship = u.idUser "
                + "  AND ct.companyIdContact = i.companyIdInternship "
                + "  AND ct.studentIdContact = i.studentIdInternship "
                + "  AND i.internSupervisorId = sup.idSupervisor "
                + "  AND ct.schoolYearIdContact = i.schoolYearIdInternship "
                + "  AND ct.companyIdContact = cp.idCompany AND ct.studentIdContact = u.idUser "
                + "  AND ct.schoolYearIdContact = sy.idSchoolYear AND u.idUser = ?;"
        )) {
      oneByStudentId.setInt(1, idStudent);
      try (ResultSet rs = oneByStudentId.executeQuery()) {
        if (rs.next()) {
          return dalService.mapperRsToObject(rs, InternshipDTO.class, true);
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
  public int getInternshipsCount() {
    try (PreparedStatement ps = dalService.getPreparedStatement(
        "SELECT COUNT(*) FROM pae.internships;")) {


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
  public int getInternshipsCountByYear(int yearId) {
    try (PreparedStatement ps = dalService.getPreparedStatement(
        "SELECT COUNT(*) FROM pae.internships WHERE schoolYearIdInternship = ?;")) {

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
  public InternshipDTO createOneInternship(InternshipDTO internshipDTO) {
    try (
        PreparedStatement createOne = dalService.getPreparedStatement(
            "INSERT INTO pae.internships "
                + "(versionInternship, studentIdInternship, companyIdInternship,"
                + " schoolYearIdInternship, internSupervisorId, signatureDate, "
                + "internShipProject) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)"
                + "RETURNING *;"
        )) {
      createOne.setInt(1, 1);
      createOne.setInt(2, internshipDTO.getContactInternship().getStudentIdContact());
      createOne.setInt(3, internshipDTO.getContactInternship().getCompanyIdContact());
      createOne.setInt(4, internshipDTO.getContactInternship().getSchoolYearIdContact());
      createOne.setInt(5, internshipDTO.getInternSupervisorId());
      createOne.setDate(6, internshipDTO.getSignatureDate());
      createOne.setString(7, internshipDTO.getInternShipProject());
      try (ResultSet rs = createOne.executeQuery()) {
        if (rs.next()) {
          return dalService.mapperRsToObject(rs, InternshipDTO.class, false);
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
  public InternshipDTO updateInternShipProject(InternshipDTO internshipDTOToUpdate) {

    try (PreparedStatement oneWithUpdate = dalService.getPreparedStatement(
        "UPDATE pae.internships i "
            + "SET internShipProject = ?, versionInternship = versionInternship + 1 "
            + "WHERE i.idInternship = ? AND i.studentIdInternship = ? AND i.versionInternship = ? "
            + "RETURNING * ;")) {

      oneWithUpdate.setString(1, internshipDTOToUpdate.getInternShipProject());
      oneWithUpdate.setInt(2, internshipDTOToUpdate.getIdInternship());
      oneWithUpdate.setInt(3, internshipDTOToUpdate.getStudentIdInternship());
      oneWithUpdate.setInt(4, internshipDTOToUpdate.getVersionInternship());
      try (ResultSet rs = oneWithUpdate.executeQuery()) {
        if (rs.next()) {
          return dalService.mapperRsToObject(rs, InternshipDTO.class, false);
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
