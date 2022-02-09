package ca.gc.aafc.transaction.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.transaction.api.entities.Transaction;
import ca.gc.aafc.transaction.api.validation.TransactionManagedAttributeValueValidator;

import lombok.NonNull;

@Service
public class TransactionService extends DefaultDinaService<Transaction> {

  private final TransactionManagedAttributeValueValidator transactionManagedAttributeValueValidator;

  public TransactionService(
    @NonNull BaseDAO baseDAO, 
    @NonNull SmartValidator smartValidator,
    @NonNull TransactionManagedAttributeValueValidator transactionManagedAttributeValueValidator
  ) {
    super(baseDAO, smartValidator);
    this.transactionManagedAttributeValueValidator = transactionManagedAttributeValueValidator;
  }

  @Override
  protected void preCreate(Transaction entity) {
    //Give new Transaction UUID
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  public void validateBusinessRules(Transaction entity) {
    transactionManagedAttributeValueValidator.validate(entity, entity.getManagedAttributeValues());
  }

}
