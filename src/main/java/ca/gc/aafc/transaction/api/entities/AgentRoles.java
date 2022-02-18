package ca.gc.aafc.transaction.api.entities;

import java.util.ArrayList;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AgentRoles {

  @NotNull
  private UUID agent;

  @NotEmpty
  private ArrayList<@NotBlank String> roles;

}
