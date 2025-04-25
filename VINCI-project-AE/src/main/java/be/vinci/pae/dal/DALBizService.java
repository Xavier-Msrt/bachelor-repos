package be.vinci.pae.dal;

/**
 * Provide system transaction.
 */
public interface DALBizService {

  /**
   * Stat a sql transaction.
   */
  void startTransaction();

  /**
   * Commit a transaction.
   */
  void commitTransaction();

  /**
   * Rollback a transaction.
   */
  void rollbackTransaction();

}
