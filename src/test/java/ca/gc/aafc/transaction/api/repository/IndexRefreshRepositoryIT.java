package ca.gc.aafc.transaction.api.repository;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.messaging.message.DocumentOperationNotification;
import ca.gc.aafc.dina.messaging.producer.DocumentOperationNotificationMessageProducer;
import ca.gc.aafc.dina.security.auth.DinaAdminCUDAuthorizationService;
import ca.gc.aafc.transaction.api.BaseIntegrationTest;
import ca.gc.aafc.transaction.api.dto.IndexRefreshDto;
import ca.gc.aafc.transaction.api.dto.TransactionDto;
import ca.gc.aafc.transaction.api.entities.Transaction;
import ca.gc.aafc.transaction.api.service.IndexRefreshService;
import ca.gc.aafc.transaction.api.testsupport.factories.TransactionFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class IndexRefreshRepositoryIT extends BaseIntegrationTest {

  @Inject
  private BaseDAO baseDAO;

  @Inject
  private DinaAdminCUDAuthorizationService dinaAdminCUDAuthorizationService;

  @Test
  @Transactional
  public void indexRefreshRepository_onRefreshAll_messageSent() {
    // we are not using beans to avoid the RabbitMQ part
    List<DocumentOperationNotification> messages = new ArrayList<>();
    DocumentOperationNotificationMessageProducer messageProducer = messages::add;
    IndexRefreshService service = new IndexRefreshService(messageProducer, baseDAO);
    IndexRefreshRepository repo = new IndexRefreshRepository(dinaAdminCUDAuthorizationService, service);

    Transaction transaction = TransactionFactory.newTransaction().build();
    transactionService.create(transaction);

    IndexRefreshDto dto = new IndexRefreshDto();
    dto.setDocType(TransactionDto.TYPENAME);
    repo.handlePost(EntityModel.of(dto));

    // we may get more than 1 message if the database includes records from other tests
    assertFalse(messages.isEmpty());
  }
}
