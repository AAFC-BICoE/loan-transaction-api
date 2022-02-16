package ca.gc.aafc.transaction.api.entities;

import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.javers.core.metamodel.annotation.Value;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Value // This class is considered a "value" belonging to a Transaction:
public class Role {
  
  @NotNull
  private UUID agent;

  @NotBlank
  private String role;

}
