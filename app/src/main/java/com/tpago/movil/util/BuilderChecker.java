package com.tpago.movil.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper for the creation of builders.
 *
 * @author hecvasro
 */
public final class BuilderChecker {

  public static BuilderChecker create() {
    return new BuilderChecker();
  }

  private final List<String> missingPropertyList;

  private BuilderChecker() {
    this.missingPropertyList = new ArrayList<>();
  }

  /**
   * Adds the given property name to the missing properties if the given flag is true and it hasn't
   * been added yet.
   *
   * @throws IllegalArgumentException
   *   If {@code propertyName} is {@code null} or empty.
   */
  public final BuilderChecker addPropertyNameIfMissing(String propertyName, boolean missing) {
    if (StringHelper.isNullOrEmpty(propertyName)) {
      throw new IllegalArgumentException("isNullOrEmpty(propertyName)");
    }
    if (missing && !this.missingPropertyList.contains(propertyName)) {
      this.missingPropertyList.add(propertyName);
    }
    return this;
  }

  /**
   * Checks if there are not missing properties.
   *
   * @throws IllegalStateException
   *   If there are missing properties.
   */
  public final void checkNoMissingProperties() {
    if (!this.missingPropertyList.isEmpty()) {
      throw new IllegalStateException(
        String.format(
          "Missing properties: %1$s",
          StringHelper.join(", ", this.missingPropertyList)
        )
      );
    }
  }
}
