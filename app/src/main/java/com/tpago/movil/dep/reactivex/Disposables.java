package com.tpago.movil.dep.reactivex;

import com.tpago.movil.dep.Preconditions;

import io.reactivex.disposables.Disposable;

/**
 * @author hecvasro
 */
@Deprecated
public final class Disposables {
  public static Disposable disposed() {
    return io.reactivex.disposables.Disposables.disposed();
  }

  public static void dispose(Disposable disposable) {
    disposable = Preconditions.assertNotNull(disposable, "disposable == null");
    if (!disposable.isDisposed()) {
      disposable.dispose();
    }
  }

  private Disposables() {
    throw new AssertionError("Cannot be instantiated");
  }
}
