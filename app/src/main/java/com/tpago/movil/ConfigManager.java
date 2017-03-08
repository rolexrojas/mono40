package com.tpago.movil;

import android.content.SharedPreferences;

import com.tpago.movil.content.SharedPreferencesCreator;
import com.tpago.movil.util.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hecvasro
 */
public final class ConfigManager {
  private static final String KEY_TIME_OUT = "timeOut";

  private final SharedPreferences sharedPreferences;
  private final List<OnTimeOutChangedListener> onTimeOutChangedListenerList;

  public ConfigManager(SharedPreferencesCreator sharedPreferencesCreator) {
    this.sharedPreferences = Preconditions
      .checkNotNull(sharedPreferencesCreator, "sharedPreferencesCreator == null")
      .create(ConfigManager.class.getCanonicalName());
    this.onTimeOutChangedListenerList = new ArrayList<>();
  }

  public final TimeOut getTimeOut() {
    // TODO: Change default time out value to ten (10) minutes.
    return TimeOut.valueOf(sharedPreferences.getString(KEY_TIME_OUT, TimeOut.TWO.name()));
  }

  public final void addOnTimeOutChangedListener(OnTimeOutChangedListener listener) {
    Preconditions.checkNotNull(listener, "listener == null");
    if (!onTimeOutChangedListenerList.contains(listener)) {
      onTimeOutChangedListenerList.add(listener);
    }
  }

  public final void removeOnTimeOutChangedListener(OnTimeOutChangedListener listener) {
    Preconditions.checkNotNull(listener, "listener == null");
    if (onTimeOutChangedListenerList.contains(listener)) {
      onTimeOutChangedListenerList.remove(listener);
    }
  }

  public final void setTimeOut(TimeOut timeOut) {
    sharedPreferences.edit()
      .putString(KEY_TIME_OUT, Preconditions.checkNotNull(timeOut, "timeOut == null").name())
      .apply();
    for (OnTimeOutChangedListener listener : onTimeOutChangedListenerList) {
      listener.onTimeOutChanged(timeOut);
    }
  }

  public interface OnTimeOutChangedListener {
    void onTimeOutChanged(TimeOut timeOut);
  }
}
