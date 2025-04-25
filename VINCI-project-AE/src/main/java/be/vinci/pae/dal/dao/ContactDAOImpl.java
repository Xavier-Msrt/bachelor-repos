package be.vinci.pae.dal.dao;

import be.vinci.pae.business.dto.CompanyDTO;
import be.vinci.pae.business.dto.ContactDTO;
import be.vinci.pae.business.dto.ContactDTO.State;
import be.vinci.pae.dal.DALService;
import be.vinci.pae.utils.exceptions.FatalException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Contact DAO provides methods to interact with contacts in the database.
 */
@Singleton
public class ContactDAOImpl implements ContactDAO {

  @Inject
  private DALService dalService;

  @Override
  public ContactDTO getOneWithContactID(int idStudent, int idCompany) {
    try (
        PreparedStatement oneWithContactID = dalService.getPreparedStatement(
            "SELECT * FROM pae.contacts ct, pae.users u, pae.companies cp, pae.schoolYears aa "
                + "   WHERE ct.studentIdContact = u.idUser AND ct.companyIdContact = cp.idCompany "
                + "   AND ct.schoolYearIdContact = aa.idSchoolYear AND u.idUser = ? "
                + "   AND cp.idCompany = ?")) {
      oneWithContactID.setInt(1, idStudent);
      oneWithContactID.setInt(2, idCompany);
      try (ResultSet rs = oneWithContactID.executeQuery()) {
        if (rs.next()) {
          return dalService.mapperRsToObject(rs, ContactDTO.class, true);
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
  public ContactDTO updateStateWithContactID(ContactDTO contactDTOToUpdate) {

    try (
        PreparedStatement oneWithUpdate = dalService.getPreparedStatement(
            "UPDATE pae.contacts c "
                + "SET state = ? , meetingPlace = ? , reasonRefusal = ? , "
                + "versionContact = versionContact + 1 "
                + "WHERE c.studentIdContact = ? AND c.companyIdContact = ? "
                + "AND c.versionContact = ? "
                + "RETURNING * ;")) {

      oneWithUpdate.setString(1, contactDTOToUpdate.getState().toString());
      if (contactDTOToUpdate.getMeetingPlace() == null) {
        oneWithUpdate.setNull(2, Types.VARCHAR);
      } else {
        oneWithUpdate.setString(2, contactDTOToUpdate.getMeetingPlace().toString());
      }
      oneWithUpdate.setString(3, contactDTOToUpdate.getReasonRefusal());
      oneWithUpdate.setInt(4, contactDTOToUpdate.getStudentIdContact());
      oneWithUpdate.setInt(5, contactDTOToUpdate.getCompanyIdContact());
      oneWithUpdate.setInt(6, contactDTOToUpdate.getVersionContact());
      try (ResultSet rs = oneWithUpdate.executeQuery()) {
        if (rs.next()) {
          return dalService.mapperRsToObject(rs, ContactDTO.class, false);
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
  public List<ContactDTO> getAllFromId(int id) {
    try (PreparedStatement ps = dalService.getPreparedStatement(
        "SELECT *"
            + " FROM pae.contacts ct, pae.users u, pae.companies cp, pae.schoolYears aa "
            + " WHERE ct.studentIdContact = u.idUser AND ct.companyIdContact = cp.idCompany "
            + " AND ct.schoolYearIdContact = aa.idSchoolYear AND u.idUser = ?;")) {

      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        List<ContactDTO> contacts = new ArrayList<>();
        while (rs.next()) {
          //Create the company
          contacts.add(dalService.mapperRsToObject(rs, ContactDTO.class, true));
        }
        return contacts;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalService.releaseConnectionNoTx();
    }
  }

  @Override
  public ContactDTO addNewContact(ContactDTO contactDTO) {
    try (PreparedStatement ps = dalService.getPreparedStatement(
        "INSERT INTO pae.contacts "
            + "(versionContact, studentIdContact, companyIdContact, schoolYearIdContact, state)"
            + " VALUES (?, ?, ?, ?, ?) "
            + "RETURNING *")
    ) {
      ps.setInt(1, contactDTO.getVersionContact());
      ps.setInt(2, contactDTO.getStudentIdContact());
      ps.setInt(3, contactDTO.getCompanyIdContact());
      ps.setInt(4, contactDTO.getSchoolYearIdContact());
      ps.setString(5, contactDTO.getState().toString());
      try (ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
          return dalService.mapperRsToObject(rs, ContactDTO.class, false);
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
  public List<ContactDTO> getAllContactByComapnyId(int companyId) {
    try (PreparedStatement ps = dalService.getPreparedStatement(
        "SELECT * "
            + "FROM pae.contacts ct, pae.users u, pae.companies cp, pae.schoolYears aa "
            + "WHERE ct.studentIdContact = u.idUser AND ct.companyIdContact = cp.idCompany "
            + "AND ct.schoolYearIdContact = aa.idSchoolYear AND cp.idcompany = ?; ")) {

      ps.setInt(1, companyId);
      try (ResultSet rs = ps.executeQuery()) {
        List<ContactDTO> contacts = new ArrayList<>();
        while (rs.next()) {
          contacts.add(dalService.mapperRsToObject(rs, ContactDTO.class, true));
        }
        return contacts;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalService.releaseConnectionNoTx();
    }
  }

  @Override
  public void updateStateOnHold(ContactDTO contactDTO) {

    try (
        PreparedStatement oneWithUpdate = dalService.getPreparedStatement(
            "UPDATE pae.contacts c "
                + "SET state = ?  , "
                + "versionContact = versionContact + 1 "
                + "WHERE c.studentIdContact = ? AND c.companyIdContact != ? "
                + "AND (c.state = 'ADMITTED' OR c.state = 'STARTED');")) {

      oneWithUpdate.setString(1, State.ON_HOLD.toString());
      oneWithUpdate.setInt(2, contactDTO.getStudentIdContact());
      oneWithUpdate.setInt(3, contactDTO.getCompanyIdContact());
      oneWithUpdate.executeUpdate();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalService.releaseConnectionNoTx();
    }
  }

  @Override
  public void updateStateBlacklistFromCompany(CompanyDTO companyDTO) {

    try (
        PreparedStatement ps = dalService.getPreparedStatement(
            "UPDATE pae.contacts c "
                + "SET state = ?  , "
                + "versionContact = versionContact + 1 "
                + "WHERE c.companyIdContact = ? "
                + "AND (c.state = 'STARTED' OR c.state = 'ADMITTED');")) {

      ps.setString(1, State.BLACKLISTED.toString());
      ps.setInt(2, companyDTO.getIdCompany());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalService.releaseConnectionNoTx();
    }
  }
}
