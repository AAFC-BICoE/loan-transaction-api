package ca.gc.aafc.transaction.api.service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.transaction.api.entities.Transaction;
import ca.gc.aafc.transaction.api.validation.TransactionValidator;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import java.util.UUID;

@Service
public class TransactionService extends DefaultDinaService<Transaction> {

  private final TransactionValidator transactionValidator;

  public TransactionService(
    @NonNull BaseDAO baseDAO, 
    @NonNull SmartValidator smartValidator,
    @NonNull TransactionValidator transactionValidator
  ) {
    super(baseDAO, smartValidator);
    this.transactionValidator = transactionValidator;
  }

  @Override
  protected void preCreate(Transaction entity) {
    //Give new Transaction UUID
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  public void validateBusinessRules(Transaction entity) {
    applyBusinessRule(entity, transactionValidator);
    super.validateBusinessRules(entity);
  }

}
