package ca.gc.aafc.transaction.api.openapi;

import java.util.Map;
import java.util.Set;
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
import ca.gc.aafc.dina.testsupport.specs.ValidationRestrictionOptions;
import ca.gc.aafc.transaction.api.TransactionModuleApiLauncher;
import ca.gc.aafc.transaction.api.dto.TransactionDto;
import ca.gc.aafc.transaction.api.entities.Transaction;
import ca.gc.aafc.transaction.api.testsupport.fixtures.ShipmentTestFixture;
import ca.gc.aafc.transaction.api.testsupport.fixtures.TransactionFixture;

import io.restassured.response.ValidatableResponse;

@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = TransactionModuleApiLauncher.class
)
@ContextConfiguration(initializers = PostgresTestContainerInitializer.class)
public class TransactionOpenApiIT extends BaseRestAssuredTest {

  public static final String API_BASE_PATH = "/api/v1/transaction/";
  private static final String SCHEMA_NAME = "Transaction";

  @Inject
  private DatabaseSupportService databaseSupportService;

  @Inject
  private TransactionTestingHelper transactionTestingHelper;

  protected TransactionOpenApiIT() {
    super(API_BASE_PATH);
  }

  @Test
  public void post_NewTransaction_ReturnsOkayAndBody() {
    // Generate post response.
    ValidatableResponse response = sendPost("",
        JsonAPITestHelper.toJsonAPIMap(
            TransactionDto.TYPENAME,
            JsonAPITestHelper.toAttributeMap(TransactionFixture.newTransaction()
                .attachment(null) // Will be added as a relationship.
                .materialSamples(null) // Will be added as a relationship.
                .shipment(ShipmentTestFixture.newShipment().build())
                .build()
            ),
            Map.of(
              "materialSamples", JsonAPITestHelper.generateExternalRelationList("material-sample", 1),
              "attachment", JsonAPITestHelper.generateExternalRelationList("metadata", 1)
            ),
            null
        )
    );

    // Validate the response against the specs.
    response.body("data.id", Matchers.notNullValue());
    OpenAPI3Assertions.assertRemoteSchema(OpenAPIConstants.TRANSACTION_API_SPECS_URL, SCHEMA_NAME, response.extract().asString(),
      ValidationRestrictionOptions.builder().allowableMissingFields(Set.of("involvedAgents")).build());

    // Cleanup:
    UUID uuid = response.extract().jsonPath().getUUID("data.id");
    transactionTestingHelper.doInTransactionWithoutResult(
        operation -> databaseSupportService.deleteByProperty(Transaction.class, "uuid", uuid));
  }
}
