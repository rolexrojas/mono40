package com.tpago.movil.data;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.tpago.movil.session.SessionManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class DeviceIdSupplierModule {

    @Provides
    @Singleton
    TelephonyManager telephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    @Provides
    @Singleton
    DeviceIdSupplier deviceIdSupplier(TelephonyManager telephonyManager, SessionManager sessionManager) {
        return NotEmulatedDeviceIdSupplier.create(telephonyManager, sessionManager);
    }
}
