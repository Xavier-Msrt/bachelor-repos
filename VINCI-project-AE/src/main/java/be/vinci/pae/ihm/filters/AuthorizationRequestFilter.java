package be.vinci.pae.ihm.filters;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.exceptions.ForbiddenException;
import be.vinci.pae.utils.exceptions.UnauthorizedException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * AuthorizationRequestFilter class.
 */
@Singleton
@Provider
@Authorize
public class AuthorizationRequestFilter implements ContainerRequestFilter {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final JWTVerifier jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0")
      .build();

  @Inject
  private UserUCC myUserUCC;
  @Context
  private ResourceInfo resourceInfo;

  /**
   * Check the user is authorized using its Token to access the requested URI.
   *
   * @param requestContext the requested URI.
   */
  @Override
  public void filter(ContainerRequestContext requestContext) {
    String token = requestContext.getHeaderString("Authorization");
    if (token == null) {
      throw new UnauthorizedException();
    } else {
      DecodedJWT decodedToken = null;
      try {
        decodedToken = this.jwtVerifier.verify(token);
      } catch (Exception e) {
        throw new UnauthorizedException("Invalid token.", e);
      }

      UserDTO authenticatedUser = myUserUCC.getOneById(decodedToken.getClaim("user").asInt());

      // Get the method that is currently running
      Method method = resourceInfo.getResourceMethod();

      // Get the Authorize annotation of the method
      Authorize authorization = method.getAnnotation(Authorize.class);

      if (authorization != null) {
        // Get the roles from the annotation
        UserDTO.Role[] requiredRoles = authorization.value();

        // Check if the user has one of the required roles
        if (!Arrays.asList(requiredRoles).contains(authenticatedUser.getRole())) {
          throw new ForbiddenException();
        } else {
          requestContext.setProperty("user", authenticatedUser);
        }
      }
    }
  }
}
