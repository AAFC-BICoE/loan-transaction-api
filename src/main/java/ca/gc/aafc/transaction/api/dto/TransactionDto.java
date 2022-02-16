package ca.gc.aafc.transaction.api.dto;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.mapper.IgnoreDinaMapping;
import ca.gc.aafc.dina.repository.meta.AttributeMetaInfoProvider;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.transaction.api.entities.Role;
import ca.gc.aafc.transaction.api.entities.Shipment;
import ca.gc.aafc.transaction.api.entities.Transaction;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor @RelatedEntity(Transaction.class)
@JsonApiResource(type = TransactionDto.TYPENAME)
@TypeName(TransactionDto.TYPENAME)
public class TransactionDto extends AttributeMetaInfoProvider {

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

  private List<Role> roles;

  // Field is mapped using the roles field. See getAgents().
  @JsonApiRelation
  @IgnoreDinaMapping
  private List<ExternalRelationDto> agents;

  private String createdBy;
  private OffsetDateTime createdOn;

  public List<ExternalRelationDto> getAgents() {
    return roles.stream()
        .map(role -> ExternalRelationDto.builder()
            .id(role.getAgent().toString())
            .type("person")
            .build()
        )
        .collect(Collectors.toList());
  }
}
