package be.vinci.pae.utils;

import be.vinci.pae.utils.exceptions.FatalException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Config class for reading properties file.
 */
public class Config {

  private static Properties props;

  /**
   * Load a file from a specific path.
   *
   * @param file the path name of the properties file.
   */
  public static void load(String file) {
    props = new Properties();
    try (InputStream input = new FileInputStream(file)) {
      props.load(input);
    } catch (IOException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Get the string value of key property.
   *
   * @param key the property.
   * @return value of the key property.
   */
  public static String getProperty(String key) {
    return props.getProperty(key);
  }

  /**
   * Get int value of key property.
   *
   * @param key the property.
   * @return value of the key property.
   */
  public static Integer getIntProperty(String key) {
    return Integer.parseInt(props.getProperty(key));
  }

  /**
   * Get boolean value of key property.
   *
   * @param key the property.
   * @return value of the key property.
   */
  public static boolean getBoolProperty(String key) {
    return Boolean.parseBoolean(props.getProperty(key));
  }

}
