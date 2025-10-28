package ca.gc.aafc.transaction.api.mapper;

import java.util.UUID;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.JsonApiExternalResource;
import ca.gc.aafc.transaction.api.dto.external.MaterialSampleExternalDto;
import ca.gc.aafc.transaction.api.dto.external.MetadataExternalDto;
import ca.gc.aafc.transaction.api.dto.external.PersonExternalDto;

/**
 * Not a MapStruct mapper
 *
 * Map known external Relationship types
 */
public final class ExternalRelationshipMapper {

  private ExternalRelationshipMapper() {
    //utility class
  }

  public static JsonApiExternalResource externalRelationDtoToJsonApiExternalResource(ExternalRelationDto externalRelationDto) {
    if (externalRelationDto == null) {
      return null;
    }

    return switch (externalRelationDto.getType()) {
      case MaterialSampleExternalDto.EXTERNAL_TYPENAME -> MaterialSampleExternalDto.builder().uuid(UUID.fromString(externalRelationDto.getId())).build();
      case PersonExternalDto.EXTERNAL_TYPENAME -> PersonExternalDto.builder().uuid(UUID.fromString(externalRelationDto.getId())).build();
      case MetadataExternalDto.EXTERNAL_TYPENAME -> MetadataExternalDto.builder().uuid(UUID.fromString(externalRelationDto.getId())).build();
      default -> throw new IllegalStateException("Unsupported type for JsonApiExternalResource: " + externalRelationDto.getType());
    };
  }
    
}
