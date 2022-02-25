package ca.gc.aafc.transaction.api.validation;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ca.gc.aafc.dina.i18n.MultilingualDescription;
import ca.gc.aafc.dina.i18n.MultilingualDescription.MultilingualPair;
import ca.gc.aafc.transaction.api.entities.TransactionManagedAttribute;

@Component
public class TransactionManagedAttributeValidator implements Validator {

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

    // Retrieve descriptions if possible.
    List<MultilingualPair> descriptions = Optional.ofNullable(ma.getMultilingualDescription())
        .map(MultilingualDescription::getDescriptions)
        .orElseGet(Collections::emptyList);

    if (CollectionUtils.isEmpty(descriptions) ||
        descriptions.stream().anyMatch(p -> StringUtils.isBlank(p.getDesc()))) {
      String errorMessage = messageSource.getMessage("description.isEmpty", null, LocaleContextHolder.getLocale());
      errors.rejectValue("multilingualDescription", "description.isEmpty", null, errorMessage);
    }
  }
}
