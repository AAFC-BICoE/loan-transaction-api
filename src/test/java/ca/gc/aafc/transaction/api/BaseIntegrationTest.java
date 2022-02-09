package ca.gc.aafc.transaction.api;

import java.util.Properties;
import javax.inject.Inject;

import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import ca.gc.aafc.dina.testsupport.PostgresTestContainerInitializer;
import ca.gc.aafc.transaction.api.service.TransactionManagedAttributeService;

@SpringBootTest(classes = {TransactionModuleApiLauncher.class, BaseIntegrationTest.TestConfig.class})
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
@ContextConfiguration(initializers = PostgresTestContainerInitializer.class)
public class BaseIntegrationTest {

  @Inject
  protected TransactionManagedAttributeService managedAttributeService;

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
