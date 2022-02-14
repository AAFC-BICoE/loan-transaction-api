package ca.gc.aafc.transaction.api.entities;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Role {
  
  @NotNull
  private UUID agent;

  private String role;

}
