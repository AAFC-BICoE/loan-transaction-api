package ca.gc.aafc.transaction.api.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.testsupport.security.WithMockKeycloakUser;
import ca.gc.aafc.dina.vocabulary.TypedVocabularyElement;
import ca.gc.aafc.transaction.api.BaseIntegrationTest;
import ca.gc.aafc.transaction.api.dto.TransactionDto;
import ca.gc.aafc.transaction.api.dto.TransactionManagedAttributeDto;
import ca.gc.aafc.transaction.api.entities.AgentRoles;
import ca.gc.aafc.transaction.api.testsupport.fixtures.ShipmentTestFixture;
import ca.gc.aafc.transaction.api.testsupport.fixtures.TransactionFixture;
import ca.gc.aafc.transaction.api.testsupport.fixtures.TransactionManagedAttributeFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.crnk.core.queryspec.QuerySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import javax.validation.ValidationException;

@SpringBootTest(properties = "keycloak.enabled: true")
public class TransactionResourceRepositoryIT extends BaseIntegrationTest {

  @Inject
  private TransactionRepository transactionRepository;

  @Inject
  private TransactionManagedAttributeRepository managedResourceRepository;

  @WithMockKeycloakUser(username = "user", groupRole = TransactionFixture.GROUP + ":USER")
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
  @WithMockKeycloakUser(username = "user", groupRole = "wronggroup:USER")
  public void create_onWrongGroup_accessDenied() {
    TransactionDto transactionDto = TransactionFixture.newTransaction().build();
    Assertions
        .assertThrows(AccessDeniedException.class,
            () -> transactionRepository.create(transactionDto));
  }

  @Test
  @WithMockKeycloakUser(username = "user", groupRole = TransactionFixture.GROUP + ":SUPER_USER")
  @Transactional
  public void create_onManagedAttributeValue_validationOccur() {
    // Create the managed attribute for bool
    TransactionManagedAttributeDto testManagedAttribute = TransactionManagedAttributeFixture.newTransactionManagedAttribute()
        .group(TransactionFixture.GROUP)
        .vocabularyElementType(TypedVocabularyElement.VocabularyElementType.BOOL)
        .build();

    JsonApiDocument docToCreate =TransactionModuleBaseRepositoryIT.dtoToJsonApiDocument(testManagedAttribute);
    String key = managedResourceRepository.create(docToCreate, null).getDto().getKey();

    TransactionDto transactionDto = TransactionFixture.newTransaction()
        .managedAttributes( Map.of(key, "xyz"))
        .build();
    Assertions
        .assertThrows(ValidationException.class,
            () -> transactionRepository.create(transactionDto));

    // fix the error and retry
    transactionDto.setManagedAttributes(Map.of(key, "true"));
    transactionRepository.create(transactionDto);
  }

  @Test
  @WithMockKeycloakUser(username = "user", groupRole = TransactionFixture.GROUP + ":SUPER_USER")
  public void save_onUpdateData_FieldsUpdated() {
    final String updatedTransactionNumber = "Updated T2";
    TransactionDto transactionDto = TransactionFixture.newTransaction().build();
    TransactionDto createdTransaction = transactionRepository.create(transactionDto);

    List<AgentRoles> agentRoleUpdate = createdTransaction.getAgentRoles();
    agentRoleUpdate.get(0).setRoles(new ArrayList<>(List.of("updatedRole")));

    createdTransaction.setTransactionNumber(updatedTransactionNumber);
    createdTransaction.setAgentRoles(agentRoleUpdate);

    transactionRepository.save(createdTransaction);

    TransactionDto loadedTransaction = transactionRepository.findOne(createdTransaction.getUuid(), new QuerySpec(TransactionDto.class));
    assertEquals(updatedTransactionNumber, loadedTransaction.getTransactionNumber());
    assertEquals("updatedRole", loadedTransaction.getAgentRoles().get(0).getRoles().get(0));

    // cleanup
    transactionRepository.delete(createdTransaction.getUuid());
  }

}
