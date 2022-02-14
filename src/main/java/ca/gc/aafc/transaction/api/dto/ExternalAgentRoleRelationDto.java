package ca.gc.aafc.transaction.api.dto;

import org.javers.core.metamodel.annotation.TypeName;
import org.javers.core.metamodel.annotation.Value;

import io.crnk.core.resource.ResourceTypeHolder;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiMetaInformation;
import io.crnk.core.resource.annotations.JsonApiResource;
import io.crnk.core.resource.meta.MetaInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonApiResource(type = "external-type")
@Value // This class is considered a "value" belonging to a parent DTO.
@TypeName(ExternalAgentRoleRelationDto.TYPENAME)
public class ExternalAgentRoleRelationDto implements ResourceTypeHolder {

  public static final String TYPENAME = "external-relation";
  public static final String ID_FIELD_NAME = "id";

  @JsonApiId
  private String id;

  private String type;

  @JsonApiMetaInformation
  private AgentRoleMetadata meta;

  @Getter
  @Setter
  @AllArgsConstructor
  public static class AgentRoleMetadata implements MetaInformation {

    private String role;

  }
}
