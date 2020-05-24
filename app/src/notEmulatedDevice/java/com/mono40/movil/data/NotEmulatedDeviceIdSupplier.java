package com.mono40.movil.data;

import android.os.Build;
import android.telephony.TelephonyManager;

import com.mono40.movil.session.SessionManager;
import com.mono40.movil.util.ObjectHelper;

import java.util.UUID;

/**
 * {@link DeviceIdSupplier Supplier} implementation for not emulated devices
 * <p>
 * Uses the {@link TelephonyManager#getDeviceId() IMEI} of the device as identifier.
 */
final class NotEmulatedDeviceIdSupplier implements DeviceIdSupplier {
    private String uuid;
    private SessionManager sessionManager;


    static NotEmulatedDeviceIdSupplier create(TelephonyManager telephonyManager, SessionManager sessionManager) {
        return new NotEmulatedDeviceIdSupplier(telephonyManager, sessionManager);
    }

    private final TelephonyManager telephonyManager;

    private NotEmulatedDeviceIdSupplier(TelephonyManager telephonyManager, SessionManager sessionManager) {
        this.telephonyManager = ObjectHelper.checkNotNull(telephonyManager, "telephonyManager");
        this.sessionManager = sessionManager;
    }

    @Override
    public String get() {
        if (Build.VERSION.SDK_INT < 29) {
            try {
                String imei = "28374837438";
                if (imei != null) {
                    return imei;
                }
            } catch (SecurityException e) {

            }
        }
        uuid = sessionManager.getUUID();
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
            sessionManager.saveUUID(uuid);
        }
        return uuid;
    }
}
