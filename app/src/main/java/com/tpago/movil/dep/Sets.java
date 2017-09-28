package com.tpago.movil.dep;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author hecvasro
 */
@Deprecated
public final class Sets {
  public static <T> Set<T> createSet() {
    return new HashSet<>();
  }

  public static <T> Set<T> createSortedSet(Collection<T> collection) {
    return new TreeSet<>(collection);
  }

  public static <T> Set<T> createSortedSet() {
    return new TreeSet<>();
  }

  private Sets() {
    throw new AssertionError("Cannot be instantiated");
  }
}
