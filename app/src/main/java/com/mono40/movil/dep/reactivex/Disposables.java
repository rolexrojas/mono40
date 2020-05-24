package com.mono40.movil.dep.reactivex;

import com.mono40.movil.util.ObjectHelper;

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
    disposable = ObjectHelper.checkNotNull(disposable, "disposable");
    if (!disposable.isDisposed()) {
      disposable.dispose();
    }
  }

  private Disposables() {
    throw new AssertionError("Cannot be instantiated");
  }
}
