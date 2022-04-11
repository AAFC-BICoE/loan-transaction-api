package ca.gc.aafc.transaction.api.validation;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;

import ca.gc.aafc.dina.i18n.MultilingualDescription;
import ca.gc.aafc.dina.i18n.MultilingualDescription.MultilingualPair;
import ca.gc.aafc.dina.validation.ValidationErrorsHelper;
import ca.gc.aafc.transaction.api.BaseIntegrationTest;
import ca.gc.aafc.transaction.api.entities.TransactionManagedAttribute;
import ca.gc.aafc.transaction.api.testsupport.factories.TransactionManagedAttributeFactory;

public class TransactionManagedAttributeValidatorTest extends BaseIntegrationTest {
  
  @Inject
  private TransactionManagedAttributeValidator transactionManagedAttributeValidator;

  @Inject
  private MessageSource messageSource;

  @Test
  void validate_NoValidationException_ValidationSuccess() {
    TransactionManagedAttribute transactionManagedAttribute = TransactionManagedAttributeFactory.newManagedAttribute()
        .multilingualDescription(null) // Description can be not set.
        .build();

    Errors errors = ValidationErrorsHelper.newErrorsObject(transactionManagedAttribute);
    transactionManagedAttributeValidator.validate(transactionManagedAttribute, errors);
    Assertions.assertEquals(0, errors.getAllErrors().size());
  }

  @Test
  void validate_withNullOrEmptyDescription_ValidationSuccess() {
    String expectedErrorMessage = getExpectedErrorMessage(TransactionManagedAttributeValidator.EMPTY_DESCRIPTION);

    // Create a multilingual description that contains an empty description.
    TransactionManagedAttribute emptyDescription = TransactionManagedAttributeFactory.newManagedAttribute()
        .multilingualDescription(MultilingualDescription.builder().descriptions(List.of(
          MultilingualPair.of("en", "Filled in description"),
          MultilingualPair.of("en", "")))
        .build()
    )
    .build();

    Errors errors = ValidationErrorsHelper.newErrorsObject(emptyDescription);
    transactionManagedAttributeValidator.validate(emptyDescription, errors);
    Assertions.assertEquals(1, errors.getAllErrors().size());
    Assertions.assertEquals(expectedErrorMessage, errors.getAllErrors().get(0).getDefaultMessage());
  }

  private String getExpectedErrorMessage(String key) {
    return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
  }

}
