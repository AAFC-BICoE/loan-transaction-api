package ca.gc.aafc.transaction.api.crud;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.transaction.api.BaseIntegrationTest;

import javax.transaction.Transactional;

/**
 * Base class for CRUD-based Integration tests. The main purpose is to ensure
 * all entities can be saved/loaded/deleted from a database.
 * 
 * This base class with run a single test (see testCRUDOperations) to control to
 * order of testing of save/find/remove.
 * 
 */
@Transactional
public abstract class BaseEntityCRUDIT extends BaseIntegrationTest {

  /**
   * Runs the three main CRUD methods while performing a transaction for each
   * test.
   * 
   * @throws InstantiationException
   * @throws IllegalAccessException
   */
  @Test
  public void testCRUDOperations() throws InstantiationException, IllegalAccessException {
    testSave();
    testFind();
    testRemove();
  }

  public abstract void testSave();

  public abstract void testFind();

  public abstract void testRemove();

}
