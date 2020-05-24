package com.mono40.movil.dep;

import com.mono40.movil.dep.reactivex.Disposables;
import com.mono40.movil.util.ObjectHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * @author hecvasro
 */
@Deprecated
public final class TimeOutManager {

  private final ConfigManager configManager;
  private final TimeOutHandler timeOutHandler;

  private final List<Object> lockList = new ArrayList<>();
  private final ConfigManager.OnTimeOutChangedListener onTimeOutChangedListener = (to) -> reset();

  private boolean started = false;
  private Disposable disposable = Disposables.disposed();

  public TimeOutManager(ConfigManager configManager, TimeOutHandler timeOutHandler) {
    this.configManager = ObjectHelper.checkNotNull(configManager, "configManager");
    this.timeOutHandler = ObjectHelper.checkNotNull(timeOutHandler, "timeOutHandler");
  }

  private void startInternally() {
//    final TimeOut timeOut = configManager.getTimeOut();
//    disposable = Completable.complete()
//      .delay(timeOut.getValue(), TimeOut.getUnit())
//      .observeOn(AndroidSchedulers.mainThread())
//      .subscribe(this.timeOutHandler::handleTimeOut);
//    configManager.addOnTimeOutChangedListener(onTimeOutChangedListener);
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
    ObjectHelper.checkNotNull(lock, "lock");
    if (!lockList.contains(lock)) {
      lockList.add(lock);
      if (started) {
        stopInternally();
      }
    }
  }

  public final void removeLock(Object lock) {
    ObjectHelper.checkNotNull(lock, "lock");
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
