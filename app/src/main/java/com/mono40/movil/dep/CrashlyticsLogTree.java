package com.mono40.movil.dep;

import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.mono40.movil.util.ObjectHelper;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * @author hecvasro
 */
@Deprecated
final class CrashlyticsLogTree extends Timber.Tree {

  CrashlyticsLogTree(Context context) {
    Fabric.with(ObjectHelper.checkNotNull(context, "context"), new Crashlytics());
  }

  @Override
  protected void log(int priority, String tag, String message, Throwable throwable) {
    if (priority >= Log.WARN) {
      if (ObjectHelper.isNull(throwable)) {
        Crashlytics.log(priority, tag, message);
      } else {
        Crashlytics.logException(throwable);
      }
    }
  }
}
