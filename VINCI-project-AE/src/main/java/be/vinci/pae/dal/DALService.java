package be.vinci.pae.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Provide PrepareStatement from database.
 */
public interface DALService {

  /**
   * Create and provide an PreparedStatement.
   *
   * @param sql the sql request for the PreparedStatement
   * @return PreparedStatement with sql
   */
  PreparedStatement getPreparedStatement(String sql);


  /**
   * Release the thread connection only if the transaction is not currently use.
   */
  void releaseConnectionNoTx();

  /**
   * Inject UserDTO data by introspection.
   *
   * @param rs           ResultSet form SQL request.
   * @param clazz        Class of the ResultSet.
   * @param withSubClass Boolean to know if we need to inject subclass.
   * @param <T>          Type of the instance.
   * @return an instance.
   */
  <T> T mapperRsToObject(ResultSet rs, Class<T> clazz, boolean withSubClass);

}
