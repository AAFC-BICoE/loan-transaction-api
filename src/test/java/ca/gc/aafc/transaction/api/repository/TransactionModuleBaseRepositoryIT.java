package ca.gc.aafc.transaction.api.repository;

import java.util.Properties;
import javax.inject.Inject;

import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gc.aafc.dina.dto.JsonApiResource;
import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.dina.testsupport.PostgresTestContainerInitializer;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.transaction.api.BaseIntegrationTest;
import ca.gc.aafc.transaction.api.TransactionModuleApiLauncher;
import ca.gc.aafc.transaction.api.service.TransactionManagedAttributeService;

import ca.gc.aafc.dina.testsupport.repository.MockMvcBasedRepository;

@SpringBootTest(classes = {TransactionModuleApiLauncher.class, BaseIntegrationTest.TestConfig.class})
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
@ContextConfiguration(initializers = PostgresTestContainerInitializer.class)
public abstract class TransactionModuleBaseRepositoryIT extends MockMvcBasedRepository {

  protected TransactionModuleBaseRepositoryIT(String baseUrl,
                                              ObjectMapper objMapper) {
    super(baseUrl, objMapper);
  }

  @Inject
  protected TransactionManagedAttributeService managedAttributeService;

  public static JsonApiDocument dtoToJsonApiDocument(JsonApiResource jsonApiResource) {
    return JsonApiDocuments.createJsonApiDocument(
      jsonApiResource.getJsonApiId(), jsonApiResource.getJsonApiType(),
      JsonAPITestHelper.toAttributeMap(jsonApiResource)
    );
  }

  @TestConfiguration
  public static class TestConfig {
    @Bean
    public BuildProperties buildProperties() {
      Properties props = new Properties();
      props.setProperty("version", "test-api-version");
      return new BuildProperties(props);
    }
  }
}
