package ca.gc.aafc.transaction.api.service;

import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.i18n.MultilingualDescription;
import ca.gc.aafc.dina.vocabulary.TypedVocabularyElement;
import ca.gc.aafc.transaction.api.BaseIntegrationTest;
import ca.gc.aafc.transaction.api.entities.TransactionManagedAttribute;
import ca.gc.aafc.transaction.api.testsupport.factories.MultilingualDescriptionFactory;
import ca.gc.aafc.transaction.api.testsupport.factories.TransactionManagedAttributeFactory;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
public class TransactionManagedAttributeCRUDIT extends BaseIntegrationTest {

  private TransactionManagedAttribute buildTransactionManagedAttribute() {
    return TransactionManagedAttributeFactory.newManagedAttribute()
      .group("group")
      .acceptedValues(new String[] { "a", "b" })
      .multilingualDescription(MultilingualDescriptionFactory.newMultilingualDescription().build())
      .createdBy("createdBy")
      .build();
  }
      
  @Test
  public void testSave() {
    TransactionManagedAttribute managedAttributeUnderTest = buildTransactionManagedAttribute();
    assertNull(managedAttributeUnderTest.getId());
    managedAttributeService.create(managedAttributeUnderTest);
    assertNotNull(managedAttributeUnderTest.getId());
  }

  @Test
  public void testSaveAllTypes() {
    for(var type : TypedVocabularyElement.VocabularyElementType.values()) {
      managedAttributeService.create(TransactionManagedAttributeFactory.newManagedAttribute()
        .vocabularyElementType(type)
        .build());
    }
  }

  @Test
  public void testSave_whenDescriptionIsBlank_throwValidationException() {
    TransactionManagedAttribute blankDescription = TransactionManagedAttributeFactory.newManagedAttribute()
      .acceptedValues(new String[] { "a", "b" })
      .multilingualDescription(MultilingualDescription.builder()
          .descriptions(List.of(MultilingualDescription.MultilingualPair.of("en", "")))
          .build())
      .build();

    assertThrows(
      ValidationException.class,
      () -> managedAttributeService.create(blankDescription));
  }

  @Test
  public void testSave_whenDescriptionsIsNull_throwValidationException() {
    TransactionManagedAttribute nullDescription = TransactionManagedAttributeFactory.newManagedAttribute()
      .acceptedValues(new String[] { "a", "b" })
      .multilingualDescription(MultilingualDescription.builder()
          .descriptions(null)
          .build())
      .build();

    assertThrows(
      ValidationException.class,
      () -> managedAttributeService.create(nullDescription));
  }

  @Test
  public void testFind() {
    TransactionManagedAttribute managedAttributeUnderTest = buildTransactionManagedAttribute();
    managedAttributeService.create(managedAttributeUnderTest);

    TransactionManagedAttribute fetchedManagedAttribute = managedAttributeService.findOne(
      managedAttributeUnderTest.getUuid(), TransactionManagedAttribute.class);
    assertEquals(managedAttributeUnderTest.getId(), fetchedManagedAttribute.getId());

    assertArrayEquals(new String[] { "a", "b" }, managedAttributeUnderTest.getAcceptedValues());

    assertEquals("attrFr", managedAttributeUnderTest.getMultilingualDescription().getDescriptions().stream().filter(p -> p.getLang().equals("fr")).findAny().get().getDesc());
    assertEquals(managedAttributeUnderTest.getCreatedBy(), fetchedManagedAttribute.getCreatedBy());
    assertNotNull(fetchedManagedAttribute.getCreatedOn());
  }

  @Test
  public void testRemove() {
    TransactionManagedAttribute managedAttributeUnderTest = buildTransactionManagedAttribute();
    managedAttributeService.create(managedAttributeUnderTest);

    UUID uuid = managedAttributeUnderTest.getUuid();
    managedAttributeService.delete(managedAttributeUnderTest);
    assertNull(managedAttributeService.findOne(
      uuid, TransactionManagedAttribute.class));
  }
}
