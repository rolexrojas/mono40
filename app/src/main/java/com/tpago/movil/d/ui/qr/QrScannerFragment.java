package com.tpago.movil.d.ui.qr;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.journeyapps.barcodescanner.camera.CameraManager;
import com.journeyapps.barcodescanner.camera.CameraSettings;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;
import com.otaliastudios.cameraview.size.Size;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.R;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoaderDialogFragment;
import com.tpago.movil.app.ui.permission.PermissionHelper;
import com.tpago.movil.d.domain.Customer;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.PhoneNumberRecipient;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.d.ui.main.DepMainActivityBase;
import com.tpago.movil.d.ui.main.transaction.TransactionCategory;
import com.tpago.movil.d.ui.main.transaction.TransactionCreationActivityBase;
import com.tpago.movil.dep.App;
import com.tpago.movil.dep.MimeType;
import com.tpago.movil.dep.init.InitActivityBase;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.util.QrDecryptUtil;
import com.tpago.movil.util.QrJWT;

import org.apache.commons.codec.DecoderException;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.CAMERA_SERVICE;

public class QrScannerFragment extends Fragment {
    private static final List<String> REQUIRED_PERMISSIONS_GALLERY;
    private static final List<String> REQUIRED_PERMISSIONS_CAMERA;
    private static final int REQUEST_CODE_TRANSACTION_CREATION = 1;
    private static final String TAKE_OVER_LOADER_DIALOG = "TAKE_OVER_LOADER_DIALOG";

    static {
        REQUIRED_PERMISSIONS_GALLERY = new ArrayList<>();
        REQUIRED_PERMISSIONS_GALLERY.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        REQUIRED_PERMISSIONS_GALLERY.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        REQUIRED_PERMISSIONS_CAMERA = new ArrayList<>();
        REQUIRED_PERMISSIONS_CAMERA.addAll(REQUIRED_PERMISSIONS_GALLERY);
        REQUIRED_PERMISSIONS_CAMERA.add(Manifest.permission.CAMERA);
    }

    private static final int REQUEST_CODE_GALLERY = 400;
    private static final int REQUEST_CODE_CAMERA = 500;

    //    @BindView(R.id.cameraPreview)
//    CustomBarCodeView barcodeView;
    BeepManager beepManager;
    boolean isFlashOn;
    boolean isFrontCameraOn;
    @Inject
    SessionManager sessionManager;
    @Inject
    DepApiBridge depApiBridge;
    boolean isInProgress;
    AlertManager alertManager;
    @Inject
    StringMapper stringMapper;
    TakeoverLoaderDialogFragment takeoverLoader;
    private Disposable closeSessionDisposable;
    @BindView(R.id.camera_scanner)
    CameraScanner cameraScanner;
    FirebaseVisionBarcodeDetector visionBarcodeDetector;
    long lastRead;
    private boolean processingImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplicationContext()).component().inject(this);
        FirebaseVisionBarcodeDetectorOptions options = new FirebaseVisionBarcodeDetectorOptions
                .Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE, FirebaseVisionBarcode.FORMAT_AZTEC)
                .build();
        visionBarcodeDetector = FirebaseVision.getInstance()
                .getVisionBarcodeDetector(options);
        this.alertManager = AlertManager.create(getContext(), stringMapper);
    }

    private void showTakeOver() {
        if (takeoverLoader != null) {
            takeoverLoader.dismiss();
        } else {
            takeoverLoader = TakeoverLoaderDialogFragment.create();
            takeoverLoader.show(getActivity().getSupportFragmentManager(), TAKE_OVER_LOADER_DIALOG);
        }

    }

    private void dismissTakeOverLoader() {
        if (takeoverLoader != null) {
            takeoverLoader.dismiss();
            takeoverLoader = null;
        }
        isInProgress = false;
    }

    public int degreesToFirebaseRotation(int degrees) {
        switch (degrees) {
            case 0:
                return FirebaseVisionImageMetadata.ROTATION_0;
            case 90:
                return FirebaseVisionImageMetadata.ROTATION_90;
            case 180:
                return FirebaseVisionImageMetadata.ROTATION_180;
            case 270:
                return FirebaseVisionImageMetadata.ROTATION_270;
            default:
                throw new IllegalArgumentException(
                        "Rotation must be 0, 90, 180, or 270.");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_scanner, container, false);
        ButterKnife.bind(this, view);
        if (!PermissionHelper.areGranted(this.getContext(), REQUIRED_PERMISSIONS_CAMERA)) {
            PermissionHelper.requestPermissions(this, REQUEST_CODE_CAMERA, REQUIRED_PERMISSIONS_CAMERA);
        }
        cameraScanner.setLifecycleOwner(this);
        cameraScanner.addFrameProcessor(frame -> {
            if (processingImage) {
                return;
            }
            byte[] data = frame.getData();
            int rotation = frame.getRotation();
            long time = frame.getTime();
            Size size = frame.getSize();
            int format = frame.getFormat();

//            if (lastRead != 0 && (time - lastRead) <= 1000) {
//                return;
//            }
            lastRead = time;
            FirebaseVisionImageMetadata metaData = new FirebaseVisionImageMetadata.Builder()
                    .setWidth(360)
                    .setHeight(480)
                    .setRotation(FirebaseVisionImageMetadata.ROTATION_0) // always assuming portrait
                    .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                    .build();

            processImage(FirebaseVisionImage.fromByteArray(data, metaData));
        });
        return view;
    }

    void onBarCodeResult(String result) {
        try {
            decipherQrCode(result);
        } catch (Exception ex) {
            Log.e("com.tpago.mobile", "error al desencriptar qr", ex);
            showErrorMessage(R.string.qr_error_unsupported_code, R.string.qr_error_unsupported_code_message);
        }
    }

    private void showErrorMessage(int title, int message) {
        dismissTakeOverLoader();
        this.alertManager.builder()
                .title(title)
                .message(message)
                .positiveButtonAction(() -> this.isInProgress = false)
                .show();
    }

    private void decipherQrCode(String data) {
        this.showTakeOver();
        Log.d("com.tpago.mobile", "qrCodeData = " + data);
        String encriptedMessage = data.split("data=")[1];
        QrJWT decriptedMessage = null;
        try {
            Log.d("com.tpago.mobile", "encripted Message = " + sessionManager.getCustomerSecretKey());
            Log.d("com.tpago.mobile", "encripted key = " + sessionManager.getCustomerSecretKey());
            decriptedMessage = QrDecryptUtil.decryptData(encriptedMessage, sessionManager.getCustomerSecretKey());
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | DecoderException | IllegalBlockSizeException e) {
            Log.e("com.tpago.mobile", "error decoding qr", e);
            showErrorMessage(R.string.qr_error_unsupported_code, R.string.qr_error_unsupported_code_message);
        }
        Log.d("com.tpago.mobile", "decoded qr code = " + decriptedMessage.getSub());
        final PhoneNumber phoneNumber = PhoneNumber.create(decriptedMessage.getSub());
        if (!phoneNumber.equals(sessionManager.getUser().phoneNumber())) {
            Single.defer(() -> Single.just(this.depApiBridge.fetchCustomer(phoneNumber.value())))
                    .subscribeOn(Schedulers.io())
                    .doFinally(() -> this.dismissTakeOverLoader())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            (result) -> this.handleStartPhoneNumberTransactionResult(phoneNumber, result),
                            this::handleStartTransactionError
                    );
        } else {
            showErrorMessage(R.string.qr_error_own_code, R.string.qr_error_own_code_message);
        }

    }

    private void handleStartTransactionError(Throwable throwable) {
        dismissTakeOverLoader();
        showGenericErrorDialog(getString(R.string.cannot_process_your_request_at_the_moment));
    }

    public void showGenericErrorDialog(String message) {
        Dialogs.builder(getContext())
                .setTitle(R.string.error_generic_title)
                .setMessage(message)
                .setPositiveButton(R.string.error_positive_button_text, (dialog, which) -> {
                    isInProgress = false;
                    if (message.contains(getString(R.string.session_expired))) {
                        this.closeSession();
                    }
                })
                .show();
    }


    public void startTransaction(Recipient recipient) {
        Intent intent = TransactionCreationActivityBase.getLaunchIntent(
                this.getActivity(),
                TransactionCategory.TRANSFER,
                recipient
        );
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(
                intent
        );
        getActivity().finish();
    }

    private void handleStartPhoneNumberTransactionResult(
            PhoneNumber phoneNumber,
            ApiResult<Customer> result
    ) {
        if (result.isSuccessful()) {
            this.isInProgress = false;
            this.startTransaction(this.handleCustomerResult(phoneNumber, result));
        } else {
            this.showGenericErrorDialog(result.getError().getDescription());
        }
    }

    private Recipient handleCustomerResult(PhoneNumber phoneNumber, ApiResult<Customer> result) {
        String customerName = null;
        if (result.isSuccessful()) {
            customerName = result.getData()
                    .getName();
        }
        if (com.tpago.movil.util.StringHelper.isNullOrEmpty(customerName)) {
            return new NonAffiliatedPhoneNumberRecipient(phoneNumber, customerName);
        } else {
            return new PhoneNumberRecipient(phoneNumber, customerName);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                for (int n = 0; n < permissions.length; n++) {
                    if (grantResults[n] != 0) {
                        this.getActivity().finish();
                        break;
                    }
                }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (barcodeView != null) {
//            if (isVisibleToUser) {
//                barcodeView.resume();
//            } else {
//                barcodeView.pauseAndWait();
//            }
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        barcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
//        barcodeView.pauseAndWait();
    }

    @OnClick(R.id.qr_flash)
    public void onFlashClicked() {
        if (this.isFlashOn) {
//            this.barcodeView.setTorchOff();
        } else {
//            this.barcodeView.setTorchOn();
        }
        isFlashOn = !isFlashOn;
    }

    @OnClick(R.id.qr_camera_flip)
    public void onCameraFlipClicked() {
//        CameraSettings cs = this.barcodeView.getBarcodeView().getCameraSettings();
//        if (barcodeView.getBarcodeView().isPreviewActive()) {
//            barcodeView.pause();
//        }
//        if (this.isFrontCameraOn) {
//            cs.setRequestedCameraId(Camera.CameraInfo.CAMERA_FACING_BACK);
//        } else {
//            cs.setRequestedCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);
//        }
//        this.barcodeView.getBarcodeView().setCameraSettings(cs);
//        this.barcodeView.resume();
        isFrontCameraOn = !isFrontCameraOn;
    }

    @OnClick(R.id.qr_import_container)
    public void onImportClicked() {
        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(MimeType.IMAGE);
        this.startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    private void closeSession() {
        this.closeSessionDisposable = this.sessionManager.closeSession()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> this.showTakeOver())
                .doFinally(this::dismissTakeOverLoader)
                .subscribe(this::handleCloseSession, (Consumer<Throwable>) throwable -> {
                    Log.d("com.tpago.mobile", throwable.getMessage(), throwable);
                });
    }

    private void handleCloseSession() {
        Intent intent = InitActivityBase.getLaunchIntent(getContext());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.getActivity().finish();
        this.startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_GALLERY:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d("com.tpago.mobile", "image data = " + data.getData());
                    try {
                        this.scanSingleImage(data.getData());
                    } catch (IOException e) {
                        Log.e("com.tpago.mobile", "error decoding image data", e);
                    }
                }
        }
    }

    private void processImage(FirebaseVisionImage image) {
        processingImage = true;
        visionBarcodeDetector.detectInImage(image)
                .addOnSuccessListener(firebaseVisionBarcodes -> {
                    Log.d("com.tpago.mobile", "FirebaseVisionBarcodes = " + firebaseVisionBarcodes.size());
                    if (!firebaseVisionBarcodes.isEmpty()) {
                        isInProgress = true;
                        showTakeOver();
                        QrScannerFragment.this.onBarCodeResult(firebaseVisionBarcodes.get(0).getRawValue());
                        processingImage = false;
                    }
                    processingImage = false;
                })
                .addOnFailureListener(e -> {
                    Log.e("com.tpago.mobile", "FirebaseVisionBarcodes Error  =", e);
                    showErrorMessage(R.string.qr_error_unsupported_code, R.string.qr_error_unsupported_code_message);
                    processingImage = false;
                });
    }

    public void scanSingleImage(Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
        processImage(FirebaseVisionImage.fromBitmap(bitmap));
    }
}
