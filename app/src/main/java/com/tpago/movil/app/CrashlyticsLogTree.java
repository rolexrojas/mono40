package com.tpago.movil.app;

import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * @author hecvasro
 */
final class CrashlyticsLogTree extends Timber.Tree {
  CrashlyticsLogTree(Context context) {
    Fabric.with(Preconditions.assertNotNull(context, "context == null"), new Crashlytics());
  }

  @Override
  protected void log(int priority, String tag, String message, Throwable throwable) {
    if (priority >= Log.WARN) {
      if (Objects.checkIfNull(throwable)) {
        Crashlytics.log(priority, tag, message);
      } else {
        Crashlytics.logException(throwable);
      }
    }
  }
}
