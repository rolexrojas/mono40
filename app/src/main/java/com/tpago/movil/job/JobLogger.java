package com.tpago.movil.job;

import com.birbit.android.jobqueue.BuildConfig;
import com.birbit.android.jobqueue.log.CustomLogger;

import timber.log.Timber;

/**
 * @author hecvasro
 */
final class JobLogger implements CustomLogger {

  static JobLogger create() {
    return new JobLogger();
  }

  private JobLogger() {
  }

  @Override
  public boolean isDebugEnabled() {
    return BuildConfig.DEBUG;
  }

  @Override
  public void d(String text, Object... args) {
    Timber.d(text, args);
  }

  @Override
  public void e(Throwable t, String text, Object... args) {
    Timber.e(t, text, args);
  }

  @Override
  public void e(String text, Object... args) {
    Timber.e(text, args);
  }

  @Override
  public void v(String text, Object... args) {
    Timber.v(text, args);
  }
}
