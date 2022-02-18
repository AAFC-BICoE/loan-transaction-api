package ca.gc.aafc.transaction.api.openapi;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.UUID;
import javax.inject.Inject;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import ca.gc.aafc.dina.testsupport.BaseRestAssuredTest;
import ca.gc.aafc.dina.testsupport.DatabaseSupportService;
import ca.gc.aafc.dina.testsupport.PostgresTestContainerInitializer;
import ca.gc.aafc.dina.testsupport.TransactionTestingHelper;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.dina.testsupport.specs.OpenAPI3Assertions;
import ca.gc.aafc.transaction.api.TransactionModuleApiLauncher;
import ca.gc.aafc.transaction.api.dto.TransactionManagedAttributeDto;
import ca.gc.aafc.transaction.api.entities.TransactionManagedAttribute;
import ca.gc.aafc.transaction.api.testsupport.fixtures.TransactionManagedAttributeFixture;

import io.restassured.response.ValidatableResponse;
import lombok.SneakyThrows;

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = TransactionModuleApiLauncher.class
)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
@ContextConfiguration(initializers = PostgresTestContainerInitializer.class)
public class TransactionManagedAttributeOpenApiIT extends BaseRestAssuredTest {

  public static final String API_BASE_PATH = "/api/v1/managed-attribute/";
  private static final String SCHEMA_NAME = "ManagedAttribute";

  @Inject
  private DatabaseSupportService databaseSupportService;

  @Inject
  private TransactionTestingHelper transactionTestingHelper;

  @SneakyThrows({MalformedURLException.class, URISyntaxException.class})
  protected TransactionManagedAttributeOpenApiIT() {
    super(API_BASE_PATH);
  }

  @Test
  @SneakyThrows
  void managedAttribute_SpecValid() {
    // Create a new transaction managed attribute.
    ValidatableResponse response = sendPost(
      "",
      JsonAPITestHelper.toJsonAPIMap(
        TransactionManagedAttributeDto.TYPENAME,
        TransactionManagedAttributeFixture.newTransactionManagedAttribute().build()
      )
    );

    // Ensure the id was created.
    response.body("data.id", Matchers.notNullValue());

    // Validate the specifications.
    OpenAPI3Assertions.assertRemoteSchema(OpenAPIConstants.TRANSACTION_API_SPECS_URL,
        SCHEMA_NAME, response.extract().asString());

    // Cleanup:
    UUID uuid = response.extract().jsonPath().getUUID("data.id");
    transactionTestingHelper.doInTransactionWithoutResult(
        transaction -> databaseSupportService.deleteByProperty(TransactionManagedAttribute.class, "uuid", uuid));
  }
}
