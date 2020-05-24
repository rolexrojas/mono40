package com.mono40.movil.util;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.mono40.movil.dep.App;
import com.mono40.movil.dep.ConfigManager;
import com.mono40.movil.session.SessionManager;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LogoutTimerService extends IntentService {
    private final long COUNT_DOWN_TIME_MS = TimeUnit.SECONDS.toMillis(15);
    private Timer timer;
    @Inject
    SessionManager sessionManager;
    private LocalBroadcastManager localBroadcastManager;
    public static final String LOGOUT_BROADCAST = "LOGOUT_BROADCAST";
    public static final String USER_INTERACTION_BROADCAST = "USER_INTERACTION_BROADCAST";
    private long lastActivityTimeInMillis;
    boolean isRunning;
    boolean shouldStop;

    public LogoutTimerService() {
        super("LogoutTimerService");
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent i) {
        if (!isRunning) {
            sessionManager = DaggerLogoutTimerServiceComponent.builder().appComponent(((App) getApplication()).component()).build().sessionManager();
            localBroadcastManager = LocalBroadcastManager.getInstance(this);
            localBroadcastManager.registerReceiver(new UserInteractionReceiver(), new IntentFilter(USER_INTERACTION_BROADCAST));
            isRunning = true;
            shouldStop = false;
            while (!shouldStop) {
                if (lastActivityTimeInMillis > 0) {
                    long currentTime = System.currentTimeMillis();
                    if (TimeUnit.MILLISECONDS.toMinutes(currentTime - lastActivityTimeInMillis) >= ConfigManager.getTimeOut(getApplicationContext())
                            .getTimeInMinutes()) {
                        try {
                            sessionManager.closeSession().subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe();
                            Intent intent = new Intent(LOGOUT_BROADCAST);
                            localBroadcastManager.sendBroadcast(intent);
                            isRunning = false;
                            shouldStop = true;
                        } catch (Exception exception) {

                        }
                    }
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
