package ca.gc.aafc.transaction.api.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.JsonApiResource;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.transaction.api.dto.external.PersonExternalDto;
import ca.gc.aafc.transaction.api.entities.AgentRoles;
import ca.gc.aafc.transaction.api.entities.Shipment;
import ca.gc.aafc.transaction.api.entities.Transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor @RelatedEntity(Transaction.class)
@JsonApiTypeForClass(TransactionDto.TYPENAME)
@TypeName(TransactionDto.TYPENAME)
public class TransactionDto implements JsonApiResource {

  public static final String TYPENAME = "transaction";

  @JsonApiId
  @Id
  @PropertyName("id")
  private UUID uuid;

  private String group;

  private Transaction.Direction materialDirection;
  private String transactionNumber;
  private List<String> otherIdentifiers;
  private Boolean materialToBeReturned;
  private String purpose;
  private String transactionType;
  private String status;

  private LocalDate openedDate;
  private LocalDate closedDate;
  private LocalDate dueDate;

  private String remarks;

  private Shipment shipment;

  @Builder.Default
  private List<AgentRoles> agentRoles = List.of();

  // Calculated field see getInvolvedAgents
  private List<ExternalRelationDto> involvedAgents;

  @JsonApiExternalRelation(type = "material-sample")
  private List<ExternalRelationDto> materialSamples = List.of();

  @Builder.Default
  private Map<String, String> managedAttributes = Map.of();

  @JsonApiExternalRelation(type = "metadata")
  @Builder.Default
  private List<ExternalRelationDto> attachment = List.of();

  private String createdBy;
  private OffsetDateTime createdOn;

  public List<ExternalRelationDto> getInvolvedAgents() {
    if (CollectionUtils.isNotEmpty(agentRoles)) {
      return agentRoles.stream()
        .map(agent -> ExternalRelationDto.builder()
          .id(agent.getAgent().toString())
          .type(PersonExternalDto.EXTERNAL_TYPENAME)
          .build()
        ).collect(Collectors.toList());
    }
    return List.of();
  }

  @Override
  @JsonIgnore
  public String getJsonApiType() {
    return TYPENAME;
  }

  @Override
  @JsonIgnore
  public UUID getJsonApiId() {
    return uuid;
  }
}
