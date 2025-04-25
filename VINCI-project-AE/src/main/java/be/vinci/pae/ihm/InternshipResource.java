package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.ContactDTO.State;
import be.vinci.pae.business.dto.InternshipDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.dto.UserDTO.Role;
import be.vinci.pae.business.ucc.InternshipUCC;
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
import org.glassfish.jersey.server.ContainerRequest;

/**
 * Resource for internships operations.
 */
@Singleton
@Path("/internships")
public class InternshipResource {

  @Inject
  private MyLogger myLogger;

  @Inject
  private InternshipUCC internshipUCC;

  /**
   * Retrieves the internship of the authenticated user. Requires an authentication token.
   *
   * @param request The context request
   * @return The internship of the authenticated user or null if the user has no internship.
   */
  @Authorize({Role.STUDENT})
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public InternshipDTO getOneByStudentId(@Context ContainerRequest request) {
    UserDTO user = (UserDTO) request.getProperty("user");
    myLogger.request("GET /internships/" + " by user id " + user.getIdUser());
    return internshipUCC.getOneByStudentId(user.getIdUser());
  }

  /**
   * Handles /internships/idUser requests. Need an authentication token and an administrative or
   * teacher role.
   *
   * @param idUser the id of the student's internship searched
   * @return an internshipDTO representing the internship of the student or null
   */
  @GET
  @Path("/admin/{idUser}")
  @Authorize({Role.ADMINISTRATIVE, Role.TEACHER})
  @Produces(MediaType.APPLICATION_JSON)
  public InternshipDTO getOneByStudentId(@PathParam("idUser") int idUser) {

    if (idUser < 1) {
      throw new NotFoundException();
    }

    myLogger.request(
        "GET /intenships/idUser=" + idUser + " requested by an administrative or teacher");
    return internshipUCC.getOneByStudentId(idUser);
  }

  /**
   * Retrieves the total number of internships. This method requires an authentication token and a
   * teacher role.
   *
   * @param request The context request
   * @return The total number of internships.
   */
  @GET
  @Path("/admin/count")
  @Authorize({Role.TEACHER})
  @Produces(MediaType.APPLICATION_JSON)
  public int getInternshipsCountByYear(@Context ContainerRequest request) {

    UserDTO userDTO = (UserDTO) request.getProperty("user");
    myLogger.request("GET /internships/count by teacher id: " + userDTO.getIdUser() + " requested");

    return internshipUCC.getInternshipsCount();
  }

  /**
   * Retrieves the total number of internships for a specific year. This method requires an
   * authentication token and a teacher role.
   *
   * @param request The context request
   * @param yearId  The year for which the internships count is to be retrieved.
   * @return The total number of internships for the specified year.
   * @throws ContractException If the yearId is less than 0.
   */
  @GET
  @Path("/admin/count/{yearId}")
  @Authorize({Role.TEACHER})
  @Produces(MediaType.APPLICATION_JSON)
  public int getInternshipsCountByYear(@Context ContainerRequest request,
      @PathParam("yearId") int yearId) {

    if (yearId < 1) {
      throw new NotFoundException();
    }

    UserDTO userDTO = (UserDTO) request.getProperty("user");
    myLogger.request("GET /internships/count/" + yearId + " by teacher id: " + userDTO.getIdUser()
        + " requested");

    return internshipUCC.getInternshipsCountByYear(yearId);
  }

  /**
   * Creates the internship of the authenticated user. Requires an authentication token.
   *
   * @param request       The context request
   * @param internshipDTO The information of the internship
   * @return The internship of the authenticated user.
   */
  @Authorize({Role.STUDENT})
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public InternshipDTO createOneInternship(@Context ContainerRequest request,
      InternshipDTO internshipDTO) {

    if (internshipDTO.getStudentIdInternship() <= 0
        || internshipDTO.getContactInternship().getStudentIdContact() <= 0
        || internshipDTO.getContactInternship().getVersionContact() <= 0
        || internshipDTO.getContactInternship().getCompanyIdContact() <= 0
        || internshipDTO.getCompanyIdInternship() <= 0
        || internshipDTO.getSchoolYearIdInternship() <= 0
        || internshipDTO.getInternSupervisorId() <= 0) {
      throw new NotFoundException();
    }

    if (internshipDTO.getContactInternship().getState() == null
        || internshipDTO.getSignatureDate() == null) {
      throw new ContractException("MISSING_FIELD");
    }

    if (!internshipDTO.getContactInternship().getState().equals(State.ACCEPTED)) {
      throw new ContractException("ACCEPTED_STATE");
    }

    UserDTO userDTO = (UserDTO) request.getProperty("user");

    if (userDTO.getIdUser() != internshipDTO.getContactInternship().getStudentIdContact()
        || userDTO.getIdUser() != internshipDTO.getStudentIdInternship()) {
      throw new NotFoundException();
    }

    myLogger.request("POST /internships/" + internshipDTO.getCompanyIdInternship() + " by user id "
        + internshipDTO.getStudentIdInternship());
    return internshipUCC.createOneInternship(internshipDTO);
  }

  /**
   * Updates the internship project of the authenticated user. Requires an authentication token.
   *
   * @param request       The context request
   * @param internshipDTO The information of the internship
   * @return The updated internship of the authenticated user.
   */
  @Authorize({Role.STUDENT})
  @POST
  @Path("/update")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public InternshipDTO updateInternShipProject(@Context ContainerRequest request,
      InternshipDTO internshipDTO) {

    if (internshipDTO.getIdInternship() <= 0
        || internshipDTO.getStudentIdInternship() <= 0
        || internshipDTO.getCompanyIdInternship() <= 0
        || internshipDTO.getSchoolYearIdInternship() <= 0
        || internshipDTO.getInternSupervisorId() <= 0) {
      throw new NotFoundException();
    }

    if (internshipDTO.getSignatureDate() == null
        || internshipDTO.getInternShipProject() == null
        || internshipDTO.getInternShipProject().isEmpty()
        || internshipDTO.getVersionInternship() <= 0) {
      throw new ContractException("MISSING_FIELD");
    }

    UserDTO userDTO = (UserDTO) request.getProperty("user");

    if (userDTO.getIdUser() != internshipDTO.getStudentIdInternship()) {
      throw new NotFoundException();
    }

    myLogger.request("POST /internships/update" + internshipDTO.getCompanyIdInternship()
        + " by user id " + internshipDTO.getStudentIdInternship());
    return internshipUCC.updateInternShipProject(internshipDTO);
  }

}
