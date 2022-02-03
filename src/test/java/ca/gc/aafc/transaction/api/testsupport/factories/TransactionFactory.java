package ca.gc.aafc.transaction.api.testsupport.factories;

import ca.gc.aafc.dina.testsupport.factories.TestableEntityFactory;
import ca.gc.aafc.transaction.api.entities.Transaction;

public class TransactionFactory implements TestableEntityFactory<Transaction> {

  public static final String GROUP = "test group";

  @Override
  public Transaction getEntityInstance() {
    return newTransaction().build();
  }

  public static Transaction.TransactionBuilder newTransaction() {
    return Transaction.
        builder()
        .group(GROUP)
        .materialDirection(Transaction.Direction.IN)
        .transactionNumber(TestableEntityFactory.generateRandomNameLettersOnly(12));
  }

}
