package ca.gc.aafc.transaction.api;

import javax.inject.Inject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ca.gc.aafc.dina.DinaBaseApiAutoConfiguration;
import ca.gc.aafc.dina.search.messaging.producer.LogBasedMessageProducer;
import ca.gc.aafc.dina.search.messaging.producer.MessageProducer;
import ca.gc.aafc.dina.service.JaversDataService;
import ca.gc.aafc.transaction.api.dto.TransactionManagedAttributeDto;
import ca.gc.aafc.transaction.api.util.ManagedAttributeIdMapper;
import io.crnk.core.engine.registry.ResourceRegistry;

@Configuration
@ComponentScan(basePackageClasses = DinaBaseApiAutoConfiguration.class)
@ImportAutoConfiguration(DinaBaseApiAutoConfiguration.class)
@MapperScan(basePackageClasses = JaversDataService.class)
public class MainConfiguration {

  @Inject
  @SuppressWarnings({"deprecation", "unchecked"})
  public void setupManagedAttributeLookup(ResourceRegistry resourceRegistry) {
    var resourceInfo = resourceRegistry.getEntry(TransactionManagedAttributeDto.class)
      .getResourceInformation();

    resourceInfo.setIdStringMapper(
      new ManagedAttributeIdMapper(resourceInfo.getIdStringMapper()));
  }

  /**
   * Provides a fallback MessageProducer when messaging.isProducer is false.
   */
  @Configuration
  public static class FallbackMessageProducer {

    @Bean
    @ConditionalOnProperty(name = "dina.messaging.isProducer", havingValue = "false")
    public MessageProducer init() {
      return new LogBasedMessageProducer();
    }
  }
}
