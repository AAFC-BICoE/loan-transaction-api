package ca.gc.aafc.transaction.api.repository;

import java.util.UUID;
import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import ca.gc.aafc.dina.entity.ManagedAttribute.ManagedAttributeType;
import ca.gc.aafc.transaction.api.BaseIntegrationTest;
import ca.gc.aafc.transaction.api.DinaAuthenticatedUserConfig;
import ca.gc.aafc.transaction.api.dto.TransactionManagedAttributeDto;
import ca.gc.aafc.transaction.api.entities.TransactionManagedAttribute;
import ca.gc.aafc.transaction.api.testsupport.factories.MultilingualDescriptionFactory;
import ca.gc.aafc.transaction.api.testsupport.factories.TransactionManagedAttributeFactory;

import io.crnk.core.queryspec.QuerySpec;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TransactionManagedAttributeRepositoryCRUDIT extends BaseIntegrationTest {
  
  @Inject
  private TransactionManagedAttributeRepository managedResourceRepository;
  
  private TransactionManagedAttribute testManagedAttribute;

  private static final String DINA_USER_NAME = DinaAuthenticatedUserConfig.USER_NAME;

  private TransactionManagedAttribute createTestManagedAttribute() throws JsonProcessingException {
    testManagedAttribute = TransactionManagedAttributeFactory.newManagedAttribute()
        .acceptedValues(new String[] { "dosal" })
        .multilingualDescription(MultilingualDescriptionFactory.newMultilingualDescription().build())
        .build();

    return managedAttributeService.create(testManagedAttribute);
  }
  
  @BeforeEach
  public void setup() throws JsonProcessingException { 
    createTestManagedAttribute();    
  }  

  @Test
  public void findManagedAttribute_whenNoFieldsAreSelected_manageAttributeReturnedWithAllFields() {
    TransactionManagedAttributeDto managedAttributeDto = managedResourceRepository
        .findOne(testManagedAttribute.getUuid(), new QuerySpec(TransactionManagedAttributeDto.class));
    assertNotNull(managedAttributeDto);
    assertEquals(testManagedAttribute.getUuid(), managedAttributeDto.getUuid());
    assertArrayEquals(testManagedAttribute.getAcceptedValues(),
        managedAttributeDto.getAcceptedValues());
    assertEquals(testManagedAttribute.getManagedAttributeType(),
        managedAttributeDto.getManagedAttributeType());
    assertEquals(testManagedAttribute.getName(), managedAttributeDto.getName());
    assertEquals(testManagedAttribute.getMultilingualDescription().getDescriptions().get(0),
        managedAttributeDto.getMultilingualDescription().getDescriptions().get(0));
  }

  @Test
  public void create_WithAuthenticatedUser_SetsCreatedBy() {
    TransactionManagedAttributeDto ma = new TransactionManagedAttributeDto();
    ma.setUuid(UUID.randomUUID());
    ma.setName("name");
    ma.setManagedAttributeType(ManagedAttributeType.STRING);
    ma.setAcceptedValues(new String[] { "dosal" });
    ma.setMultilingualDescription(MultilingualDescriptionFactory.newMultilingualDescription().build());

    TransactionManagedAttributeDto result = managedResourceRepository.findOne(
      managedResourceRepository.create(ma).getUuid(),
      new QuerySpec(TransactionManagedAttributeDto.class));
    assertEquals(DINA_USER_NAME, result.getCreatedBy());
  }

  @Test
  void findOneByKey_whenKeyProvided_managedAttributeFetched() {
    TransactionManagedAttributeDto newAttribute = new TransactionManagedAttributeDto();
    newAttribute.setName("Object Store Attribute 1");
    newAttribute.setManagedAttributeType(ManagedAttributeType.INTEGER);
    newAttribute.setCreatedBy("poffm");
    newAttribute.setMultilingualDescription(
      MultilingualDescriptionFactory.newMultilingualDescription().build()
      );

    UUID newAttributeUuid = managedResourceRepository.create(newAttribute).getUuid();

    QuerySpec querySpec = new QuerySpec(TransactionManagedAttributeDto.class);

    // Fetch using the key instead of the UUID:
    TransactionManagedAttributeDto fetchedAttribute = managedResourceRepository
      .findOne("object_store_attribute_1", querySpec);

    assertEquals(newAttributeUuid, fetchedAttribute.getUuid());
  }
    
}
