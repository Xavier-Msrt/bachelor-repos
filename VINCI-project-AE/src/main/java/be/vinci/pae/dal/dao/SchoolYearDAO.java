package be.vinci.pae.dal.dao;

import be.vinci.pae.business.dto.SchoolYearDTO;
import java.util.List;

/**
 * The SchoolYearsDAO interface provides methods for interacting with school years data in the data
 * access layer. Implementations of this interface should handle the retrieval of school years
 * information from a data source.
 */
public interface SchoolYearDAO {

  /**
   * Retrieves a SchoolYearsDTO (Data Transfer Object) representing a school year based on the
   * specified start and end dates.
   *
   * @param dateStart The starting date of the school year.
   * @return A SchoolYearsDTO representing the school year within date range, or null if not found.
   */
  SchoolYearDTO getOneByStartingDate(int dateStart);

  /**
   * Retrieves all SchoolYearsDTO (Data Transfer Object) representing a school year.
   *
   * @return A list of SchoolYearsDTO representing the school year.
   */
  List<SchoolYearDTO> getAll();
}
