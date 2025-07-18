package ca.gc.aafc.transaction.api.service;

import java.util.UUID;

import ca.gc.aafc.dina.messaging.DinaEventPublisher;
import ca.gc.aafc.dina.messaging.EntityChanged;
import ca.gc.aafc.dina.service.MessageProducingService;
import ca.gc.aafc.transaction.api.dto.TransactionDto;
import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.transaction.api.entities.Transaction;
import ca.gc.aafc.transaction.api.validation.TransactionManagedAttributeValueValidator;
import ca.gc.aafc.transaction.api.validation.TransactionValidator;

import lombok.NonNull;

@Service
public class TransactionService extends MessageProducingService<Transaction> {

  private final TransactionValidator transactionValidator;
  private final TransactionManagedAttributeValueValidator transactionManagedAttributeValueValidator;

  public TransactionService(
    @NonNull BaseDAO baseDAO, 
    @NonNull SmartValidator smartValidator,
    @NonNull TransactionValidator transactionValidator,
    @NonNull TransactionManagedAttributeValueValidator transactionManagedAttributeValueValidator,
    DinaEventPublisher<EntityChanged> eventPublisher
  ) {
    super(baseDAO, smartValidator, TransactionDto.TYPENAME, eventPublisher);
    this.transactionValidator = transactionValidator;
    this.transactionManagedAttributeValueValidator = transactionManagedAttributeValueValidator;
  }

  @Override
  protected void preCreate(Transaction entity) {
    //Give new Transaction UUID
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  public void validateBusinessRules(Transaction entity) {
    transactionManagedAttributeValueValidator.validate(entity, entity.getManagedAttributes());
    applyBusinessRule(entity, transactionValidator);
    super.validateBusinessRules(entity);
  }

}
