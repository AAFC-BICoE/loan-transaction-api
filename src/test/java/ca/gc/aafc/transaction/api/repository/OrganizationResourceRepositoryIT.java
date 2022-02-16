package ca.gc.aafc.transaction.api.repository;

import ca.gc.aafc.transaction.api.BaseIntegrationTest;
import ca.gc.aafc.dina.testsupport.security.WithMockKeycloakUser;
import ca.gc.aafc.transaction.api.dto.TransactionDto;
import ca.gc.aafc.transaction.api.entities.AgentRoles;
import ca.gc.aafc.transaction.api.testsupport.fixtures.ShipmentTestFixture;
import ca.gc.aafc.transaction.api.testsupport.fixtures.TransactionFixture;
import io.crnk.core.queryspec.QuerySpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest(properties = {"keycloak.enabled: true"})
public class OrganizationResourceRepositoryIT extends BaseIntegrationTest {

  @Inject
  private TransactionRepository transactionRepository;

  @WithMockKeycloakUser(username = "user", groupRole = TransactionFixture.GROUP + ":staff")
  @Test
  public void create_onValidData_transactionPersisted() {
    TransactionDto transactionDto = TransactionFixture
        .newTransaction()
        .shipment(ShipmentTestFixture.newShipment().build())
        .agentRoles(new ArrayList<AgentRoles>(List.of(
          AgentRoles.builder()
              .agent(UUID.randomUUID())
              .roles(new ArrayList<String>(List.of("Role1", "Role2")))
              .build(),
          AgentRoles.builder()
              .agent(UUID.randomUUID())
              .roles(new ArrayList<String>(List.of("Role3")))
              .build()
        )))
        .build();

    TransactionDto createdTransaction = transactionRepository.create(transactionDto);
    assertNotNull(createdTransaction.getCreatedOn());

    TransactionDto result = transactionRepository.findOne(createdTransaction.getUuid(), new QuerySpec(TransactionDto.class));
    assertEquals(createdTransaction.getUuid(), result.getUuid());
    assertEquals("user", result.getCreatedBy());

    // Test roles.
    assertEquals(2, result.getInvolvedAgents().size());
    assertEquals("Role1", result.getAgentRoles().get(0).getRoles().get(0));
    assertEquals("Role2", result.getAgentRoles().get(0).getRoles().get(1));
    assertEquals("Role3", result.getAgentRoles().get(1).getRoles().get(0));

    assertEquals(ShipmentTestFixture.CURRENCY, result.getShipment().getCurrency());

    // cleanup
    transactionRepository.delete(createdTransaction.getUuid());
  }

  @Test
  @WithMockKeycloakUser(username = "user", groupRole = "wronggroup:STAFF")
  public void create_onWrongGroup_accessDenied() {
    TransactionDto transactionDto = TransactionFixture.newTransaction().build();
    Assertions
        .assertThrows(AccessDeniedException.class,
            () -> transactionRepository.create(transactionDto));
  }

  @Test
  @WithMockKeycloakUser(username = "user", groupRole = TransactionFixture.GROUP + ":COLLECTION_MANAGER")
  public void save_onUpdateData_FieldsUpdated() {
    final String updatedTransactionNumber = "Updated T2";
    TransactionDto transactionDto = TransactionFixture.newTransaction().build();
    TransactionDto createdTransaction = transactionRepository.create(transactionDto);

    createdTransaction.setTransactionNumber(updatedTransactionNumber);
    transactionRepository.save(createdTransaction);

    TransactionDto loadedTransaction = transactionRepository.findOne(createdTransaction.getUuid(), new QuerySpec(TransactionDto.class));
    assertEquals(updatedTransactionNumber, loadedTransaction.getTransactionNumber());

    // cleanup
    transactionRepository.delete(createdTransaction.getUuid());
  }

}
