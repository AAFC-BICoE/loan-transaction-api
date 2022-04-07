package ca.gc.aafc.transaction.api.entities;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AgentRoles {

  @NotNull
  private UUID agent;

  @NotEmpty
  private List<@NotBlank String> roles;

  private LocalDate date;

  @Size(max = 1000)
  private String remarks;

}
