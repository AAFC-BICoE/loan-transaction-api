package ca.gc.aafc.transaction.api.testsupport.fixtures;

import org.apache.commons.lang3.RandomStringUtils;

import ca.gc.aafc.dina.entity.ManagedAttribute.ManagedAttributeType;
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
        .managedAttributeType(ManagedAttributeType.INTEGER)
        .createdBy("Created By")
        .acceptedValues(new String[] {})
        .multilingualDescription(MultilingualDescriptionFactory.newMultilingualDescription().build());
  }
}
