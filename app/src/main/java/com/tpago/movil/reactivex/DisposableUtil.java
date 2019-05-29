package com.tpago.movil.reactivex;

import com.tpago.movil.util.ObjectHelper;

import io.reactivex.disposables.Disposable;

/**
 * @author hecvasro
 */
public final class DisposableUtil {

    public static void dispose(Disposable disposable) {
        if (disposable != null) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }

    private DisposableUtil() {
    }
}
