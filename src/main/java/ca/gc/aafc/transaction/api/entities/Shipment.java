package ca.gc.aafc.transaction.api.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class Shipment {

  @Size(max = 1000)
  private String contentRemarks;

  // Format in String instead of Number to avoid any confusion and unwanted rounding
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal value;

  // ISO 4217 currency code
  @Size(min = 3, max = 3)
  private String currency;

  @Min(1)
  private Integer itemCount;

  private LocalDate shippedOn;

  @Size (max = 50)
  private String status;

  @Size (max = 250)
  private String packingMethod;

  @Size (max = 50)
  private String trackingNumber;

  @Valid
  private Address address;

  @Size(max = 1000)
  private String shipmentRemarks;

  @Data
  @Builder
  public static class Address {
    @Size (max = 150)
    private String receiverName;

    @Size (max = 150)
    private String companyName;

    @Size (max = 150)
    private String addressLine1;
    @Size (max = 150)
    private String addressLine2;

    @Size (max = 150)
    private String city;

    @Size (max = 150)
    private String provinceState;

    @Size (max = 50)
    private String zipCode;

    @Size (max = 50)
    private String country;

  }

}
