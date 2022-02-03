package ca.gc.aafc.transaction.api.service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.transaction.api.entities.Transaction;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import java.util.UUID;

@Service
public class TransactionService extends DefaultDinaService<Transaction> {

  public TransactionService(
    @NonNull BaseDAO baseDAO, 
    @NonNull SmartValidator smartValidator
  ) {
    super(baseDAO, smartValidator);
  }

  @Override
  protected void preCreate(Transaction entity) {
    //Give new Transaction UUID
    entity.setUuid(UUID.randomUUID());
  }

}
