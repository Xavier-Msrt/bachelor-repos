package be.vinci.pae.business.dto;

import be.vinci.pae.business.biz.SchoolYearImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * This interface representing School Years Data Transfer Object (DTO). This interface defines the
 * structure of data related to school years.
 */
@JsonDeserialize(as = SchoolYearImpl.class)
public interface SchoolYearDTO {

  /**
   * Gets the unique identifier for school years.
   *
   * @return The identifier for school years.
   */
  int getIdSchoolYear();

  /**
   * Sets the unique identifier for school years.
   *
   * @param idSchoolYears The new identifier for school years.
   */
  void setIdSchoolYear(int idSchoolYears);

  /**
   * Gets the start date of the school years.
   *
   * @return The start date of the school years.
   */
  int getDateStart();

  /**
   * Sets the start date of the school years.
   *
   * @param dateStart The new start date of the school years.
   */
  void setDateStart(int dateStart);

  /**
   * Gets the end date of the school years.
   *
   * @return The end date of the school years.
   */
  int getDateEnd();

  /**
   * Sets the end date of the school years.
   *
   * @param dateEnd The new end date of the school years.
   */
  void setDateEnd(int dateEnd);
}
