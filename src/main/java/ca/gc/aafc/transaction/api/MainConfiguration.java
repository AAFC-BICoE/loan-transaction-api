package ca.gc.aafc.transaction.api;

import ca.gc.aafc.dina.DinaBaseApiAutoConfiguration;
import ca.gc.aafc.dina.search.messaging.producer.LogBasedMessageProducer;
import ca.gc.aafc.dina.search.messaging.producer.MessageProducer;
import ca.gc.aafc.dina.service.JaversDataService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = DinaBaseApiAutoConfiguration.class)
@ImportAutoConfiguration(DinaBaseApiAutoConfiguration.class)
@MapperScan(basePackageClasses = JaversDataService.class)
public class MainConfiguration {

  /**
   * Provides a fallback MessageProducer when messaging.isProducer is false.
   */
  @Configuration
  public static class FallbackMessageProducer {

    @Bean
    @ConditionalOnProperty(name = "messaging.isProducer", havingValue = "false")
    public MessageProducer init() {
      return new LogBasedMessageProducer();
    }
  }
}
