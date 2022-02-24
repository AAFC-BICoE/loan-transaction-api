package ca.gc.aafc.transaction.api.util;

import java.util.UUID;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import ca.gc.aafc.dina.testsupport.BaseRestAssuredTest;
import ca.gc.aafc.dina.testsupport.PostgresTestContainerInitializer;
import ca.gc.aafc.transaction.api.TransactionModuleApiLauncher;
import ca.gc.aafc.transaction.api.dto.TransactionManagedAttributeDto;
import ca.gc.aafc.transaction.api.repository.TransactionManagedAttributeRepository;
import ca.gc.aafc.transaction.api.testsupport.fixtures.TransactionManagedAttributeFixture;
import io.restassured.response.ValidatableResponse;

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = TransactionModuleApiLauncher.class
)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
@ContextConfiguration(initializers = PostgresTestContainerInitializer.class)
public class ManagedAttributeIdMapperIT extends BaseRestAssuredTest {
  
  @Inject
  private TransactionManagedAttributeRepository managedResourceRepository;

  public static final String API_BASE_PATH = "/api/v1/managed-attribute/";

  protected ManagedAttributeIdMapperIT() {
    super(API_BASE_PATH);
  }

  @Test
  void findOneByKey_whenKeyProvided_managedAttributeFetched() {
    TransactionManagedAttributeDto newAttribute = TransactionManagedAttributeFixture.newTransactionManagedAttribute()
      .name("Transaction Managed Attribute 1")
      .build();

    // Create a new transaction managed attribute.
    UUID newAttributeUuid = managedResourceRepository.create(newAttribute).getUuid();

    // This will test it from the crnk API level instead of the repository level.
    ValidatableResponse response = sendGet("transaction_managed_attribute_1");
    Assertions.assertEquals(newAttributeUuid, response.extract().jsonPath().getUUID("data.id"));
  }

}
