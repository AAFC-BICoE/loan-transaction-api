package ca.gc.aafc.transaction.api.testsupport.factories;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ca.gc.aafc.dina.testsupport.factories.TestableEntityFactory;
import ca.gc.aafc.transaction.api.entities.AgentRoles;

public class AgentRolesFactory implements TestableEntityFactory<AgentRoles> {

  @Override
  public AgentRoles getEntityInstance() {
    return newAgentRoles().build();
  }

  public static AgentRoles.AgentRolesBuilder newAgentRoles() {
    return AgentRoles.builder()
        .agent(UUID.randomUUID())
        .roles(new ArrayList<String>(List.of("Role1", "Role2")));
  }
}
