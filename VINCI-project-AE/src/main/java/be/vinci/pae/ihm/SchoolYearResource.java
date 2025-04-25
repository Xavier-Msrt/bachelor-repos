package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.SchoolYearDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.dto.UserDTO.Role;
import be.vinci.pae.business.ucc.SchoolYearUCC;
import be.vinci.pae.ihm.filters.Authorize;
import be.vinci.pae.utils.loggers.MyLogger;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * Resource for schoolyears operations.
 */
@Singleton
@Path("/schoolYears")
public class SchoolYearResource {

  @Inject
  private MyLogger myLogger;

  @Inject
  private SchoolYearUCC schoolYearUCCL;

  /**
   * Retrieves all school years. Requires an authentication token and a teacher role.
   *
   * @param request The context request
   * @return All school years.
   */
  @Authorize({Role.ADMINISTRATIVE, Role.TEACHER})
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<SchoolYearDTO> getAll(@Context ContainerRequest request) {
    UserDTO user = (UserDTO) request.getProperty("user");
    myLogger.request("GET /schoolyears/" + " by user id " + user.getIdUser());
    return schoolYearUCCL.getAll();
  }

}
