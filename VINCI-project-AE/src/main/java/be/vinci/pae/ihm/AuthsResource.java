package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.business.utils.Utils;
import be.vinci.pae.utils.exceptions.ContractException;
import be.vinci.pae.utils.loggers.MyLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Resource for authentication operations.
 */
@Singleton
@Path("/auths")
public class AuthsResource {

  @Inject
  private MyLogger myLogger;

  @Inject
  private UserUCC userUCC;

  @Inject
  private Utils utils;

  /**
   * Handles user login requests.
   *
   * @param json contains a jsonNode object with login and password
   * @return a json with inside a token and user
   */
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode login(JsonNode json) {
    if (!json.hasNonNull("emailUser") || !json.hasNonNull("password")) {
      throw new ContractException("MISSING_FIELD");
    }

    String email = json.get("emailUser").asText();
    String password = json.get("password").asText();

    email = email.toLowerCase();

    if (email.isBlank() || password.isBlank() || email.isEmpty() || password.isEmpty()) {
      throw new ContractException("MISSING_FIELD");
    }

    if (!email.matches("^[a-z]+\\.[a-z]+@(student.)?vinci\\.be$")) {
      throw new ContractException("INCORRECT_VINCI_EMAIL");
    }

    UserDTO userDTO = userUCC.login(email, password);

    myLogger.request(
        "POST /auths/login " + userDTO.getEmailUser() + " by user id " + userDTO.getIdUser()
            + " logged in");

    return utils.makeToken(userDTO);
  }

  /**
   * Handles user register requests.
   *
   * @param userDTO contains a userDTO object with user info
   * @return a json with inside a token and user
   */
  @POST
  @Path("register")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode register(UserDTO userDTO) {
    // Get and check credentials
    if (userDTO.getEmailUser().isEmpty() || userDTO.getEmailUser().isBlank()
        || userDTO.getPassword().isEmpty() || userDTO.getPassword().isBlank()
        || userDTO.getLastNameUser().isEmpty() || userDTO.getLastNameUser().isBlank()
        || userDTO.getFirstNameUser().isEmpty() || userDTO.getFirstNameUser().isBlank()
        || userDTO.getPhoneNumberUser().isEmpty() || userDTO.getPhoneNumberUser().isBlank()
        || userDTO.getEmailUser().isEmpty() || userDTO.getEmailUser().isBlank()
        || userDTO.getRole() == null) {
      throw new ContractException("MISSING_FIELD");
    }

    userDTO.setEmailUser(userDTO.getEmailUser().toLowerCase());

    if (!userDTO.getEmailUser().matches("^[a-z]+\\.[a-z]+@(student.)?vinci\\.be$")) {
      throw new ContractException("INCORRECT_VINCI_EMAIL");
    }

    userDTO.setPhoneNumberUser(userDTO.getPhoneNumberUser().toLowerCase());

    utils.phoneNumberValidation(userDTO.getPhoneNumberUser());

    // Register user
    UserDTO user = userUCC.register(userDTO);

    myLogger.request(
        "POST /auths/register " + user.getEmailUser() + " by user id " + user.getIdUser()
            + " registered");

    return utils.makeToken(user);
  }
}

