package com.tpago.movil.util;

import android.app.Activity;
import android.content.Context;

import com.tpago.movil.R;
import com.tpago.movil.d.ui.Dialogs;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class RootUtil {

    public static boolean isDeviceRooted() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
    }

    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootMethod2() {
        String[] paths = { "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] { "/system/xbin/which", "su" });
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

    //  TODO: Remove if they are not going to use the root checking
    public static void showRootErrorDialog(Context context, Activity activity) {
        Dialogs.builder(context)
          .setTitle(R.string.error_generic_title)
          .setMessage(R.string.norootdevices)
          .setPositiveButton(R.string.error_positive_button_text, (dialog, which) -> RootUtil.terminateApp(activity))
          .setOnCancelListener((dialog) -> RootUtil.terminateApp(activity))
          .setOnDismissListener((dialog) -> RootUtil.terminateApp(activity))
          .show();
    }

    public static void terminateApp(Activity activity) {
        activity.finish();
        activity.finishAffinity();
        System.exit(0);
    }
}