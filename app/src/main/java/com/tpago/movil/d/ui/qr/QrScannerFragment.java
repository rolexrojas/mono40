package com.tpago.movil.d.ui.qr;

import android.Manifest;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.journeyapps.barcodescanner.camera.CameraSettings;
import com.tpago.movil.R;
import com.tpago.movil.app.ui.permission.PermissionHelper;
import com.tpago.movil.util.QrDecryptUtil;
import com.tpago.movil.util.QrJWT;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QrScannerFragment extends Fragment {
    private static final List<String> REQUIRED_PERMISSIONS_GALLERY;
    private static final List<String> REQUIRED_PERMISSIONS_CAMERA;

    static {
        REQUIRED_PERMISSIONS_GALLERY = new ArrayList<>();
        REQUIRED_PERMISSIONS_GALLERY.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        REQUIRED_PERMISSIONS_GALLERY.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        REQUIRED_PERMISSIONS_CAMERA = new ArrayList<>();
        REQUIRED_PERMISSIONS_CAMERA.addAll(REQUIRED_PERMISSIONS_GALLERY);
        REQUIRED_PERMISSIONS_CAMERA.add(Manifest.permission.CAMERA);
    }

    private static final int REQUEST_CODE_CAMERA = 1;

    @BindView(R.id.cameraPreview)
    DecoratedBarcodeView barcodeView;
    BeepManager beepManager;
    String lastText;
    boolean isFlashOn;
    boolean isFrontCameraOn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_scanner, container, false);
        ButterKnife.bind(this, view);
        if (!PermissionHelper.areGranted(this.getContext(), REQUIRED_PERMISSIONS_CAMERA)) {
            PermissionHelper.requestPermissions(this, REQUEST_CODE_CAMERA, REQUIRED_PERMISSIONS_CAMERA);
        }
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE_CAMERA:
                for(int n = 0; n < permissions.length; n++) {
                    if(grantResults[n] != 0) {
                        this.getActivity().finish();
                        break;
                    }
                }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats, null, null, Intents.Scan.NORMAL_SCAN));
        beepManager = new BeepManager(getActivity());
        barcodeView.initializeFromIntent(getActivity().getIntent());
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result.getText() == null || !result.getText().contains("type=") || result.getText().equals(lastText)) {
                    return;
                }
//                lastText = result.getText();

                String encriptedMessage = result.getText().split("data=")[1];
                beepManager.playBeepSoundAndVibrate();
//                String encriptedMessage = "bFOvDFnfqywdlikH4XAIJOE21WH00MrobLFY3z035ypT5IVLC1zjwpNn99Q1KESD7+fiQATQV4eQoLx/PzpAFlnxyiAGeq1wkyBJjTqmm7z9uQkkXXfkBwJE0l1Z6nTn9sGYHxFXICWLCIaUy7P2hFr0g7HTaqWDsVJ2hFf0zFIXaYIVcXbmEEG9CdTnhoAfeJIb0+/lXU1Iewe+/81n5XLGmv6P21DxNpGcuhZm46t0Wcpz1p6Jq9BrbQM62QHrXyRCfgKzqfZ9mluqz0ef+6De011sd98T5vnllDFLBZOJsDa5iZieG+Ac8t65ZItygCfSVITrxZsEt85O7sRIrJ13vfmbm4vI/j+d2/orODfw1ths1/bv4DesR7hddnivFO71IgUTBx+yLznfN3WzDut88bMPMoimrcmx1jJwogiz79BnUCRqNhTx8G7/hlYIqkvRzZEKbAymsb8mpARXT+xwDD1umJmg1lGlh6ojILhyWHV6i5Jnj2YrG/IeipXfDc5yv+e7AmLjOXAbyUTwTEB+S2hTMDp+gszqiTCntBL5+emxoF63/nHcXGTP6yR4gA3wv7+WR1MRt78kPqYPPqwQvoFfqh09Fwp0awfmc5YrOe6TmhcfHsDdvIC737pOkdpGZ/Cr5YjBK65sNYOWkX/WntFQgr0AJx4QL11UGwc=";
//                String decriptedMessage = "";
//                try {
////                    decriptedMessage = QrDecryptUtil.decrypt(QrDecryptUtil.getPrivateKey("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"
//                    ), encriptedMessage.getBytes());
//                } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(encriptedMessage);
                QrJWT decodedJwt = QrDecryptUtil.decodeJwt(encriptedMessage);
                System.out.println(decodedJwt);
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (barcodeView != null) {
            if (isVisibleToUser) {
                barcodeView.resume();
            } else {
                barcodeView.pauseAndWait();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pauseAndWait();
    }

    @OnClick(R.id.qr_flash)
    public void onFlashClicked() {
        if(this.isFlashOn) {
            this.barcodeView.setTorchOff();
        } else {
            this.barcodeView.setTorchOn();
        }
        isFlashOn = !isFlashOn;
    }

    @OnClick(R.id.qr_camera_flip)
    public void onCameraFlipClicked() {
        CameraSettings cs = this.barcodeView.getBarcodeView().getCameraSettings();
        if(barcodeView.getBarcodeView().isPreviewActive()) {
            barcodeView.pause();
        }
        if(this.isFrontCameraOn) {
            cs.setRequestedCameraId(Camera.CameraInfo.CAMERA_FACING_BACK);
        } else {
            cs.setRequestedCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);
        }
        this.barcodeView.getBarcodeView().setCameraSettings(cs);
        this.barcodeView.resume();
        isFrontCameraOn = !isFrontCameraOn;
    }

    @OnClick(R.id.qr_import)
    public void onImportClicked() {

    }
}
