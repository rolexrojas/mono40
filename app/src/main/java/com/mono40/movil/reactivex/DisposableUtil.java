package com.mono40.movil.reactivex;

import com.mono40.movil.util.ObjectHelper;

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
