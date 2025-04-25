package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.CompanyDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.utils.exceptions.ContractException;
import be.vinci.pae.utils.exceptions.FatalException;
import java.util.List;

/**
 * The CompanyUCCImpl class is an implementation of the CompanyUCC interface and represents a
 * Company Use Case Controller (UCC) in the system. This interface is responsible for handling
 * contact-related operations.
 */
public interface CompanyUCC {


  /**
   * Retrieves a list of all companies.
   *
   * @return A list of {@link CompanyDTO} objects representing all companies
   */
  List<CompanyDTO> getAll();

  /**
   * Retrieves a list of all companies with the number of users in internship for all years.
   *
   * @return A list of {@link CompanyDTO} objects representing all companies
   */
  List<CompanyDTO> getAllWithNumberInternshipWithoutYear();


  /**
   * Retrieves a list of all companies with the number of users in internship on the year with the
   * id precise in parameter.
   *
   * @param yearId the id of the year
   * @return A list of {@link CompanyDTO} objects representing all companies
   */
  List<CompanyDTO> getAllWithNumberInternship(int yearId);

  /**
   * Retrieves a CompanyDTO object based on company IDs.
   *
   * @param id the company id
   * @return A complete CompanyDTO object.
   */
  CompanyDTO getOneCompanyById(int id);

  /**
   * Inserts a new company into the system.
   *
   * @param companyDTO The DTO (Data Transfer Object) representing the company to be inserted.
   * @param userDTO The DTO (Data Transfer Object) representing the user that made the request.
   * @return The DTO representing the newly inserted company.
   * @throws ContractException If the company already exists.
   * @throws FatalException    If an unexpected error occurs during the insertion process.
   */
  CompanyDTO insertNewCompany(UserDTO userDTO, CompanyDTO companyDTO);

  /**
   * Updates a company based on the provided CompanyDTO.
   *
   * @param companyDTO The CompanyDTO containing new company information.
   * @return A CompanyDTO representing the updated company if successful, or null if fails.
   */
  CompanyDTO blacklistOneCompany(CompanyDTO companyDTO);
}
