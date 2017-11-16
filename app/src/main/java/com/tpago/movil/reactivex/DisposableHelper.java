package com.tpago.movil.reactivex;

import com.tpago.movil.util.ObjectHelper;

import io.reactivex.disposables.Disposable;

/**
 * @author hecvasro
 */
public final class DisposableHelper {

  public static void dispose(Disposable disposable) {
    ObjectHelper.checkNotNull(disposable, "disposable");
    if (!disposable.isDisposed()) {
      disposable.dispose();
    }
  }

  private DisposableHelper() {
  }
}
