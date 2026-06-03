package ca.gc.aafc.transaction.api.entities;

import ca.gc.aafc.dina.entity.ManagedAttribute;
import ca.gc.aafc.dina.i18n.MultilingualDescription;
import ca.gc.aafc.dina.i18n.MultilingualTitle;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Transient;
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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity(name = "managed_attribute")
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
  @Type(PostgreSQLEnumType.class)
  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  private VocabularyElementType vocabularyElementType;

  @Column(columnDefinition = "text[]")
  private String[] acceptedValues;

  @Column(name = "created_on", insertable = false, updatable = false)
  @Generated(value = GenerationTime.INSERT)
  private OffsetDateTime createdOn;

  @NotBlank
  @Column(name = "created_by", updatable = false)
  private String createdBy;

  @Type(JsonType.class)
  @Column(name = "multilingual_description", columnDefinition = "jsonb")
  private MultilingualDescription multilingualDescription;
  
  @NotNull
  @Column(updatable = false)
  private String name;

  @NotBlank
  @Size(max = 50)
  @Column(updatable = false)
  private String key;

  @Override
  public String getTerm() {
    return null;
  }

  // not implemented for now
  @Transient
  @Override
  public String getUnit() {
    return null;
  }

  @Override
  public MultilingualTitle getMultilingualTitle() {
    return null;
  }
}
