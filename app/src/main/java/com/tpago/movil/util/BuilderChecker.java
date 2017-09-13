package com.tpago.movil.util;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Strings.isNullOrEmpty;

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
   * @throws NullPointerException
   *   If {@code propertyName} is null.
   * @throws IllegalArgumentException
   *   If {@code propertyName} is empty.
   */
  public final BuilderChecker addPropertyNameIfMissing(String propertyName, boolean missing) {
    checkNotNull(propertyName, "isNull(propertyName)");
    checkArgument(!isNullOrEmpty(propertyName), "isNullOrEmpty(propertyName)");
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
    checkState(
      this.missingPropertyList.isEmpty(),
      "Missing properties: %1$s",
      Joiner.on(", ")
        .join(this.missingPropertyList)
    );
  }
}
