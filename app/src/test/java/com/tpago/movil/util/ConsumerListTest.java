package com.tpago.movil.util;

import org.junit.Test;

import java.lang.ref.WeakReference;

import static org.junit.Assert.assertEquals;

public final class ConsumerListTest {

  @Test
  public final void equals() {
    final Object object = new Object();
    final WeakReference<Object> referenceA = new WeakReference<>(object);
    final WeakReference<Object> referenceB = new WeakReference<>(object);
    assertEquals(referenceA, referenceB);
  }
}
