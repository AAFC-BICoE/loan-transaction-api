package ca.gc.aafc.transaction.api.mapper;

import java.util.Set;

import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import ca.gc.aafc.dina.mapper.DinaMapperV2;
import ca.gc.aafc.transaction.api.dto.TransactionManagedAttributeDto;
import ca.gc.aafc.transaction.api.entities.TransactionManagedAttribute;

@Mapper
public interface TransactionManagedAttributeMapper extends DinaMapperV2<TransactionManagedAttributeDto, TransactionManagedAttribute> {

  TransactionManagedAttributeMapper INSTANCE = Mappers.getMapper(TransactionManagedAttributeMapper.class);

  TransactionManagedAttributeDto toDto(TransactionManagedAttribute entity, @Context Set<String> provided, @Context String scope);

  TransactionManagedAttribute toEntity(TransactionManagedAttributeDto dto, @Context Set<String> provided, @Context String scope);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget TransactionManagedAttribute entity, TransactionManagedAttributeDto dto, @Context Set<String> provided, @Context String scope);
}
