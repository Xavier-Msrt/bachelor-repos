package be.vinci.pae.dal.dao;

import be.vinci.pae.business.dto.CompanyDTO;
import java.util.List;

/**
 * CompanyDAO provides methods to interact with companies in the database.
 */
public interface CompanyDAO {

  /**
   * Get one company by its id.
   *
   * @param id the id of the company
   * @return The CompanyDTO  which has been get in db.
   */
  CompanyDTO getOneById(int id);

  /**
   * Get one company by its email.
   *
   * @param email the email of the company
   * @return The CompanyDTO  which has been get in db.
   */
  CompanyDTO getOneByEmail(String email);

  /**
   * Get a List of CompanyDTO.
   *
   * @return a list of CompanyDTO.
   */
  List<CompanyDTO> getAllCompanies();

  /**
   * Get a List of CompanyDTO with the number of internship for all years.
   *
   * @return a list of CompanyDTO.
   */
  List<CompanyDTO> getAllWithNumberInternshipWithoutYear();


  /**
   * Get a List of CompanyDTO with the number of internship for a specific year.
   *
   * @param yearId the id of the year
   * @return a list of CompanyDTO.
   */
  List<CompanyDTO> getAllWithNumberInternship(int yearId);

  /**
   * Inserts a new company into the database, based on the provided CompanyDTO.
   *
   * @param companyDTO The CompanyDTO containing company information.
   * @return The CompanyDTO which has been inserted in db.
   */
  CompanyDTO insertCompany(CompanyDTO companyDTO);

  /**
   * Get one company by they TradeName and if specify designation.
   *
   * @param tradeName   the long name of the company
   * @param designation the complete name of the company
   * @return The CompanyDTO  which has been get in db.
   */
  CompanyDTO getOneByTradeNameDesignation(String tradeName, String designation);

  /**
   * Updates a company's blacklist status in the database.
   *
   * @param companyDTO The CompanyDTO object containing the information of the company.
   * @return The updated CompanyDTO object with the new blacklist status.
   */
  CompanyDTO blacklistCompany(CompanyDTO companyDTO);
}
