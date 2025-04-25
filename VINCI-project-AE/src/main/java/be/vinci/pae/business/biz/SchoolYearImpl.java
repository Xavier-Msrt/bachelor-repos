package be.vinci.pae.business.biz;

import be.vinci.pae.business.dto.SchoolYearDTO;

/**
 * Implementation of the SchoolYears interface representing school years information.
 */
public class SchoolYearImpl implements SchoolYearDTO {

  private int idSchoolYear;
  private int dateStart;
  private int dateEnd;

  @Override
  public int getIdSchoolYear() {
    return idSchoolYear;
  }

  @Override
  public void setIdSchoolYear(int idSchoolYear) {
    this.idSchoolYear = idSchoolYear;
  }

  @Override
  public int getDateStart() {
    return dateStart;
  }

  @Override
  public void setDateStart(int dateStart) {
    this.dateStart = dateStart;
  }

  @Override
  public int getDateEnd() {
    return dateEnd;
  }

  @Override
  public void setDateEnd(int dateEnd) {
    this.dateEnd = dateEnd;
  }
}
