package ca.gc.aafc.transaction.api.validation;

import javax.inject.Named;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import ca.gc.aafc.dina.service.ManagedAttributeService;
import ca.gc.aafc.dina.validation.ManagedAttributeValueValidator;
import ca.gc.aafc.transaction.api.entities.TransactionManagedAttribute;

import lombok.NonNull;

@Component
public class TransactionManagedAttributeValueValidator extends ManagedAttributeValueValidator<TransactionManagedAttribute> {

  public TransactionManagedAttributeValueValidator(
    @Named("validationMessageSource") MessageSource baseMessageSource, // from dina-base
    @NonNull ManagedAttributeService<TransactionManagedAttribute> dinaService
  ) {
    super(baseMessageSource, dinaService);
  }
  
}
