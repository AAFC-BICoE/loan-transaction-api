package ca.gc.aafc.transaction.api.openapi;

import ca.gc.aafc.dina.testsupport.BaseRestAssuredTest;
import ca.gc.aafc.dina.testsupport.DatabaseSupportService;
import ca.gc.aafc.dina.testsupport.PostgresTestContainerInitializer;
import ca.gc.aafc.dina.testsupport.TransactionTestingHelper;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.dina.testsupport.specs.OpenAPI3Assertions;
import ca.gc.aafc.transaction.api.dto.TransactionDto;
import ca.gc.aafc.transaction.api.entities.Transaction;
import ca.gc.aafc.transaction.api.testsupport.fixtures.TransactionFixture;
import io.restassured.response.ValidatableResponse;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;

@TestPropertySource(properties = {
  "spring.config.additional-location=classpath:application-test.yml",
  "dev-user.enabled=true"})
@ContextConfiguration(initializers = {PostgresTestContainerInitializer.class})
public class TransactionOpenApiIT extends BaseRestAssuredTest {

  public static final String API_BASE_PATH = "/api/v1/transaction/";
  private static final String SCHEMA_NAME = "Transaction";

  @Inject
  private DatabaseSupportService databaseSupportService;

  @Inject
  private TransactionTestingHelper transactionTestingHelper;

  private static URL specUrl;

  @SneakyThrows({MalformedURLException.class, URISyntaxException.class})
  protected TransactionOpenApiIT() {
    super(API_BASE_PATH);
    specUrl = createSchemaUriBuilder(OpenAPIConstants.SPEC_HOST, OpenAPIConstants.SPEC_PATH).build()
        .toURL();
  }

  @Test
  public void post_NewTransaction_ReturnsOkayAndBody() {

    ValidatableResponse response = sendPost("",
        JsonAPITestHelper.toJsonAPIMap(TransactionDto.TYPENAME,
            TransactionFixture.newTransaction().build()));

    response
      .body("data.id", Matchers.notNullValue());
    OpenAPI3Assertions.assertRemoteSchema(specUrl, SCHEMA_NAME, response.extract().asString());

    // Cleanup:
    UUID uuid = response.extract().jsonPath().getUUID("data.id");
    transactionTestingHelper.doInTransactionWithoutResult(
        (a) -> databaseSupportService.deleteByProperty(Transaction.class, "uuid", uuid));
  }

}
