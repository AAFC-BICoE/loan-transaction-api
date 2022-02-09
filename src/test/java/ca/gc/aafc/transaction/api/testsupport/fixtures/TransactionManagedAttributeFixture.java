package ca.gc.aafc.transaction.api.testsupport.fixtures;

import org.apache.commons.lang3.RandomStringUtils;

import ca.gc.aafc.dina.entity.ManagedAttribute.ManagedAttributeType;
import ca.gc.aafc.transaction.api.dto.TransactionManagedAttributeDto;
import ca.gc.aafc.transaction.api.testsupport.factories.MultilingualDescriptionFactory;

public final class TransactionManagedAttributeFixture {

  private TransactionManagedAttributeFixture() {
  }

  public static TransactionManagedAttributeDto.TransactionManagedAttributeDtoBuilder newTransactionManagedAttribute() {
    return TransactionManagedAttributeDto.builder()
        .name(RandomStringUtils.randomAlphabetic(5))
        .managedAttributeType(ManagedAttributeType.INTEGER)
        .createdBy("Created By")
        .acceptedValues(new String[] {})
        .multilingualDescription(MultilingualDescriptionFactory.newMultilingualDescription().build());
  }
}
