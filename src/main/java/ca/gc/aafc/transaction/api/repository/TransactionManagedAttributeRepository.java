package ca.gc.aafc.transaction.api.repository;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.security.DinaAuthenticatedUser;
import ca.gc.aafc.transaction.api.dto.TransactionManagedAttributeDto;
import ca.gc.aafc.transaction.api.entities.TransactionManagedAttribute;
import ca.gc.aafc.transaction.api.security.TransactionManagedAttributeAuthorizationService;
import ca.gc.aafc.transaction.api.service.TransactionManagedAttributeService;

import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;
import lombok.NonNull;

@Repository
public class TransactionManagedAttributeRepository
  extends DinaRepository<TransactionManagedAttributeDto, TransactionManagedAttribute> {

  private final Optional<DinaAuthenticatedUser> dinaAuthenticatedUser;
  private final TransactionManagedAttributeService dinaService;

  public TransactionManagedAttributeRepository(
    @NonNull TransactionManagedAttributeService dinaService,
    @NonNull TransactionManagedAttributeAuthorizationService authorizationService,
    Optional<DinaAuthenticatedUser> dinaAuthenticatedUser,
    @NonNull BuildProperties props
  ) {
    super(
      dinaService,
      authorizationService,
      Optional.empty(),
      new DinaMapper<>(TransactionManagedAttributeDto.class),
      TransactionManagedAttributeDto.class,
      TransactionManagedAttribute.class, 
      null, 
      null,
      props);
    this.dinaAuthenticatedUser = dinaAuthenticatedUser;
    this.dinaService = dinaService;
  }

  @Override
  public <S extends TransactionManagedAttributeDto> S create(S resource) {
    dinaAuthenticatedUser.ifPresent(user -> resource.setCreatedBy(user.getUsername()));
    return super.create(resource);
  }

  @Override
  public TransactionManagedAttributeDto findOne(Serializable id, QuerySpec querySpec) {
    boolean idIsUuid = true;
    try {
      UUID.fromString(id.toString());
    } catch (IllegalArgumentException exception) {
      idIsUuid = false;
    }
    
    // Try finding by UUID:
    if (idIsUuid) {
      return super.findOne(id, querySpec);
    }

    // Otherwise try a lookup by the managed attribute key.
    // e.g. GET /api/v1/managed-attribute/test-managed-attribute
    TransactionManagedAttribute managedAttribute = dinaService.findOneByKey(id.toString());

    if (managedAttribute != null) {
      return getMappingLayer().toDtoSimpleMapping(managedAttribute);
    } else {
      throw new ResourceNotFoundException("Managed Attribute not found: " + id);
    }
  }

}
