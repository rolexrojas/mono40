package com.tpago.movil.d.ui.qr;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tpago.movil.R;

public class CameraScanner extends FrameLayout {

    public CameraScanner(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_camera_scanner,
                this);
    }
}
