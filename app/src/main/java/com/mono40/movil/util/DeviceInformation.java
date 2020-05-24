package com.mono40.movil.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.crashlytics.android.Crashlytics;

public class DeviceInformation {
    public static String getAppVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Crashlytics.logException(e);
            return "";
        }
    }

    public static String getOS() {
        return "Android";
    }
}
