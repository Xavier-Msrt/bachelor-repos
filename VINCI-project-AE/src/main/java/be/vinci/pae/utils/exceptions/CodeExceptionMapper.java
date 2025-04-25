package be.vinci.pae.utils.exceptions;

/**
 * Interface for code exception mapper.
 */
public interface CodeExceptionMapper {

  /**
   * Translate code exception into a sentence.
   *
   * @param code the code exception
   * @return a translated sentence
   */
  String codeMapper(String code);
}
