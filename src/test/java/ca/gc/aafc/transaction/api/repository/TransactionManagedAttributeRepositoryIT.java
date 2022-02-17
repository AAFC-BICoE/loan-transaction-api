package ca.gc.aafc.transaction.api.repository;

import java.util.UUID;
import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;

import ca.gc.aafc.dina.entity.ManagedAttribute.ManagedAttributeType;
import ca.gc.aafc.dina.testsupport.security.WithMockKeycloakUser;
import ca.gc.aafc.transaction.api.BaseIntegrationTest;
import ca.gc.aafc.transaction.api.dto.TransactionManagedAttributeDto;
import ca.gc.aafc.transaction.api.testsupport.factories.MultilingualDescriptionFactory;

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
  @WithMockKeycloakUser(groupRole = VALID_GROUP + ":COLLECTION_MANAGER", username = USER_NAME)
  public void findManagedAttribute_whenNoFieldsAreSelected_manageAttributeReturnedWithAllFields() {
    TransactionManagedAttributeDto testManagedAttribute = TransactionManagedAttributeDto.builder()
        .group(VALID_GROUP)
        .name("name")
        .acceptedValues(new String[] { "dosal" })
        .managedAttributeType(ManagedAttributeType.STRING)
        .multilingualDescription(MultilingualDescriptionFactory.newMultilingualDescription().build())
        .build();
    UUID managedResourceUUID = managedResourceRepository.create(testManagedAttribute).getUuid();

    TransactionManagedAttributeDto managedAttributeDto = managedResourceRepository
        .findOne(managedResourceUUID, new QuerySpec(TransactionManagedAttributeDto.class));

    assertNotNull(managedAttributeDto);
    assertEquals(managedResourceUUID, managedAttributeDto.getUuid());
    assertArrayEquals(testManagedAttribute.getAcceptedValues(),
        managedAttributeDto.getAcceptedValues());
    assertEquals(testManagedAttribute.getManagedAttributeType(),
        managedAttributeDto.getManagedAttributeType());
    assertEquals(testManagedAttribute.getName(), managedAttributeDto.getName());
    assertEquals(testManagedAttribute.getMultilingualDescription().getDescriptions().get(0),
        managedAttributeDto.getMultilingualDescription().getDescriptions().get(0));
  }

  @Test
  @WithMockKeycloakUser(groupRole = VALID_GROUP + ":COLLECTION_MANAGER", username = USER_NAME)
  public void create_WithAuthenticatedUser_SetsCreatedBy() {
    TransactionManagedAttributeDto ma = TransactionManagedAttributeDto.builder()
      .uuid(UUID.randomUUID())
      .name("name")
      .group(VALID_GROUP)
      .managedAttributeType(ManagedAttributeType.STRING)
      .acceptedValues(new String[] { "dosal" })
      .multilingualDescription(MultilingualDescriptionFactory.newMultilingualDescription().build())
      .build();

    TransactionManagedAttributeDto result = managedResourceRepository.findOne(
      managedResourceRepository.create(ma).getUuid(),
      new QuerySpec(TransactionManagedAttributeDto.class));
    assertEquals(USER_NAME, result.getCreatedBy());
  }

  @Test
  @WithMockKeycloakUser(groupRole = VALID_GROUP + ":COLLECTION_MANAGER", username = USER_NAME)
  public void update_WithIncorrectGroup_AccessDeniedException() {
    TransactionManagedAttributeDto ma = TransactionManagedAttributeDto.builder()
      .uuid(UUID.randomUUID())
      .name("name")
      .group(INVALID_GROUP)
      .managedAttributeType(ManagedAttributeType.STRING)
      .acceptedValues(new String[] { "dosal" })
      .multilingualDescription(MultilingualDescriptionFactory.newMultilingualDescription().build())
      .build();

    assertThrows(AccessDeniedException.class, () -> managedResourceRepository.create(ma));
  }


  @Test
  @WithMockKeycloakUser(groupRole = VALID_GROUP + ":COLLECTION_MANAGER", username = USER_NAME)
  void findOneByKey_whenKeyProvided_managedAttributeFetched() {
    TransactionManagedAttributeDto newAttribute = TransactionManagedAttributeDto.builder()
      .name("Object Store Attribute 1")
      .group(VALID_GROUP)
      .managedAttributeType(ManagedAttributeType.INTEGER)
      .createdBy("poffm")
      .multilingualDescription(MultilingualDescriptionFactory.newMultilingualDescription().build())
      .build();

    UUID newAttributeUuid = managedResourceRepository.create(newAttribute).getUuid();

    QuerySpec querySpec = new QuerySpec(TransactionManagedAttributeDto.class);

    // Fetch using the key instead of the UUID:
    TransactionManagedAttributeDto fetchedAttribute = managedResourceRepository
      .findOne("object_store_attribute_1", querySpec);

    assertEquals(newAttributeUuid, fetchedAttribute.getUuid());
  }
    
}
