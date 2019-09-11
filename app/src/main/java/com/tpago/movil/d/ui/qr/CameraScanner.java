package com.tpago.movil.d.ui.qr;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.controls.Flash;
import com.otaliastudios.cameraview.frame.FrameProcessor;
import com.tpago.movil.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CameraScanner extends FrameLayout {
    @BindView(R.id.camera)
    CameraView cameraView;

    public CameraScanner(Context context) {
        super(context);
        inflateView();
    }

    public CameraScanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateView();
    }

    private void inflateView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_camera_scanner,
                this);
        ButterKnife.bind(this);
    }

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        cameraView.setLifecycleOwner(lifecycleOwner);
    }

    public void addFrameProcessor(FrameProcessor frameProcessor) {
        cameraView.addFrameProcessor(frameProcessor);
    }

    public void toggleFacing() {
        cameraView.toggleFacing();
    }

    public void toggleFlash() {
        Flash flash = cameraView.getFlash();
        switch (flash) {
            case OFF:
                cameraView.setFlash(Flash.TORCH);
                break;
            case ON:
            case TORCH:
            default:
                cameraView.setFlash(Flash.OFF);
        }
    }
}
