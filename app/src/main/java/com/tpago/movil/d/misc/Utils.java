package com.tpago.movil.d.misc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import java.util.Arrays;

/**
 * @author hecvasro
 */
@Deprecated
public final class Utils {

    private Utils() {
    }

    public static boolean isNull(@Nullable Object object) {
        return object == null;
    }

    public static boolean isNotNull(@Nullable Object object) {
        return !isNull(object);
    }

    public static int hashCode(@NonNull Object... objects) {
        return Arrays.hashCode(objects);
    }

    public static void hideSoftKeyboard(AppCompatActivity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        AppCompatActivity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
