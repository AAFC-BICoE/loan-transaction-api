package ca.gc.aafc.transaction.api.testsupport.factories;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ca.gc.aafc.dina.testsupport.factories.TestableEntityFactory;
import ca.gc.aafc.transaction.api.entities.Transaction;

public class TransactionFactory implements TestableEntityFactory<Transaction> {

  public static final String GROUP = "test group";

  @Override
  public Transaction getEntityInstance() {
    return newTransaction().build();
  }

  public static Transaction.TransactionBuilder newTransaction() {
    return Transaction.builder()
        .uuid(UUID.randomUUID())
        .group(GROUP)
        .materialDirection(Transaction.Direction.IN)
        .attachment(List.of(UUID.randomUUID()))
        .agentRoles(new ArrayList<>(List.of(
            AgentRolesFactory.newAgentRoles().build(),
            AgentRolesFactory.newAgentRoles().build()
        )))
        .transactionNumber(TestableEntityFactory.generateRandomNameLettersOnly(12));
  }

}
