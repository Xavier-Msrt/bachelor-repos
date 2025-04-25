package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.CompanyDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.dto.UserDTO.Role;
import be.vinci.pae.business.ucc.CompanyUCC;
import be.vinci.pae.business.utils.Utils;
import be.vinci.pae.ihm.filters.Authorize;
import be.vinci.pae.utils.exceptions.ContractException;
import be.vinci.pae.utils.exceptions.NotFoundException;
import be.vinci.pae.utils.loggers.MyLogger;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * Resource for companies operations.
 */
@Singleton
@Path("/companies")
public class CompanyResource {

  @Inject
  private MyLogger myLogger;

  @Inject
  private CompanyUCC companyUCC;

  @Inject
  private Utils utils;

  /**
   * Get all companies.
   *
   * @return a list of all companies.
   */
  @Authorize
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<CompanyDTO> getAll() {
    myLogger.request("GET /companies");
    return companyUCC.getAll();
  }

  /**
   * Get all companies with the number of user in internship fot all years.
   *
   * @param request The context request
   * @return a list of all companies.
   */
  @Authorize({Role.TEACHER})
  @Path("/admin")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<CompanyDTO> getAllWithNumberInternshipWithoutYear(@Context ContainerRequest request) {
    UserDTO user = (UserDTO) request.getProperty("user");
    myLogger.request("GET /companies/admin by user id " + user.getIdUser());
    return companyUCC.getAllWithNumberInternshipWithoutYear();
  }

  /**
   * Get all companies with the number of user in internship on the year of the year parameter.
   *
   * @param request The context request
   * @param yearId  the year targeted
   * @return a list of all companies.
   */
  @Authorize({Role.TEACHER})
  @Path("/admin/{yearId}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<CompanyDTO> getAllWithNumberInternship(@Context ContainerRequest request,
      @PathParam("yearId") int yearId) {
    if (yearId < 1) {
      throw new NotFoundException();
    }
    UserDTO user = (UserDTO) request.getProperty("user");
    myLogger.request("GET /companies/admin/" + yearId + " by user id " + user.getIdUser());
    return companyUCC.getAllWithNumberInternship(yearId);
  }


  /**
   * Retrieves one company. Requires an authentication token and a teacher role OR admin role.
   *
   * @param request   The context request
   * @param companyId the company targeted
   * @return a complete company
   */
  @Authorize({Role.TEACHER, Role.ADMINISTRATIVE})
  @Path("/{companyId}")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public CompanyDTO getOneCompany(@Context ContainerRequest request,
      @PathParam("companyId") int companyId) {
    if (companyId <= 0) {
      throw new NotFoundException();
    }
    UserDTO user = (UserDTO) request.getProperty("user");
    myLogger.request(
        "GET /companies/" + companyId + " by user id " + user.getIdUser());

    return companyUCC.getOneCompanyById(companyId);
  }

  /**
   * Insert a new company.
   *
   * @param companyDTO the company to insert.
   * @param request    The context request
   * @return the company inserted.
   */
  @Authorize({Role.STUDENT})
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public CompanyDTO insertOneCompany(@Context ContainerRequest request, CompanyDTO companyDTO) {
    if (companyDTO.getTradeName() == null
        || companyDTO.getTradeName().isBlank()
        || companyDTO.getTradeName().isEmpty()
        || companyDTO.getStreet().isEmpty()
        || companyDTO.getStreet().isBlank()
        || companyDTO.getBoxNumber().isEmpty()
        || companyDTO.getBoxNumber().isBlank()
        || companyDTO.getCity().isEmpty()
        || companyDTO.getCity().isBlank()
        || companyDTO.getPostCode().isBlank()
        || companyDTO.getPostCode().isEmpty()
    ) {
      throw new ContractException("MISSING_FIELD");
    }

    if (companyDTO.getEmailCompany() != null) {
      if (companyDTO.getEmailCompany().isBlank() || companyDTO.getEmailCompany().isEmpty()) {
        throw new ContractException("INCORRECT_EMAIL");
      }
      companyDTO.setEmailCompany(companyDTO.getEmailCompany().toLowerCase());

      if (!companyDTO.getEmailCompany()
          .matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$")) {
        throw new ContractException("INCORRECT_EMAIL");
      }
    }

    if (companyDTO.getPhoneNumberCompany() != null) {
      if (companyDTO.getPhoneNumberCompany().isBlank() || companyDTO.getPhoneNumberCompany()
          .isEmpty()) {
        throw new ContractException("INCORRECT_PHONE_NUMBER");
      }

      companyDTO.setPhoneNumberCompany(companyDTO.getPhoneNumberCompany().toLowerCase());
      utils.phoneNumberValidation(companyDTO.getPhoneNumberCompany());
    }

    UserDTO user = (UserDTO) request.getProperty("user");
    myLogger.request(
        "POST /companies " + " with company id " + companyDTO.getIdCompany() + " by user id "
            + user.getIdUser() + " requested");

    return companyUCC.insertNewCompany(user, companyDTO);
  }

  /**
   * Updates a company with a new blacklist status. This method requires an authentication token and
   * a teacher role.
   *
   * @param request            the context of the request
   * @param companyDTOToUpdate the company to update, serialized from JSON
   * @return the updated company
   * @throws ContractException if the ID is less than 0 or if the version is less than 1
   */
  @Authorize({Role.TEACHER})
  @POST
  @Path("/admin/blacklist")
  @Produces(MediaType.APPLICATION_JSON)
  public CompanyDTO blacklistOneCompany(@Context ContainerRequest request,
      CompanyDTO companyDTOToUpdate) {

    if (companyDTOToUpdate.getIdCompany() <= 0) {
      throw new NotFoundException();
    }

    if (companyDTOToUpdate.getVersionCompany() <= 0
        || companyDTOToUpdate.getBlackListMotivation() == null
        || companyDTOToUpdate.getBlackListMotivation().isBlank()
        || companyDTOToUpdate.getBlackListMotivation().isEmpty()) {
      throw new ContractException("MISSING_FIELD");
    }

    UserDTO userDTO = (UserDTO) request.getProperty("user");
    CompanyDTO companyDTOUpdated = companyUCC.blacklistOneCompany(companyDTOToUpdate);

    myLogger.request(
        "POST /companies/blacklist/" + companyDTOToUpdate.getIdCompany() + " by user id "
            + userDTO.getIdUser() + " requested");

    return companyDTOUpdated;
  }
}
