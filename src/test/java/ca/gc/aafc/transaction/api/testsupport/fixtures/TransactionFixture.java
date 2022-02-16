package ca.gc.aafc.transaction.api.testsupport.fixtures;

import ca.gc.aafc.transaction.api.dto.TransactionDto;
import ca.gc.aafc.transaction.api.entities.Transaction;
import ca.gc.aafc.transaction.api.testsupport.factories.AgentRolesFactory;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

public final class TransactionFixture {

  public static final String GROUP = "group 1";

  private TransactionFixture() {
  }

  public static TransactionDto.TransactionDtoBuilder newTransaction() {
    return TransactionDto.builder()
        .agentRoles(List.of(
          AgentRolesFactory.newAgentRoles().build(),
          AgentRolesFactory.newAgentRoles().build()
        ))
        .materialDirection(Transaction.Direction.IN)
        .transactionNumber(RandomStringUtils.randomAlphabetic(12))
        .otherIdentifiers(List.of("T2123", "P245643"))
        .group(GROUP);
  }
}
