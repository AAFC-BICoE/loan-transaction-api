package ca.gc.aafc.transaction.api.entities;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import ca.gc.aafc.dina.entity.DinaEntity;
import ca.gc.aafc.dina.service.OnUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
@NaturalIdCache
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
public class Transaction implements DinaEntity {

  public enum Direction { IN, OUT }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NaturalId
  @NotNull(groups = OnUpdate.class)
  @Column(name = "uuid", unique = true)
  private UUID uuid;

  @NotBlank
  @Size(max = 50)
  @Column(name = "_group")
  private String group;

  @NotNull
  @Type(type = "pgsql_enum")
  @Enumerated(EnumType.STRING)
  private Direction materialDirection;

  @Size(max = 50)
  private String transactionNumber;

  @Type(type = "list-array")
  private List<String> otherIdentifiers;

  @Generated(value = GenerationTime.INSERT)
  private Boolean materialToBeReturned;

  @Size(max = 50)
  private String transactionType;
  @Size(max = 50)
  private String status;

  @Size(max = 1000)
  private String purpose;

  @PastOrPresent
  private LocalDate openedDate;
  @PastOrPresent
  private LocalDate closedDate;

  private LocalDate dueDate;

  @Size(max = 1000)
  private String remarks;

  @Type(type = "jsonb")
  @Valid
  private Shipment shipment;

  @Type(type = "jsonb")
  @Valid
  @Builder.Default
  private List<AgentRoles> agentRoles = List.of();

  // This field is mapped to the dto, but NOT stored in the database.
  // IgnoreDinaMapping annotation is not respected when using external relationships.
  @Transient
  private List<UUID> involvedAgents;

  @Type(type = "list-array")
  private List<UUID> materialSamples = List.of();

  @Type(type = "jsonb")
  @NotNull
  @Builder.Default
  private Map<String, String> managedAttributes = new HashMap<>();

  @Type(type = "list-array")
  @Column(name = "attachment", columnDefinition = "uuid[]")
  @Builder.Default
  private List<UUID> attachment = List.of();

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "created_on", insertable = false, updatable = false)
  @Generated(value = GenerationTime.INSERT)
  private OffsetDateTime createdOn;

}
