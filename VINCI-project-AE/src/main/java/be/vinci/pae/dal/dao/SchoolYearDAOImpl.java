package be.vinci.pae.dal.dao;

import be.vinci.pae.business.dto.SchoolYearDTO;
import be.vinci.pae.dal.DALService;
import be.vinci.pae.utils.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * SchoolYearsDAOImpl provides methods to interact with SchoolYears in the database.
 */
public class SchoolYearDAOImpl implements SchoolYearDAO {

  @Inject
  private DALService dalService;


  @Override
  public SchoolYearDTO getOneByStartingDate(int dateStart) {
    try (PreparedStatement OneByDateStartEnd = dalService.getPreparedStatement(
        "SELECT * "
            + "FROM pae.schoolyears sy "
            + "WHERE  sy.datestart = ? AND sy.dateend = ?;")) {

      OneByDateStartEnd.setInt(1, dateStart);
      OneByDateStartEnd.setInt(2, dateStart + 1);

      try (ResultSet rs = OneByDateStartEnd.executeQuery()) {
        if (rs.next()) {
          return dalService.mapperRsToObject(rs, SchoolYearDTO.class, true);
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
  public List<SchoolYearDTO> getAll() {
    try (PreparedStatement ps = dalService.getPreparedStatement("SELECT * "
        + "FROM pae.schoolyears;")) {
      try (ResultSet rs = ps.executeQuery()) {
        List<SchoolYearDTO> schoolYears = new ArrayList<>();
        while (rs.next()) {
          SchoolYearDTO schoolYearDTO = dalService.mapperRsToObject(rs, SchoolYearDTO.class,
              false);
          schoolYears.add(schoolYearDTO);
        }
        return schoolYears;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalService.releaseConnectionNoTx();
    }
  }
}
