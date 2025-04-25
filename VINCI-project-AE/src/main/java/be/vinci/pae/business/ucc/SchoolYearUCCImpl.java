package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.SchoolYearDTO;
import be.vinci.pae.dal.dao.SchoolYearDAO;
import jakarta.inject.Inject;
import java.util.List;

/**
 * This class is an implementation of the InternshipUCC interface and represents an internship Use
 * Case Controller (UCC) in the system. This interface is responsible for handling
 * internship-related operations.
 */
public class SchoolYearUCCImpl implements SchoolYearUCC {

  @Inject
  private SchoolYearDAO schoolYearDAO;

  @Override
  public List<SchoolYearDTO> getAll() {
    return schoolYearDAO.getAll();
  }

}
