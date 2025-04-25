package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.ContactDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.dto.UserDTO.Role;
import be.vinci.pae.business.ucc.ContactUCC;
import be.vinci.pae.ihm.filters.Authorize;
import be.vinci.pae.utils.exceptions.ContractException;
import be.vinci.pae.utils.exceptions.NotFoundException;
import be.vinci.pae.utils.loggers.MyLogger;
import com.fasterxml.jackson.databind.JsonNode;
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
 * Resource for contacts operations.
 */
@Singleton
@Path("/contacts")
public class ContactResource {

  @Inject
  private MyLogger myLogger;

  @Inject
  private ContactUCC contactUCC;

  /**
   * Handles requests for contact information by company ID. Requires an authentication token.
   *
   * @param request   The context Request.
   * @param idCompany The company's ID.
   * @return A ContactDTO with userID and companyID.
   */
  @Authorize({Role.STUDENT})
  @GET
  @Path("/{idCompany}")
  @Produces(MediaType.APPLICATION_JSON)
  public ContactDTO contactInfo(@Context ContainerRequest request,
      @PathParam("idCompany") int idCompany) {

    if (idCompany <= 0) {
      throw new NotFoundException();
    }

    UserDTO student = (UserDTO) request.getProperty("user");
    ContactDTO contactDTO = contactUCC.getOneWithID(student.getIdUser(), idCompany);

    if (contactDTO == null) {
      throw new NotFoundException();
    }

    myLogger.request(
        "GET /contacts with company id " + idCompany + " by user id " + student.getIdUser());

    return contactDTO;
  }

  /**
   * Updates the state of a contact.
   *
   * @param request            The context Request.
   * @param companyId          The company's ID.
   * @param contactDTOToUpdate The JSON representation of the contact update.
   * @return The updated ContactDTO.
   */
  @Authorize({Role.STUDENT})
  @POST
  @Path("/{companyId}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public ContactDTO contactUpdateState(@PathParam("companyId") int companyId,
      ContactDTO contactDTOToUpdate, @Context ContainerRequest request) {

    if (contactDTOToUpdate.getState() == null
        || contactDTOToUpdate.getState().toString().isBlank()
        || contactDTOToUpdate.getVersionContact() <= 0) {
      throw new ContractException("MISSING_FIELD");
    }

    if (contactDTOToUpdate.getStudentIdContact() <= 0
        || contactDTOToUpdate.getCompanyIdContact() <= 0) {
      throw new NotFoundException();
    }

    UserDTO userDTO = (UserDTO) request.getProperty("user");

    if (userDTO.getIdUser() != contactDTOToUpdate.getStudentIdContact()) {
      throw new NotFoundException();
    }

    if (companyId != contactDTOToUpdate.getCompanyIdContact()) {
      throw new NotFoundException();
    }

    ContactDTO contactDTOUpdated = contactUCC.updateStateWithID(contactDTOToUpdate);

    myLogger.request(
        "POST /contacts/" + companyId + " with state " + contactDTOToUpdate.getState().toString()
            + " by user id " + contactDTOUpdated.getStudentIdContact());

    return contactDTOUpdated;
  }

  /**
   * Retrieves all contacts of the authenticated user. Requires an authentication token.
   *
   * @param request The context request
   * @return A list of ContactDTO
   */
  @Authorize({Role.STUDENT})
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<ContactDTO> getAllFromId(@Context ContainerRequest request) {
    UserDTO user = (UserDTO) request.getProperty("user");
    myLogger.request("GET /contacts/" + " by user id " + user.getIdUser());
    return contactUCC.getAllFromId(user.getIdUser());
  }

  /**
   * Handles /contacts/idUser requests. Need an authentication token and an administrative or
   * teacher role.
   *
   * @param idUser the id of the student's contacts list searched
   * @return a contactDTO list representing the contacts of the student or null
   */
  @GET
  @Path("/admin/contactsList/{idUser}")
  @Authorize({Role.ADMINISTRATIVE, Role.TEACHER})
  @Produces(MediaType.APPLICATION_JSON)
  public List<ContactDTO> getAllFromId(@PathParam("idUser") int idUser) {

    if (idUser <= 0) {
      throw new NotFoundException();
    }

    myLogger.request(
        "GET /contacts/idUser=" + idUser + " requested by an administrative or teacher");
    return contactUCC.getAllFromId(idUser);
  }


  /**
   * Inserts a new contact.
   *
   * @param request The context request.
   * @param json    the company id to insert.
   * @return The inserted contact.
   */
  @Authorize({Role.STUDENT})
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ContactDTO insertOneContact(@Context ContainerRequest request, JsonNode json) {
    if (!json.hasNonNull("companyId")) {
      throw new ContractException("MISSING_FIELD");
    }
    int companyId = json.get("companyId").asInt();
    UserDTO user = (UserDTO) request.getProperty("user");

    myLogger.request(
        "POST /contacts/" + " by user id " + user.getIdUser() + " with company id " + companyId);

    return contactUCC.createNewContact(companyId, user);
  }

  /**
   * Retrieves all contacts of the company. Requires an authentication token.
   *
   * @param request   The context request
   * @param idCompany the company id
   * @return A list of ContactDTO
   */
  @Authorize({Role.TEACHER, Role.ADMINISTRATIVE})
  @GET
  @Path("/admin/{idCompany}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ContactDTO> getAllContactCompany(@Context ContainerRequest request,
      @PathParam("idCompany") int idCompany) {
    UserDTO user = (UserDTO) request.getProperty("user");

    myLogger.request("GET /contacts/admin/" + idCompany + " by user id " + user.getIdUser());

    return contactUCC.getAllFromCompanyId(idCompany);
  }
}
