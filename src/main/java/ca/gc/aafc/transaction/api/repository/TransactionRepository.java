package ca.gc.aafc.transaction.api.repository;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.DinaAuthenticatedUser;
import ca.gc.aafc.dina.security.auth.GroupAuthorizationService;
import ca.gc.aafc.dina.service.AuditService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.transaction.api.dto.TransactionDto;
import ca.gc.aafc.transaction.api.entities.Transaction;

import lombok.NonNull;

@Repository
public class TransactionRepository extends DinaRepository<TransactionDto, Transaction> {

  private Optional<DinaAuthenticatedUser> dinaAuthenticatedUser;

  public TransactionRepository(
    @NonNull DinaService<Transaction> dinaService,
    @NonNull GroupAuthorizationService authorizationService,
    Optional<DinaAuthenticatedUser> dinaAuthenticatedUser,
    @NonNull BuildProperties props,
    @NonNull AuditService auditService,
    @NonNull ExternalResourceProvider externalResourceProvider,
    @NonNull ObjectMapper objMapper
  ) {
    super(
      dinaService,
      authorizationService,
      Optional.of(auditService),
      new DinaMapper<>(TransactionDto.class),
      TransactionDto.class,
        Transaction.class,
      null,
      externalResourceProvider,
      props, objMapper);
    this.dinaAuthenticatedUser = dinaAuthenticatedUser;
  }

  @Override
  public <S extends TransactionDto> S create(S resource) {
    if (dinaAuthenticatedUser.isPresent()) {
      resource.setCreatedBy(dinaAuthenticatedUser.get().getUsername());
    }
    return super.create(resource);
  }

}
