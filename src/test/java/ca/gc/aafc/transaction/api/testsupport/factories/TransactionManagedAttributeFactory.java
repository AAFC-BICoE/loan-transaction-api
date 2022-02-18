package ca.gc.aafc.transaction.api.testsupport.factories;

import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

import ca.gc.aafc.dina.entity.ManagedAttribute.ManagedAttributeType;
import ca.gc.aafc.dina.testsupport.factories.TestableEntityFactory;
import ca.gc.aafc.transaction.api.entities.TransactionManagedAttribute;

public class TransactionManagedAttributeFactory implements TestableEntityFactory<TransactionManagedAttribute> {

  @Override
  public TransactionManagedAttribute getEntityInstance() {
    return newManagedAttribute().build();
  }

  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   *
   * @return Pre-configured builder with all mandatory fields set
   */
  public static TransactionManagedAttribute.TransactionManagedAttributeBuilder newManagedAttribute() {
    return TransactionManagedAttribute.builder()
        .uuid(UUID.randomUUID())
        .name(TestableEntityFactory.generateRandomNameLettersOnly(12))
        .multilingualDescription(MultilingualDescriptionFactory.newMultilingualDescription().build())
        .createdBy("createdBy")
        .managedAttributeType(ManagedAttributeType.STRING);
   }

  /**
   * A utility method to create a list of qty number of Chains with no configuration.
   *
   * @param qty The number of Chains populated in the list
   * @return List of Chain
   */
  public static List<TransactionManagedAttribute> newListOf(int qty) {
    return newListOf(qty, null);
  }

  /**
   * A utility method to create a list of qty number of Chain with an incrementing attribute
   * based on the configuration argument. An example of configuration would be the functional
   * interface (bldr, index) -> bldr.name(" string" + index)
   *
   * @param qty           The number of Chain that is populated in the list.
   * @param configuration the function to apply, usually to differentiate the different entities in
   *                      the list.
   * @return List of Chain
   */
  public static List<TransactionManagedAttribute> newListOf(int qty,
      BiFunction<TransactionManagedAttribute.TransactionManagedAttributeBuilder, Integer, TransactionManagedAttribute.TransactionManagedAttributeBuilder> configuration) {

    return TestableEntityFactory.newEntity(qty, TransactionManagedAttributeFactory::newManagedAttribute, configuration,
        TransactionManagedAttribute.TransactionManagedAttributeBuilder::build);
  }

}
