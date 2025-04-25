package be.vinci.pae.business.utils;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.exceptions.ContractException;
import be.vinci.pae.utils.exceptions.FatalException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import java.time.LocalDate;
import java.util.Date;

/**
 * The UtilsImpl class implements the Utils interface.
 */
public class UtilsImpl implements Utils {

  @Override
  public int currentSchoolYearStart() {
    LocalDate now = LocalDate.now();

    int year = now.getYear(); // Get the year from the given date
    int month = now.getMonthValue(); // Get the month from the given date

    // If the date is before September 15th of the current year,
    // then it belongs to the previous school year
    if (month < 9 || month == 9 && now.getDayOfMonth() < 15) {
      return year - 1;
    }
    return year;
  }

  @Override
  public ObjectNode makeToken(UserDTO userDTO) {
    int expireInHours = Config.getIntProperty("JWTExpiry");
    Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
    ObjectMapper jsonMapper = new ObjectMapper();

    String token;
    try {
      // convert ((1 minute * 60) = 1 hour) * expireInHours
      Date expired = new Date(System.currentTimeMillis() + ((60000 * 60) * expireInHours));

      // create token with user id
      token = JWT.create().withIssuer("auth0")
          .withClaim("user", userDTO.getIdUser())
          .withExpiresAt(expired)
          .sign(jwtAlgorithm);

      // create json with token
      return jsonMapper.createObjectNode()
          .put("token", token)
          .putPOJO("user", userDTO);
    } catch (Exception e) {
      throw new FatalException(e);
    }

  }

  @Override
  public void phoneNumberValidation(String phoneNumber) {
    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    try {
      PhoneNumber phone = phoneNumberUtil.parse(phoneNumber, "BE");
      if (!phoneNumberUtil.isPossibleNumber(phone)) {
        throw new ContractException("INCORRECT_PHONE_NUMBER");
      }
      phoneNumberUtil.isValidNumber(phone);
    } catch (Exception e) {
      throw new ContractException("INCORRECT_PHONE_NUMBER", e);
    }
  }

}
