package ca.gc.aafc.transaction.api.testsupport.fixtures;

import ca.gc.aafc.dina.vocabulary.TypedVocabularyElement;
import org.apache.commons.lang3.RandomStringUtils;

import ca.gc.aafc.transaction.api.dto.TransactionManagedAttributeDto;
import ca.gc.aafc.transaction.api.testsupport.factories.MultilingualDescriptionFactory;

public final class TransactionManagedAttributeFixture {

  public static final String GROUP = "group 1";

  private TransactionManagedAttributeFixture() {
  }

  public static TransactionManagedAttributeDto.TransactionManagedAttributeDtoBuilder newTransactionManagedAttribute() {
    return TransactionManagedAttributeDto.builder()
        .name(RandomStringUtils.randomAlphabetic(5))
        .group(GROUP)
        .vocabularyElementType(TypedVocabularyElement.VocabularyElementType.INTEGER)
        .createdBy("Created By")
        .acceptedValues(new String[] {})
        .multilingualDescription(MultilingualDescriptionFactory.newMultilingualDescription().build());
  }
}
