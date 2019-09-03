package com.tpago.movil.util;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tpago.movil.dep.App;
import com.tpago.movil.dep.ConfigManager;
import com.tpago.movil.session.SessionManager;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LogoutTimerService extends Service {
    private final long COUNT_DOWN_TIME_MS = TimeUnit.SECONDS.toMillis(15);
    private Timer timer;
    @Inject
    SessionManager sessionManager;
    private LocalBroadcastManager localBroadcastManager;
    public static final String LOGOUT_BROADCAST = "LOGOUT_BROADCAST";
    public static final String USER_INTERACTION_BROADCAST = "USER_INTERACTION_BROADCAST";
    private long lastActivityTimeInMillis;


    @Override
    public void onCreate() {
        super.onCreate();
        sessionManager = DaggerLogoutTimerServiceComponent.builder().appComponent(((App) getApplication()).component()).build().sessionManager();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(new UserInteractionReceiver(), new IntentFilter(USER_INTERACTION_BROADCAST));
        timer = new Timer();
        timer.scheduleAtFixedRate(new LogoutTimerTask(), COUNT_DOWN_TIME_MS, COUNT_DOWN_TIME_MS);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class LogoutTimerTask extends TimerTask {

        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            if (TimeUnit.MILLISECONDS.toMinutes(currentTime - lastActivityTimeInMillis) >= ConfigManager.getTimeOut(getApplicationContext())
                    .getTimeInMinutes()) {
                try {
                    sessionManager.closeSession().subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe();
                    Intent intent = new Intent(LOGOUT_BROADCAST);
                    localBroadcastManager.sendBroadcast(intent);
                } catch (Exception exception) {

                }
            }
        }
    }

    class UserInteractionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            lastActivityTimeInMillis = System.currentTimeMillis();
        }
    }
}
