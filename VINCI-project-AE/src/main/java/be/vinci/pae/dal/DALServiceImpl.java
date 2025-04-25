package be.vinci.pae.dal;

import be.vinci.pae.business.BizFactory;
import be.vinci.pae.business.dto.CompanyDTO;
import be.vinci.pae.business.dto.ContactDTO.MeetingPlace;
import be.vinci.pae.business.dto.ContactDTO.State;
import be.vinci.pae.business.dto.InternshipSupervisorDTO;
import be.vinci.pae.business.dto.SchoolYearDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.dto.UserDTO.Role;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.exceptions.FatalException;
import be.vinci.pae.utils.exceptions.NotFoundException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Provide PrepareStatement from database.
 */
@Singleton
public class DALServiceImpl implements DALService, DALBizService {

  private final BasicDataSource dataSource;

  private final ThreadLocal<Connection> conn;

  @Inject
  private BizFactory bizFactory;


  /**
   * Construct a connection link to the db.
   */
  public DALServiceImpl() {
    this.conn = new ThreadLocal<>();

    // Create a BasicDataSource for pool of connection
    this.dataSource = new BasicDataSource();
    this.dataSource.setUrl(Config.getProperty("DbUrl"));
    this.dataSource.setUsername(Config.getProperty("DbUser"));
    this.dataSource.setPassword(Config.getProperty("DbPassword"));

    autoInitSql();

  }

  /**
   * Create db if not exist.
   */
  private void autoInitSql() {
    try (PreparedStatement ps = getPreparedStatement(
        "SELECT schema_name FROM information_schema.schemata  WHERE schema_name = 'pae';")) {
      try (ResultSet rs = ps.executeQuery()) {

        if (!rs.next()) {

          // Run sql file
          File myObj = new File(Config.getProperty("SqlFile"));
          StringBuilder sql = new StringBuilder();

          // read file
          try (Scanner myReader = new Scanner(myObj)) {
            // read all ligne of sql file
            while (myReader.hasNextLine()) {
              sql.append(myReader.nextLine());
            }
          } catch (FileNotFoundException e) {
            throw new NotFoundException(e);
          }

          // execute sql file
          try (PreparedStatement psAutoInit = getPreparedStatement(sql.toString());) {
            psAutoInit.executeUpdate();
          }
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      releaseConnection();
    }
  }

  @Override
  public PreparedStatement getPreparedStatement(String sql) {
    if (sql == null || sql.isEmpty() || sql.isBlank()) {
      return null;
    }
    try {
      // without transaction
      if (conn.get() == null) {
        Connection connection = dataSource.getConnection();
        conn.set(connection);
        return connection.prepareStatement(sql);
      }
      // with transaction
      return conn.get().prepareStatement(sql);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void startTransaction() {
    if (conn.get() != null) {
      throw new FatalException(new SQLException("Connection already used."));
    }
    try {
      Connection connection = dataSource.getConnection();
      connection.setAutoCommit(false);
      conn.set(connection);
    } catch (SQLException e) {
      throw new FatalException(e);
    }

  }

  @Override
  public void commitTransaction() {
    if (conn.get() == null) {
      throw new FatalException(new SQLException("Connection already closed."));
    }
    try {
      conn.get().commit();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      releaseConnection();
    }
  }

  @Override
  public void rollbackTransaction() {
    try {
      conn.get().rollback();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      releaseConnection();
    }
  }

  /**
   * Release the thread connection only if the transaction is not currently use.
   */
  private void releaseConnection() {
    Connection connection = conn.get();
    if (connection != null) {
      try {
        conn.remove();
        connection.setAutoCommit(true);
      } catch (SQLException e) {
        throw new FatalException(e);
      } finally {
        try {
          connection.close();
        } catch (SQLException e) {
          throw new FatalException(e);
        }
      }
    }
  }

  @Override
  public void releaseConnectionNoTx() {
    try {
      if (conn.get().getAutoCommit()) {
        releaseConnection();
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Inject UserDTO data by introspection.
   *
   * @param rs           ResultSet form SQL.
   * @param clazz        class use.
   * @param withSubClass is returning the sub tables or not
   * @param <T>          the type of return
   * @return an instance.
   */
  @Override
  public <T> T mapperRsToObject(ResultSet rs, Class<T> clazz, boolean withSubClass) {

    Class<?> bizClass = bizFactory.getClass();
    Method[] declaredMethods = bizClass.getDeclaredMethods();
    Method getMethodBizFacto = null;
    Object objectOfReturn;
    Set<String> setClassBiz = new HashSet<>();

    for (Method method : declaredMethods) {
      if (method.getReturnType().equals(clazz)) {
        getMethodBizFacto = method;
      }
      setClassBiz.add(method.getReturnType().getSimpleName().toLowerCase());
    }

    try {
      Objects.requireNonNull(getMethodBizFacto).setAccessible(true);
      objectOfReturn = getMethodBizFacto.invoke(bizFactory);
      getMethodBizFacto.setAccessible(true);
    } catch (Exception e) {
      throw new FatalException(e);
    }

    Set<String> columnNames = new HashSet<>();
    try {
      ResultSetMetaData rsmd = rs.getMetaData();
      int columnCount = rsmd.getColumnCount();
      for (int i = 1; i <= columnCount; i++) {
        columnNames.add(rsmd.getColumnName(i));
      }
    } catch (Exception e) {
      throw new FatalException(e);
    }

    Field[] fields = objectOfReturn.getClass().getDeclaredFields();
    for (Field field : fields) {
      String fieldName = field.getName();
      String methReturnValue = field.getType().getSimpleName().toLowerCase();
      if (!columnNames.contains(fieldName.toLowerCase()) && !setClassBiz.contains(
          methReturnValue)) {
        continue;
      }
      String methodName =
          "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
      try {
        Method setMethod = clazz.getMethod(methodName, field.getType());
        if (field.getType() == SchoolYearDTO.class && withSubClass) {
          setMethod.invoke(objectOfReturn, mapperRsToObject(rs, SchoolYearDTO.class, true));
        } else if (field.getType() == Role.class) {
          setMethod.invoke(objectOfReturn, Role.valueOf(rs.getString(fieldName).toUpperCase()));
        } else if (field.getType() == String.class) {
          setMethod.invoke(objectOfReturn, rs.getString(fieldName));
        } else if (field.getType() == int.class) {
          setMethod.invoke(objectOfReturn, rs.getInt(fieldName));
        } else if (field.getType() == Date.class) {
          setMethod.invoke(objectOfReturn, rs.getDate(fieldName));
        } else if (field.getType() == boolean.class) {
          setMethod.invoke(objectOfReturn, rs.getBoolean(fieldName));
        } else if (field.getType() == State.class) {
          setMethod.invoke(objectOfReturn, State.valueOf(rs.getString(fieldName).toUpperCase()));
        } else if (field.getType() == MeetingPlace.class && rs.getString(fieldName) != null) {
          setMethod.invoke(objectOfReturn,
              MeetingPlace.valueOf(rs.getString(fieldName).toUpperCase()));
        } else if (field.getType() == UserDTO.class && withSubClass) {
          setMethod.invoke(objectOfReturn, mapperRsToObject(rs, UserDTO.class, true));
        } else if (field.getType() == CompanyDTO.class && withSubClass) {
          setMethod.invoke(objectOfReturn, mapperRsToObject(rs, CompanyDTO.class, true));
        } else if (field.getType() == InternshipSupervisorDTO.class && withSubClass) {
          setMethod.invoke(objectOfReturn,
              mapperRsToObject(rs, InternshipSupervisorDTO.class, true));
        }
      } catch (Exception e) {
        throw new FatalException(e);
      }
    }
    return (T) objectOfReturn;
  }


}
