package ca.gc.aafc.transaction.api.entities;

import java.util.Map;
import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.entity.ManagedAttribute.ManagedAttributeType;
import ca.gc.aafc.transaction.api.BaseIntegrationTest;
import ca.gc.aafc.transaction.api.testsupport.factories.TransactionFactory;
import ca.gc.aafc.transaction.api.testsupport.factories.TransactionManagedAttributeFactory;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Transactional
public class TransactionCrudIT extends BaseIntegrationTest {

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

  @Test
  void validate_WhenValidStringType() {
    Transaction newTransaction = TransactionFactory.newTransaction().build();
    transactionService.createAndFlush(newTransaction);

    TransactionManagedAttribute testManagedAttribute = TransactionManagedAttributeFactory.newManagedAttribute()
        .acceptedValues(null)
        .build();

    managedAttributeService.create(testManagedAttribute);

    newTransaction.setManagedAttributes(Map.of(testManagedAttribute.getKey(), "anything"));
    assertDoesNotThrow(() -> transactionService.update(newTransaction));

    //cleanup
    transactionService.delete(newTransaction);
    transactionService.flush();

    managedAttributeService.delete(testManagedAttribute);
  }

  @Test
  void validate_WhenInvalidIntegerTypeExceptionThrown() {
    Transaction newTransaction = TransactionFactory.newTransaction().build();
    transactionService.createAndFlush(newTransaction);

    TransactionManagedAttribute testManagedAttribute = TransactionManagedAttributeFactory.newManagedAttribute()
        .acceptedValues(null)
        .managedAttributeType(ManagedAttributeType.INTEGER)
        .build();

    managedAttributeService.create(testManagedAttribute);

    newTransaction.setManagedAttributes(Map.of(testManagedAttribute.getKey(), "1.2"));

    assertThrows(ValidationException.class, () ->  transactionService.update(newTransaction));

    //cleanup
    transactionService.delete(newTransaction);
    transactionService.flush();

    managedAttributeService.delete(testManagedAttribute);
  }

  @Test
  void assignedValueContainedInAcceptedValues_validationPasses() {
    Transaction newTransaction = TransactionFactory.newTransaction().build();
    transactionService.createAndFlush(newTransaction);

    TransactionManagedAttribute testManagedAttribute = TransactionManagedAttributeFactory.newManagedAttribute()
        .acceptedValues(new String[]{"val1", "val2"})
        .build();

    managedAttributeService.create(testManagedAttribute);

    newTransaction.setManagedAttributes(Map.of(testManagedAttribute.getKey(), testManagedAttribute.getAcceptedValues()[0]));
    assertDoesNotThrow(() -> transactionService.update(newTransaction));

    //cleanup
    transactionService.delete(newTransaction);
    transactionService.flush();

    managedAttributeService.delete(testManagedAttribute);
  }

  @Test
  void assignedValueNotContainedInAcceptedValues_validationPasses() {
    Transaction newTransaction = TransactionFactory.newTransaction().build();
    transactionService.createAndFlush(newTransaction);

    TransactionManagedAttribute testManagedAttribute = TransactionManagedAttributeFactory.newManagedAttribute()
        .acceptedValues(new String[]{"val1", "val2"})
        .build();

    managedAttributeService.create(testManagedAttribute);

    newTransaction.setManagedAttributes(Map.of(testManagedAttribute.getKey(), "val3"));
    assertThrows(ValidationException.class, () ->  transactionService.update(newTransaction));

    //cleanup
    transactionService.delete(newTransaction);
    transactionService.flush();

    managedAttributeService.delete(testManagedAttribute);
  }
}
