package ca.gc.aafc.transaction.api.entities;

import ca.gc.aafc.dina.testsupport.factories.TestableEntityFactory;
import ca.gc.aafc.transaction.api.BaseIntegrationTest;
import ca.gc.aafc.transaction.api.testsupport.factories.ShipmentFactory;
import ca.gc.aafc.transaction.api.testsupport.factories.TransactionFactory;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Integration tests related to the Shipment part of Transaction
 */
@Transactional
public class TransactionShipmentCrudIT extends BaseIntegrationTest {

  @Test
  public void testCreate() {

    // First test with a shipment with a status too long to make sure validation of
    // the nested element is in place
    Shipment shipment = ShipmentFactory.newShipment()
        .status(TestableEntityFactory.generateRandomNameLettersOnly(250)).build();

    Transaction newTransaction = TransactionFactory.newTransaction().shipment(shipment).build();

    assertThrows(ConstraintViolationException.class,
        () -> transactionService.createAndFlush(newTransaction));

    // Fix the issue and retry
    newTransaction.setShipment(ShipmentFactory.newShipment().build());

    transactionService.createAndFlush(newTransaction);

    //cleanup
    transactionService.delete(newTransaction);
  }

}
