package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.dto.UserDTO.Role;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.business.utils.Utils;
import be.vinci.pae.ihm.filters.Authorize;
import be.vinci.pae.utils.exceptions.ContractException;
import be.vinci.pae.utils.exceptions.NotFoundException;
import be.vinci.pae.utils.loggers.MyLogger;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
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
 * Resource for users related operations.
 */
@Singleton
@Path("/users")
public class UserRessource {

  @Inject
  private MyLogger myLogger;

  @Inject
  private UserUCC userUCC;

  @Inject
  private Utils utils;

  /**
   * Handles /users/me requests. Need an authentication token.
   *
   * @param request the context Request
   * @return a UserDTO representing the user
   */
  @Authorize
  @GET
  @Path("me")
  @Produces(MediaType.APPLICATION_JSON)
  public UserDTO userInfo(@Context ContainerRequest request) {
    UserDTO userDTO = (UserDTO) request.getProperty("user");
    myLogger.request(
        "GET /users/me " + userDTO.getEmailUser() + " by user id " + userDTO.getIdUser()
            + " requested");
    return userDTO;
  }

  /**
   * Handles /users/idUser requests. Need an authentication token and an administrative or teacher
   * role.
   *
   * @param idUser the id of the user searched
   * @return a UserDTO representing the user or null
   */
  @GET
  @Path("/admin/{idUser}")
  @Authorize({Role.ADMINISTRATIVE, Role.TEACHER})
  @Produces(MediaType.APPLICATION_JSON)
  public UserDTO userInfo(@PathParam("idUser") int idUser) {

    if (idUser < 1) {
      throw new NotFoundException();
    }

    myLogger.request("GET /users/idUser=" + idUser + " requested by an administrative or teacher");
    return userUCC.getOneById(idUser);
  }

  /**
   * Handles /user/updatePhoneNumber requests. Need an authentication token.
   *
   * @param request         the context Request and DTO
   * @param userDTOToUpdate serialize json to userDTO
   * @return an updated UserDTO without password
   */
  @Authorize
  @POST
  @Path("updatePhoneNumber")
  @Produces(MediaType.APPLICATION_JSON)
  public UserDTO updatePersonalData(@Context ContainerRequest request, UserDTO userDTOToUpdate) {

    UserDTO userDTO = (UserDTO) request.getProperty("user");
    if (userDTO.getIdUser() != userDTOToUpdate.getIdUser()
        || userDTOToUpdate.getIdUser() < 0) {
      throw new NotFoundException();
    }

    if (userDTOToUpdate.getPhoneNumberUser().isBlank()
        || userDTOToUpdate.getVersionUser() <= 0
        || userDTOToUpdate.getPhoneNumberUser().isEmpty()) {
      throw new ContractException("MISSING_FIELD");
    }

    utils.phoneNumberValidation(userDTOToUpdate.getPhoneNumberUser());

    UserDTO userDTOUpdated = userUCC.updateUserPhoneNumber(userDTOToUpdate);
    myLogger.request(
        "POST /users/updatePhoneNumber " + userDTOToUpdate.getEmailUser() + " by user id "
            + userDTOToUpdate.getIdUser() + " requested");
    return userDTOUpdated;
  }

  /**
   * Handles /user/updatePassword requests. This method requires an authentication token.
   *
   * @param request the context Request containing the user's information
   * @param json    the json object containing the user's information
   * @return an updated UserDTO without password
   * @throws ContractException if the old password, new password, user id, or version number is
   *                           missing
   * @throws NotFoundException if the user id does not match the id of the user making the request
   */
  @Authorize
  @POST
  @Path("updatePassword")
  @Produces(MediaType.APPLICATION_JSON)
  public UserDTO updatePassword(@Context ContainerRequest request, JsonNode json) {
    String oldPassword = null;
    String newPassword = null;

    if (!json.hasNonNull("password") || !json.hasNonNull("newPassword")) {
      throw new ContractException("MISSING_FIELD");
    }
    oldPassword = json.get("password").asText();
    newPassword = json.get("newPassword").asText();

    if (oldPassword.isBlank() || oldPassword.isEmpty() || newPassword.isBlank()
        || newPassword.isEmpty()) {
      throw new ContractException("MISSING_FIELD");
    }

    if (oldPassword.equals(newPassword)) {
      throw new ContractException("NOT_FOUND");
    }

    UserDTO userDTOToUpdate = (UserDTO) request.getProperty("user");

    myLogger.request(
        "POST /users/updatePassword" + userDTOToUpdate.getEmailUser() + "by user id "
            + userDTOToUpdate.getIdUser() + " requested");

    return userUCC.updatePassword(userDTOToUpdate, newPassword, oldPassword);
  }

  /**
   * Retrieves all users and the date of their stage if they have one. Requires an authentication
   * token and an administrative or teacher role.
   *
   * @return A json object list of users with their internship date if present
   */
  @GET
  @Path("/admin/usersList")
  @Authorize({Role.ADMINISTRATIVE, Role.TEACHER})
  @Produces(MediaType.APPLICATION_JSON)
  public List<UserDTO> getAllUsers() {
    myLogger.request("GET /users/usersList by an admin or teacher requested");
    return userUCC.getAllUsers();
  }

  /**
   * Retrieves the total number of users. This method requires an authentication token and a teacher
   * role.
   *
   * @param request The context request
   * @return The total number of users.
   */
  @GET
  @Path("/admin/count")
  @Authorize({Role.TEACHER})
  @Produces(MediaType.APPLICATION_JSON)
  public int getUsersCount(@Context ContainerRequest request) {

    UserDTO userDTO = (UserDTO) request.getProperty("user");
    myLogger.request("GET /users/count by teacher id: " + userDTO.getIdUser() + " requested");

    return userUCC.getUsersCount();
  }

  /**
   * Retrieves the total number of users for a specific year. This method requires an authentication
   * token and a teacher role.
   *
   * @param request The context request
   * @param yearId  The year for which the user count is to be retrieved.
   * @return The total number of users for the specified year.
   * @throws ContractException If the yearId is less than 0.
   */
  @GET
  @Path("/admin/count/{yearId}")
  @Authorize({Role.TEACHER})
  @Produces(MediaType.APPLICATION_JSON)
  public int getUsersCountByYear(@Context ContainerRequest request,
      @PathParam("yearId") int yearId) {

    if (yearId <= 0) {
      throw new NotFoundException();
    }

    UserDTO userDTO = (UserDTO) request.getProperty("user");
    myLogger.request("GET /users/count/" + yearId + " by teacher id: " + userDTO.getIdUser()
        + " requested");

    return userUCC.getUsersCountByYear(yearId);
  }
}
