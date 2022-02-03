package ca.gc.aafc.transaction.api.entities;

import ca.gc.aafc.transaction.api.BaseIntegrationTest;
import ca.gc.aafc.transaction.api.testsupport.factories.TransactionFactory;
import ca.gc.aafc.transaction.api.service.TransactionService;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Transactional
public class TransactionCrudIT extends BaseIntegrationTest {

  @Inject
  private TransactionService transactionService;

  @Test
  public void testCreate() {
    Transaction newTransaction = TransactionFactory.newTransaction().build();
    assertNull(newTransaction.getId());
    transactionService.createAndFlush(newTransaction);
    assertNotNull(newTransaction.getId());
    assertNotNull(newTransaction.getMaterialToBeReturned());

    //cleanup
    transactionService.delete(newTransaction);
  }

  @Test
  public void testFind() {
    Transaction newTransaction = TransactionFactory.newTransaction().build();
    transactionService.createAndFlush(newTransaction);

    Transaction foundTransaction = transactionService.findOne(newTransaction.getUuid(), Transaction.class);
    assertEquals(newTransaction.getId(), foundTransaction.getId());
    assertEquals(newTransaction.getUuid(), foundTransaction.getUuid());
    assertNotNull(foundTransaction.getCreatedOn());

    //cleanup
    transactionService.delete(newTransaction);
  }

}
