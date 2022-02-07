package ca.gc.aafc.transaction.api.testsupport.factories;

import ca.gc.aafc.dina.testsupport.factories.TestableEntityFactory;
import ca.gc.aafc.transaction.api.entities.Shipment;

import java.math.BigDecimal;

public class ShipmentFactory implements TestableEntityFactory<Shipment> {

  @Override
  public Shipment getEntityInstance() {
    return newShipment().build();
  }

  public static Shipment.ShipmentBuilder newShipment() {
    return Shipment.
        builder()
        .value(new BigDecimal("153.56"))
        .currency("CAD")
        .address(newAddress().build());
  }

  public static Shipment.Address.AddressBuilder newAddress() {
    return Shipment.Address.
        builder()
        .receiverName("Receiver Name");
  }

}
