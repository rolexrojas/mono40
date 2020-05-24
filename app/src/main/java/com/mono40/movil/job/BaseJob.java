package com.mono40.movil.job;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

/**
 * @author hecvasro
 */
public abstract class BaseJob extends Job {

  protected BaseJob(Params configuration) {
    super(configuration);
  }

  @Override
  public void onAdded() {
  }

  @Override
  protected int getRetryLimit() {
    return 0;
  }

  @Override
  protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(
    @NonNull Throwable throwable,
    int runCount,
    int maxRunCount
  ) {
    return RetryConstraint.CANCEL;
  }
}
