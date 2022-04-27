package ca.gc.aafc.transaction.api.external;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;

@Component
public class ExternalResourceProviderImplementation implements ExternalResourceProvider {

  public static final Map<String, String> TYPE_TO_REFERENCE_MAP = Map.of(
    "person", "agent/api/v1/person",
    "metadata", "objectstore/api/v1/metadata",
    "material-sample", "collection/api/v1/material-sample"
  );

  @Override
  public String getReferenceForType(String type) {
    return TYPE_TO_REFERENCE_MAP.get(type);
  }

  @Override
  public Set<String> getTypes() {
    return TYPE_TO_REFERENCE_MAP.keySet();
  }
}
