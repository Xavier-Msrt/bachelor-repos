package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.SchoolYearDTO;
import java.util.List;

/**
 * This interface represents a schoolyears Use Case Controller (UCC) in the system. This class is
 * responsible for handling schoolyears-related operations.
 */
public interface SchoolYearUCC {

  /**
   * This method is responsible for getting a schoolyears.
   *
   * @return a list of SchoolYearsDTO
   */
  List<SchoolYearDTO> getAll();
}
