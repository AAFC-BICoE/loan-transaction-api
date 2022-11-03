package ca.gc.aafc.transaction.api.entities;

import java.util.List;
import java.util.Map;
import java.util.UUID;
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
        .materialSamples(List.of(UUID.randomUUID()))
        .build();
    assertNull(newTransaction.getId());
    transactionService.createAndFlush(newTransaction);
    // detach to force reload from the database
    transactionService.detach(newTransaction);

    Transaction t = transactionService.findOne(newTransaction.getUuid(), Transaction.class);
    assertNotNull(t.getId());
    assertNotNull(t.getMaterialToBeReturned());
    assertNotNull(t.getMaterialSamples());

    //cleanup
    transactionService.delete(t);
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
