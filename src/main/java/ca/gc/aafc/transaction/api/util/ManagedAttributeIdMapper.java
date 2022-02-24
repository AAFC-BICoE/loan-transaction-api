package ca.gc.aafc.transaction.api.util;

import java.util.UUID;

import io.crnk.core.engine.parser.StringMapper;
import lombok.RequiredArgsConstructor;

/**
 * Lets you use either the UUID or key as the ID.
 * e.g. /managed-attribute/key
 */
@RequiredArgsConstructor
public class ManagedAttributeIdMapper implements StringMapper<Object> {
  private final StringMapper<Object> stringMapper;

  @Override
  public Object parse(String input) {
    // If the input's not in UUID format then use the raw string as the ID:
    try {
      UUID.fromString(input);
    } catch (IllegalArgumentException e) {
      return input;
    }
    return stringMapper.parse(input);
  }

  @Override
  public String toString(Object input) {
    return stringMapper.toString(input);
  }

}
