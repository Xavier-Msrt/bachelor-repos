package be.vinci.pae.business.biz;

import be.vinci.pae.business.dto.CompanyDTO;

/**
 * The {@code Company} interface represents not only a data transfer object (DTO) for
 * company-related data and clever function. This interface is primarily used within the use case
 * controller (UCC).
 */
public interface Company extends CompanyDTO {

  /**
   * Set up the company with default value for create a new company.
   */
  void setupNewCompany();

}
