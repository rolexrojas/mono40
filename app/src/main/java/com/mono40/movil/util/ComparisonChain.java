package com.mono40.movil.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hecvasro
 */
public final class ComparisonChain {

  public static ComparisonChain create() {
    return new ComparisonChain();
  }

  private final List<Entry<?>> entries;

  private ComparisonChain() {
    this.entries = new ArrayList<>();
  }

  public final <T extends Comparable<T>> ComparisonChain compare(T a, T b) {
    this.entries.add(Entry.create(a, b));
    return this;
  }

  public final int result() {
    int result;
    for (Entry<?> entry : this.entries) {
      result = entry.result();
      if (result != 0) {
        return result;
      }
    }
    return 0;
  }

  private static final class Entry<T extends Comparable<T>> {

    private static <T extends Comparable<T>> Entry<T> create(T a, T b) {
      return new Entry<>(a, b);
    }

    private final T a;
    private final T b;

    private Entry(T a, T b) {
      this.a = ObjectHelper.checkNotNull(a, "a");
      this.b = ObjectHelper.checkNotNull(b, "b");
    }

    private int result() {
      return this.a.compareTo(this.b);
    }
  }
}
