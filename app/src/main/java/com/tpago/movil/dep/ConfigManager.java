package com.tpago.movil.dep;

import android.content.Context;
import android.content.SharedPreferences;

import com.tpago.movil.app.App;
import com.tpago.movil.app.ui.main.settings.timeout.TimeoutSessionOption;
import com.tpago.movil.dep.content.SharedPreferencesCreator;
import com.tpago.movil.util.ObjectHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hecvasro
 */
@Deprecated
public final class ConfigManager {

    private static final String KEY_TIME_OUT = "timeOut";

    private final SharedPreferences sharedPreferences;
    private final List<OnTimeOutChangedListener> onTimeOutChangedListenerList;

    public ConfigManager(SharedPreferencesCreator sharedPreferencesCreator) {
        this.sharedPreferences = ObjectHelper
                .checkNotNull(sharedPreferencesCreator, "sharedPreferencesCreator")
                .create(ConfigManager.class.getCanonicalName());
        this.onTimeOutChangedListenerList = new ArrayList<>();
    }

    public static TimeoutSessionOption getTimeOut(Context context) {
        return new TimeoutSessionOption(getSharedPreferences(context).getInt(KEY_TIME_OUT, 10));
    }

    public final void addOnTimeOutChangedListener(OnTimeOutChangedListener listener) {
        ObjectHelper.checkNotNull(listener, "listener");
        if (!onTimeOutChangedListenerList.contains(listener)) {
            onTimeOutChangedListenerList.add(listener);
        }
    }

    public final void removeOnTimeOutChangedListener(OnTimeOutChangedListener listener) {
        ObjectHelper.checkNotNull(listener, "listener");
        onTimeOutChangedListenerList.remove(listener);
    }

    public static void setTimeOut(Context context, TimeoutSessionOption timeOut) {
        getSharedPreferences(context).edit()
                .putInt(
                        KEY_TIME_OUT,
                        ObjectHelper.checkNotNull(timeOut, "timeOut")
                                .getTimeInMinutes()
                )
                .apply();
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getApplicationContext().getSharedPreferences(App.class.getCanonicalName(), Context.MODE_PRIVATE);
    }

    public interface OnTimeOutChangedListener {

        void onTimeOutChanged(TimeoutSessionOption timeOut);
    }
}
