package ca.gc.aafc.transaction.api.dto;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.mapper.CustomFieldAdapter;
import ca.gc.aafc.dina.mapper.DinaFieldAdapter;
import ca.gc.aafc.dina.mapper.IgnoreDinaMapping;
import ca.gc.aafc.dina.repository.meta.AttributeMetaInfoProvider;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.transaction.api.dto.ExternalAgentRoleRelationDto.AgentRoleMetadata;
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

import org.apache.commons.collections.CollectionUtils;
import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor @RelatedEntity(Transaction.class)
@JsonApiResource(type = TransactionDto.TYPENAME)
@TypeName(TransactionDto.TYPENAME)
@CustomFieldAdapter(adapters = TransactionDto.RoleListMapperAdapter.class)
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

  @JsonApiExternalRelation(type = "person")
  @JsonApiRelation
  @IgnoreDinaMapping(reason = "Custom resolved using the RoleListMapperAdapter")
  private Set<ExternalAgentRoleRelationDto> roles;

  private String createdBy;
  private OffsetDateTime createdOn;

  public static class RoleListMapperAdapter
  implements DinaFieldAdapter<TransactionDto, Transaction, Set<ExternalAgentRoleRelationDto>, Set<Role>> {

  @Override
  public Set<ExternalAgentRoleRelationDto> toDTO(Set<Role> roles) {
    if (CollectionUtils.isNotEmpty(roles)) {
      return roles.stream()
          .map(role -> ExternalAgentRoleRelationDto.builder()
              .id(role.getAgent().toString())
              .meta(new AgentRoleMetadata(role.getRole()))
              .build())
          .collect(Collectors.toSet());
    }

    return Collections.emptySet();
  }

  @Override
  public Set<Role> toEntity(Set<ExternalAgentRoleRelationDto> externalResources) {
    if (CollectionUtils.isNotEmpty(externalResources)) {
      return externalResources.stream()
          .map(resource -> Role.builder()
              .agent(UUID.fromString(resource.getId()))
              .role(resource.getMeta().getRole())
              .build())
          .collect(Collectors.toSet());
    }

    return Collections.emptySet();
  }

  @Override
  public Consumer<Set<Role>> entityApplyMethod(Transaction entityRef) {
    return entityRef::setRoles;
  }

  @Override
  public Consumer<Set<ExternalAgentRoleRelationDto>> dtoApplyMethod(TransactionDto dtoRef) {
    return dtoRef::setRoles;
  }

  @Override
  public Supplier<Set<Role>> entitySupplyMethod(Transaction entityRef) {
    return entityRef::getRoles;
  }

  @Override
  public Supplier<Set<ExternalAgentRoleRelationDto>> dtoSupplyMethod(TransactionDto dtoRef) {
    return dtoRef::getRoles;
  }
}
}
