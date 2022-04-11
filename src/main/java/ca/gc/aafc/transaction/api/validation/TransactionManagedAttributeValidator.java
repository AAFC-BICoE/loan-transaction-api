package ca.gc.aafc.transaction.api.validation;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ca.gc.aafc.transaction.api.entities.TransactionManagedAttribute;

@Component
public class TransactionManagedAttributeValidator implements Validator {

  public static final String EMPTY_DESCRIPTION = "description.isEmpty";

  private final MessageSource messageSource;

  public TransactionManagedAttributeValidator(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return TransactionManagedAttribute.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    TransactionManagedAttribute ma = (TransactionManagedAttribute) target;

    // If a multilingual description is provided, ensure it's not empty.
    if (ma.getMultilingualDescription() != null) {
      if (CollectionUtils.isEmpty(ma.getMultilingualDescription().getDescriptions()) ||
          ma.getMultilingualDescription().hasBlankDescription()) {
        String errorMessage = messageSource.getMessage(EMPTY_DESCRIPTION, null, LocaleContextHolder.getLocale());
        errors.rejectValue("multilingualDescription", EMPTY_DESCRIPTION, null, errorMessage);
      }      
    }

  }
}
