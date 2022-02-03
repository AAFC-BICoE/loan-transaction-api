package ca.gc.aafc.transaction.api;

import ca.gc.aafc.dina.testsupport.PostgresTestContainerInitializer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Properties;

@SpringBootTest(classes = {TransactionModuleApiLauncher.class, BaseIntegrationTest.TestConfig.class})
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
@ContextConfiguration(initializers = { PostgresTestContainerInitializer.class })
public class BaseIntegrationTest {

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
