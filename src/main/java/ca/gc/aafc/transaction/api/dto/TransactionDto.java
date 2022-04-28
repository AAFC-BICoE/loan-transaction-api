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
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.AttributeMetaInfoProvider;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.transaction.api.entities.AgentRoles;
import ca.gc.aafc.transaction.api.entities.Shipment;
import ca.gc.aafc.transaction.api.entities.Transaction;

import io.crnk.core.resource.annotations.JsonApiField;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import io.crnk.core.resource.annotations.PatchStrategy;
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
@JsonApiResource(type = TransactionDto.TYPENAME)
@TypeName(TransactionDto.TYPENAME)
public class TransactionDto extends AttributeMetaInfoProvider {

  public static final String TYPENAME = "transaction";
  public static final String EXTERNAL_AGENT = "person";

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

  // Field is mapped using the roles field. See getAgents().
  // This field is mapped to the entity, but NOT stored in the database.
  @JsonApiRelation
  @JsonApiExternalRelation(type = EXTERNAL_AGENT)
  private List<ExternalRelationDto> involvedAgents = List.of();

  @JsonApiRelation
  @JsonApiExternalRelation(type = "material-sample")
  private ExternalRelationDto materialSample;

  @JsonApiField(patchStrategy = PatchStrategy.SET)
  @Builder.Default
  private Map<String, String> managedAttributes = Map.of();

  @JsonApiExternalRelation(type = "metadata")
  @JsonApiRelation
  @Builder.Default
  private List<ExternalRelationDto> attachment = List.of();

  private String createdBy;
  private OffsetDateTime createdOn;

  public void setInvolvedAgents(List<ExternalRelationDto> fromEntity) {
    if (CollectionUtils.isNotEmpty(agentRoles)) {
      involvedAgents = agentRoles.stream()
          .map(agent -> ExternalRelationDto.builder()
              .id(agent.getAgent().toString())
              .type(EXTERNAL_AGENT)
              .build()
          ).collect(Collectors.toList());      
    }
  }
}
