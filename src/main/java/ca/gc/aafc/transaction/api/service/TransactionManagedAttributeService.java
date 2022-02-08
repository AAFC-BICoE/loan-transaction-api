package ca.gc.aafc.transaction.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.ManagedAttributeService;
import ca.gc.aafc.dina.service.PostgresJsonbService;
import ca.gc.aafc.transaction.api.entities.TransactionManagedAttribute;
import ca.gc.aafc.transaction.api.validation.TransactionManagedAttributeValidator;

import lombok.NonNull;

@Service
public class TransactionManagedAttributeService extends ManagedAttributeService<TransactionManagedAttribute> {

  public static final String METADATA_TABLE_NAME = "metadata";
  public static final String MANAGED_ATTRIBUTES_COL_NAME = "managed_attribute_values";

  private final TransactionManagedAttributeValidator managedAttributeValidator;
  private final PostgresJsonbService jsonbService;

  public TransactionManagedAttributeService(
    @NonNull BaseDAO baseDAO,
    @NonNull TransactionManagedAttributeValidator managedAttributeValidator,
    SmartValidator smartValidator,
    @NonNull PostgresJsonbService postgresJsonbService
  ) {
    super(baseDAO, smartValidator, TransactionManagedAttribute.class);
    this.managedAttributeValidator = managedAttributeValidator;
    this.jsonbService = postgresJsonbService;
  }

  @Override
  protected void preCreate(TransactionManagedAttribute entity) {
    entity.setUuid(UUID.randomUUID());
    super.preCreate(entity);
  }

  @Override
  protected void preDelete(TransactionManagedAttribute entity) {
    checkKeysFor(entity.getKey());
  }

  @Override
  public void validateBusinessRules(TransactionManagedAttribute entity) {
    applyBusinessRule(entity, managedAttributeValidator);
  }

  private void checkKeysFor(String key) {
    Integer countFirstLevelKeys = jsonbService.countFirstLevelKeys(
      TransactionManagedAttributeService.METADATA_TABLE_NAME, TransactionManagedAttributeService.MANAGED_ATTRIBUTES_COL_NAME, key);
    if (countFirstLevelKeys > 0) {
      throw new IllegalStateException("Managed attribute key: " + key + ", is currently in use.");
    }
  }
}
