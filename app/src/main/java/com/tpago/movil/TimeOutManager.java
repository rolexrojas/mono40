package com.tpago.movil;

import com.tpago.movil.reactivex.Disposables;
import com.tpago.movil.util.Preconditions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

/**
 * @author hecvasro
 */
public final class TimeOutManager {
  private final ConfigManager configManager;
  private final TimeOutHandler timeOutHandler;

  private final List<Object> lockList
    = new ArrayList<>();
  private final ConfigManager.OnTimeOutChangedListener onTimeOutChangedListener
    = new ConfigManager.OnTimeOutChangedListener() {
    @Override
    public void onTimeOutChanged(TimeOut timeOut) {
      reset();
    }
  };

  private boolean started = false;
  private Disposable disposable = Disposables.disposed();

  public TimeOutManager(ConfigManager configManager, TimeOutHandler timeOutHandler) {
    this.configManager = Preconditions.checkNotNull(configManager, "configManager == null");
    this.timeOutHandler = Preconditions.checkNotNull(timeOutHandler, "timeOutHandler == null");
  }

  private void startInternally() {
    final TimeOut timeOut = configManager.getTimeOut();
    disposable = Completable.complete()
      .delay(timeOut.getValue(), TimeOut.getUnit())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action() {
        @Override
        public void run() throws Exception {
          timeOutHandler.handleTimeOut();
        }
      });
    configManager.addOnTimeOutChangedListener(onTimeOutChangedListener);
  }

  private void stopInternally() {
    configManager.removeOnTimeOutChangedListener(onTimeOutChangedListener);
    Disposables.dispose(disposable);
  }

  public final void start() {
    if (!started) {
      started = true;
      startInternally();
    }
  }

  public final void stop() {
    if (started) {
      stopInternally();
      started = false;
    }
  }

  public final void reset() {
    if (started) {
      stopInternally();
      startInternally();
    }
  }

  public final void addLock(Object lock) {
    Preconditions.checkNotNull(lock, "lock == null");
    if (!lockList.contains(lock)) {
      lockList.add(lock);
      if (started) {
        stopInternally();
      }
    }
  }

  public final void removeLock(Object lock) {
    Preconditions.checkNotNull(lock, "lock == null");
    if (lockList.contains(lock)) {
      lockList.remove(lock);
      if (started && lockList.isEmpty()) {
        startInternally();
      }
    }
  }

  public interface TimeOutHandler {
    void handleTimeOut();
  }
}
