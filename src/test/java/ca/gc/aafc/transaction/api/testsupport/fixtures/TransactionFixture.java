package ca.gc.aafc.transaction.api.testsupport.fixtures;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.HashMap;

import org.apache.commons.lang3.RandomStringUtils;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.transaction.api.dto.TransactionDto;
import ca.gc.aafc.transaction.api.entities.Transaction;
import ca.gc.aafc.transaction.api.testsupport.factories.AgentRolesFactory;

public final class TransactionFixture {

  public static final String GROUP = "group 1";

  private TransactionFixture() {
  }

  public static TransactionDto.TransactionDtoBuilder newTransaction() {
    return TransactionDto.builder()
        .agentRoles(new ArrayList<>(List.of(
          AgentRolesFactory.newAgentRoles().build(),
          AgentRolesFactory.newAgentRoles().build()
        )))
        .materialDirection(Transaction.Direction.IN)
        .transactionNumber(RandomStringUtils.randomAlphabetic(12))
        .otherIdentifiers(List.of("T2123", "P245643"))
        .managedAttributes(new HashMap<>())
        .attachment(List.of(ExternalRelationDto.builder()
          .id(UUID.randomUUID().toString())
          .type("metadata")
          .build())
        )
        .group(GROUP);
  }
}
