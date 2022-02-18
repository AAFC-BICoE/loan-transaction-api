package ca.gc.aafc.transaction.api.entities;

import java.util.Map;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.transaction.api.BaseIntegrationTest;
import ca.gc.aafc.transaction.api.testsupport.factories.TransactionFactory;
import ca.gc.aafc.transaction.api.testsupport.factories.TransactionManagedAttributeFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Transactional
public class TransactionCrudIT extends BaseIntegrationTest {

  @Test
  public void testCreate() {
    TransactionManagedAttribute newTransactionManagedAttribute = TransactionManagedAttributeFactory
        .newManagedAttribute()
        .build();
    managedAttributeService.createAndFlush(newTransactionManagedAttribute);

    Transaction newTransaction = TransactionFactory.newTransaction()
        .managedAttributes(Map.of(newTransactionManagedAttribute.getKey(), "anything"))
        .build();
    assertNull(newTransaction.getId());
    transactionService.createAndFlush(newTransaction);

    assertNotNull(newTransaction.getId());
    assertNotNull(newTransaction.getMaterialToBeReturned());

    //cleanup
    transactionService.delete(newTransaction);
    transactionService.flush();

    managedAttributeService.delete(newTransactionManagedAttribute);
  }

  @Test
  public void testFind() {
    TransactionManagedAttribute newTransactionManagedAttribute = TransactionManagedAttributeFactory
        .newManagedAttribute()
        .build();
    managedAttributeService.createAndFlush(newTransactionManagedAttribute);

    Transaction newTransaction = TransactionFactory.newTransaction()
        .managedAttributes(Map.of(newTransactionManagedAttribute.getKey(), "anything"))
        .build();
    transactionService.createAndFlush(newTransaction);

    Transaction foundTransaction = transactionService.findOne(newTransaction.getUuid(), Transaction.class);
    assertEquals(newTransaction.getId(), foundTransaction.getId());
    assertEquals(newTransaction.getUuid(), foundTransaction.getUuid());
    assertEquals(newTransaction.getManagedAttributes().get(newTransactionManagedAttribute.getKey()), 
        foundTransaction.getManagedAttributes().get(newTransactionManagedAttribute.getKey()));
    assertNotNull(foundTransaction.getCreatedOn());

    //cleanup
    transactionService.delete(newTransaction);
    transactionService.flush();

    managedAttributeService.delete(newTransactionManagedAttribute);
  }
}
