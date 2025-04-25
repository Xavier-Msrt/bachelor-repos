package be.vinci.pae.ihm.filters;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import be.vinci.pae.business.dto.UserDTO.Role;
import jakarta.ws.rs.NameBinding;
import java.lang.annotation.Retention;


/**
 * Authorize annotation class.
 */
@NameBinding
@Retention(RUNTIME)
public @interface Authorize {

  /**
   * Value method. Default value is an empty array.
   *
   * @return an array of Role.
   */
  Role[] value() default {Role.ADMINISTRATIVE, Role.TEACHER, Role.STUDENT};
}
