package ca.gc.aafc.transaction.api.entities;

import ca.gc.aafc.dina.entity.ManagedAttribute;
import ca.gc.aafc.dina.i18n.MultilingualDescription;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity(name = "managed_attribute")
@TypeDefs({@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class),
  @TypeDef(name = "string-array", typeClass = StringArrayType.class),
  @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@AllArgsConstructor
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@NaturalIdCache
public class TransactionManagedAttribute implements ManagedAttribute {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NaturalId
  @NotNull
  @Column(name = "uuid", unique = true)
  private UUID uuid;

  @NotBlank
  @Column(name = "_group")
  @Size(max = 50)
  private String group;

  @NotNull
  @Type(type = "pgsql_enum")
  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  private ManagedAttributeType managedAttributeType;

  @Type(type = "string-array")
  @Column(columnDefinition = "text[]")
  private String[] acceptedValues;

  @Column(name = "created_on", insertable = false, updatable = false)
  @Generated(value = GenerationTime.INSERT)
  private OffsetDateTime createdOn;

  @NotBlank
  @Column(name = "created_by", updatable = false)
  private String createdBy;

  @Type(type = "jsonb")
  @Column(name = "multilingual_description")
  private MultilingualDescription multilingualDescription;
  
  @NotNull
  @Column(updatable = false)
  private String name;

  @NotBlank
  @Size(max = 50)
  @Column(updatable = false)
  private String key;

}
