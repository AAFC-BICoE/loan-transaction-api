package ca.gc.aafc.transaction.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.entity.ManagedAttribute.ManagedAttributeType;
import ca.gc.aafc.dina.i18n.MultilingualDescription;
import ca.gc.aafc.transaction.api.entities.TransactionManagedAttribute;

import io.crnk.core.resource.annotations.JsonApiField;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RelatedEntity(TransactionManagedAttribute.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonApiResource(type = TransactionManagedAttributeDto.TYPENAME) 
public class TransactionManagedAttributeDto {

  public static final String TYPENAME = "managed-attribute";

  @JsonApiId
  @Id
  @PropertyName("id")
  private UUID uuid;

  @JsonApiField(patchable = false)
  private String name;
  
  @JsonApiField(patchable = false)
  private String key;

  private ManagedAttributeType managedAttributeType;
  private String[] acceptedValues;
  private OffsetDateTime createdOn;
  private String createdBy;
  private MultilingualDescription multilingualDescription;

}
