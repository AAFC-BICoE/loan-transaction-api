package ca.gc.aafc.transaction.api.repository;

import java.util.UUID;
import javax.inject.Inject;

import ca.gc.aafc.dina.vocabulary.TypedVocabularyElement;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;

import ca.gc.aafc.dina.testsupport.security.WithMockKeycloakUser;
import ca.gc.aafc.transaction.api.BaseIntegrationTest;
import ca.gc.aafc.transaction.api.dto.TransactionManagedAttributeDto;
import ca.gc.aafc.transaction.api.testsupport.fixtures.TransactionManagedAttributeFixture;
import io.crnk.core.queryspec.QuerySpec;
import lombok.SneakyThrows;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = "keycloak.enabled=true")
public class TransactionManagedAttributeRepositoryIT extends BaseIntegrationTest {
  
  @Inject
  private TransactionManagedAttributeRepository managedResourceRepository;

  private static final String VALID_GROUP = "validGroup";
  private static final String INVALID_GROUP = "invalidGroup";
  private static final String USER_NAME = "testUser";

  @Test
  @SneakyThrows
  @WithMockKeycloakUser(groupRole = VALID_GROUP + ":SUPER_USER", username = USER_NAME)
  public void findManagedAttribute_whenNoFieldsAreSelected_manageAttributeReturnedWithAllFields() {
    TransactionManagedAttributeDto testManagedAttribute = TransactionManagedAttributeFixture.newTransactionManagedAttribute()
        .group(VALID_GROUP)
        .acceptedValues(new String[] { "dosal" })
        .vocabularyElementType(TypedVocabularyElement.VocabularyElementType.STRING)
        .build();
    UUID managedResourceUUID = managedResourceRepository.create(testManagedAttribute).getUuid();

    TransactionManagedAttributeDto managedAttributeDto = managedResourceRepository
        .findOne(managedResourceUUID, new QuerySpec(TransactionManagedAttributeDto.class));

    assertNotNull(managedAttributeDto);
    assertEquals(managedResourceUUID, managedAttributeDto.getUuid());
    assertArrayEquals(testManagedAttribute.getAcceptedValues(),
        managedAttributeDto.getAcceptedValues());
    assertEquals(testManagedAttribute.getVocabularyElementType(),
        managedAttributeDto.getVocabularyElementType());
    assertEquals(testManagedAttribute.getName(), managedAttributeDto.getName());
    assertEquals(testManagedAttribute.getMultilingualDescription().getDescriptions().get(0),
        managedAttributeDto.getMultilingualDescription().getDescriptions().get(0));
  }

  @Test
  @WithMockKeycloakUser(groupRole = VALID_GROUP + ":SUPER_USER", username = USER_NAME)
  public void create_WithAuthenticatedUser_SetsCreatedBy() {
    TransactionManagedAttributeDto ma = TransactionManagedAttributeFixture.newTransactionManagedAttribute()
      .group(VALID_GROUP)
      .build();

    TransactionManagedAttributeDto result = managedResourceRepository.findOne(
      managedResourceRepository.create(ma).getUuid(),
      new QuerySpec(TransactionManagedAttributeDto.class));
    assertEquals(USER_NAME, result.getCreatedBy());
  }

  @Test
  @WithMockKeycloakUser(groupRole = VALID_GROUP + ":SUPER_USER", username = USER_NAME)
  public void update_WithIncorrectGroup_AccessDeniedException() {
    TransactionManagedAttributeDto ma = TransactionManagedAttributeFixture.newTransactionManagedAttribute()
      .group(INVALID_GROUP)
      .vocabularyElementType(TypedVocabularyElement.VocabularyElementType.STRING)
      .acceptedValues(new String[] { "dosal" })
      .build();

    assertThrows(AccessDeniedException.class, () -> managedResourceRepository.create(ma));
  }
}
