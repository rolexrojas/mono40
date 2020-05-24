package com.mono40.movil.util;

import android.view.View;

public class UiUtil {
    public static void setEnabled(View view, boolean enabled) {
        view.setAlpha(enabled ? 1.0f : 0.5f);
        view.setEnabled(enabled);
    }
}
