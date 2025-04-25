package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.InternshipSupervisorDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.dto.UserDTO.Role;
import be.vinci.pae.business.ucc.InternshipSupervisorUCC;
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
 * Resource for Internship Supervisors operations.
 */
@Singleton
@Path("/internshipSupervisors")
public class InternshipSupervisorResource {


  @Inject
  private MyLogger myLogger;

  @Inject
  private InternshipSupervisorUCC internshipSupervisorUCC;

  @Inject
  private Utils utils;

  /**
   * Add a new Internship Supervisor.
   *
   * @param supervisors The InternshipSupervisor to add
   * @param request     The context request.
   * @return the Internship Supervisor added
   */
  @Authorize({Role.STUDENT})
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public InternshipSupervisorDTO addInternshipSupervisor(InternshipSupervisorDTO supervisors,
      @Context ContainerRequest request) {

    if (supervisors.getFirstNameSupervisor().isEmpty()
        || supervisors.getFirstNameSupervisor().isBlank()
        || supervisors.getLastNameSupervisor().isEmpty()
        || supervisors.getLastNameSupervisor().isBlank()
        || supervisors.getPhoneNumberSupervisor().isEmpty()
        || supervisors.getPhoneNumberSupervisor().isBlank()
    ) {
      throw new ContractException("MISSING_FIELD");
    }

    if (supervisors.getCompanyIdSupervisor() <= 0) {
      throw new NotFoundException();
    }

    if (supervisors.getEmailSupervisor() != null) {
      supervisors.setEmailSupervisor(supervisors.getEmailSupervisor().toLowerCase());

      if (!supervisors.getEmailSupervisor()
          .matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$")) {
        throw new ContractException("INCORRECT_EMAIL");
      }
    }

    supervisors.setPhoneNumberSupervisor(supervisors.getPhoneNumberSupervisor().toLowerCase());
    utils.phoneNumberValidation(supervisors.getPhoneNumberSupervisor());

    UserDTO user = (UserDTO) request.getProperty("user");
    myLogger.request("POST /internshipSupervisors/" + " by user id " + user.getIdUser());
    return internshipSupervisorUCC.addInternshipSupervisors(supervisors);
  }

  /**
   * Get all Internship Supervisor of the company.
   *
   * @param request   The context request.
   * @param idCompany The company's ID.
   * @return the Internship Supervisor added
   */
  @Authorize({Role.STUDENT})
  @GET
  @Path("/{idCompany}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public List<InternshipSupervisorDTO> getSupervisors(@PathParam("idCompany") int idCompany,
      @Context ContainerRequest request) {

    if (idCompany < 1) {
      throw new ContractException("INCORRECT_VALUE");
    }
    UserDTO user = (UserDTO) request.getProperty("user");
    myLogger.request("GET /internshipSupervisors/" + idCompany + " by user id " + user.getIdUser());
    return internshipSupervisorUCC.getAllInternshipSupervisors(idCompany);
  }
}
