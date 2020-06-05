package com.mono40.movil.d.ui.qr;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.renderscript.RenderScript;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;

import com.mono40.movil.ServiceInformation.CarServiceInformation;
import com.mono40.movil.ServiceInformation.Maintenance;
import com.mono40.movil.app.ui.activity.toolbar.ActivityToolbar;
import com.mono40.movil.app.ui.main.transaction.paypal.PayPalTransactionArgument;
import com.mono40.movil.d.ui.main.purchase.PurchaseFragment;
import com.mono40.movil.d.ui.main.recipient.index.category.Category;
import com.mono40.movil.d.ui.main.recipient.index.category.RecipientCategoryFragment;
import com.mono40.movil.d.ui.main.recipient.index.category.SecondActivity;
import com.mono40.movil.paypal.PayPalAccount;
import com.mono40.movil.util.QrJwtMnt;
import com.otaliastudios.cameraview.size.Size;
import com.mono40.movil.PhoneNumber;
import com.mono40.movil.R;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.app.ui.alert.Alert;
import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.app.ui.loader.takeover.TakeoverLoader;
import com.mono40.movil.app.ui.permission.PermissionHelper;
import com.mono40.movil.d.domain.Customer;
import com.mono40.movil.d.domain.MerchantRecipient;
import com.mono40.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.mono40.movil.d.domain.PhoneNumberRecipient;
import com.mono40.movil.d.domain.Recipient;
import com.mono40.movil.d.domain.api.ApiResult;
import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.d.ui.Dialogs;
import com.mono40.movil.d.ui.main.transaction.TransactionCategory;
import com.mono40.movil.d.ui.main.transaction.TransactionCreationActivityBase;
import com.mono40.movil.dep.App;
import com.mono40.movil.dep.MimeType;
import com.mono40.movil.dep.init.InitActivityBase;
import com.mono40.movil.session.SessionManager;
import com.mono40.movil.util.QrDecryptUtil;
import com.mono40.movil.util.QrJWT;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
    private Disposable closeSessionDisposable;
    @BindView(R.id.camera_scanner)
    CameraScanner cameraScanner;


    FirebaseVisionBarcodeDetector visionBarcodeDetector;
    long lastRead;
    RenderScript rs;
    private Alert errorDialog;
    TakeoverLoader takeoverLoader;
    private Disposable subscribe;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RenderScript renderScript = RenderScript.create(requireContext());
        ((App) requireActivity().getApplicationContext()).component().inject(this);
        FirebaseVisionBarcodeDetectorOptions options = new FirebaseVisionBarcodeDetectorOptions
                .Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ALL_FORMATS)
                .build();
        visionBarcodeDetector = FirebaseVision.getInstance()
                .getVisionBarcodeDetector(options);
        this.alertManager = AlertManager.create(getContext(), stringMapper);
        takeoverLoader = TakeoverLoader.create(getFragmentManager());
    }

    private void showTakeOver() {
        if (errorDialog == null) {
            takeoverLoader.show();
        }

    }

    private void dismissTakeOverLoader() {
        takeoverLoader.hide();
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

    private Bitmap arrayToBitmap(byte[] data, int width, int height) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, width, height, null);
        yuvImage.compressToJpeg(new Rect(0, 0, width, height), 100, byteArrayOutputStream);
        byte[] array = byteArrayOutputStream.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
        Log.d("com.cryptoqr.mobile", "Testing bitmap");
        return bitmap;
    }

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cameraScanner.setLifecycleOwner(this);
        cameraScanner.addFrameProcessor(frame -> {
            if (isInProgress) {
                return;
            }

            byte[] data = frame.getData();
            int rotation = frame.getRotation();
            long time = frame.getTime();
            Size size = frame.getSize();
            int format = frame.getFormat();

            if (lastRead != 0 && (time - lastRead) <= 500) {
                return;
            }
            lastRead = time;


            FirebaseVisionImageMetadata metaData = new FirebaseVisionImageMetadata.Builder()
                    .setWidth(size.getWidth())
                    .setHeight(size.getHeight())
                    .setRotation(FirebaseVisionImageMetadata.ROTATION_0) // always assuming portrait
                    .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                    .build();

            Bitmap bitmap = rotateBitmap(arrayToBitmap(data, size.getWidth(), size.getHeight()));

            processImage(FirebaseVisionImage.fromBitmap(bitmap));
            isInProgress = true;
        });
    }


    private Bitmap rotateBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();

        matrix.postRotate(90);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }

    void onBarCodeResult(String result) {
        try {
            decipherQrCode(result);
        } catch (Exception ex) {
            Log.e("com.tpago.mobile", "error al desencriptar qr", ex);
            dismissTakeOverLoader();
            showErrorMessage(R.string.qr_error_unsupported_code, R.string.qr_error_unsupported_code_message);
        }
    }

    private void showErrorMessage(int title, int message) {
        dismissTakeOverLoader();
        if (errorDialog != null) {
            errorDialog = null;
        }
        errorDialog = this.alertManager.builder()
                .title(title)
                .message(message)
                .positiveButtonAction(() -> {
                    this.isInProgress = false;
                    errorDialog = null;
                })
                .build();
        errorDialog.show();
    }

    private void decipherQrCode(String data) {
        Log.d("com.cryptoqr.mobile", "qrCodeData = " + data);
        String qrType = null;
        String encryptedMessage = null;
        QrJWT decryptedMessage = null;
        QrJwtMnt decryptedMessageQr = null;
        try {
            qrType = data.split("data=")[0].replace("type=", "").replace(";", "");
            Log.d("com.cryptoqr.mobile", "qrType = " + qrType);
            encryptedMessage = data.split("data=")[1];
            Log.d("com.cryptoqr.mobile", "encripted Message = " + encryptedMessage);
            Log.d("com.cryptoqr.mobile", "encripted key = " + sessionManager.getCustomerSecretKey());
            decryptedMessageQr = QrDecryptUtil.decryptQrData(encryptedMessage, sessionManager.getCustomerSecretKey());
            Log.d("com.cryptoqr.mobile", "decrpited = " + decryptedMessageQr.getSub());
            Log.d("com.cryptoqr.mobile", "decrpited = " + decryptedMessageQr.getIss());
        } catch (Exception e) {
            Log.e("com.cryptoqr.mobile", "error decoding qr", e);
            showErrorMessage(R.string.qr_error_unsupported_code, R.string.qr_error_unsupported_code_message);
            return;
        }
        if (qrType == null || decryptedMessageQr == null) {
            showErrorMessage(R.string.qr_error_unsupported_code, R.string.qr_error_unsupported_code_message);
            return;
        }
        switch (qrType) {
            case "TPNa":
                if (decryptedMessage.getSub() == null) {
                    this.dismissTakeOverLoader();
                    showErrorMessage(R.string.qr_error_unsupported_code, R.string.qr_error_unsupported_code_message);
                    return;
                }
                this.startTransaction(new MerchantRecipient(decryptedMessage.getMerchantDescription(), decryptedMessage.getSub()),
                        TransactionCategory.PAY);
                break;
            case "TRF-INa":
                if (decryptedMessage.getSub() == null) {
                    this.dismissTakeOverLoader();
                    showErrorMessage(R.string.qr_error_unsupported_code, R.string.qr_error_unsupported_code_message);
                    return;
                }
                final PhoneNumber phoneNumber = PhoneNumber.create(decryptedMessage.getSub());
                if (!phoneNumber.equals(sessionManager.getUser().phoneNumber())) {
                    showTakeOver();
                    subscribe = Single.defer(() -> Single.just(this.depApiBridge.fetchCustomer(phoneNumber.value())))
                            .subscribeOn(Schedulers.io())
                            .doFinally(this::dismissTakeOverLoader)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    (result) -> this.handleStartPhoneNumberTransactionResult(phoneNumber, result),
                                    this::handleStartTransactionError
                            );
                } else {
                    this.dismissTakeOverLoader();
                    showErrorMessage(R.string.qr_error_own_code, R.string.qr_error_own_code_message);
                }
                break;
            case "MNT-OUT":
                Log.d("message mnt", "inside mnt-out");
                if (decryptedMessageQr.getSub() == null && decryptedMessageQr.getIss() == null) {
                    this.dismissTakeOverLoader();
                    showErrorMessage(R.string.qr_error_unsupported_code, R.string.qr_error_unsupported_code_message);
                    return;
                }

                 //ScannedQrFragment.create(decryptedMessageQr.carServiceInformation, decryptedMessageQr.maintenance);
                startQrResume(decryptedMessageQr);
                /**
                 * Here will Start activity to display the non-editable carServiceInformation screen (RecipientCategoryFragment.Java)
                 * */
                //this.startTransaction(new SecondActivity(decryptedMessageQr.getMerchantDescription(), decryptedMessage.getSub()),
                     //   TransactionCategory.PAY);
                break;
            default:
                this.dismissTakeOverLoader();
                showErrorMessage(R.string.qr_error_unsupported_code, R.string.qr_error_unsupported_code_message);
                break;
        }

    }


    public void startQrResume(QrJwtMnt decrypted) {
        CarServiceInformation carServiceInformationFromIss = decrypted.getCarServiceInformationFromIss();

        if(carServiceInformationFromIss.getInsuranceNo() == null){
            Log.d("mnt", "NULL CAR SERVICE");
        }
        Log.d("mnt", carServiceInformationFromIss.getMiles());
        Log.d("mnt", carServiceInformationFromIss.getMake());
        Maintenance maintenance = decrypted.getMaintenanceFromSub();

        if(maintenance == null){
            Log.d("mnt", "NULL MAINTENANCE");
        }
        Log.d("mnt", String.valueOf(maintenance.isOilChange()));

        Fragment frag = new ScannedQrFragment(decrypted.getCarServiceInformationFromIss(), decrypted.getMaintenanceFromSub());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.camera_scanner, frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
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


    public void startTransaction(Recipient recipient, TransactionCategory transactionCategory) {
        Intent intent = TransactionCreationActivityBase.getLaunchIntent(
                requireActivity(),
                transactionCategory,
                recipient
        );
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(
                intent
        );
        requireActivity().finish();
    }

    private void handleStartPhoneNumberTransactionResult(
            PhoneNumber phoneNumber,
            ApiResult<Customer> result
    ) {
        if (result.isSuccessful()) {
            this.startTransaction(this.handleCustomerResult(phoneNumber, result),
                    TransactionCategory.TRANSFER);
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
        if (com.mono40.movil.util.StringHelper.isNullOrEmpty(customerName)) {
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
                        requireActivity().finish();
                        break;
                    }
                }
        }
    }

    @OnClick(R.id.qr_flash)
    public void onFlashClicked() {
        cameraScanner.toggleFlash();
        isFlashOn = !isFlashOn;
    }

    @OnClick(R.id.qr_camera_flip)
    public void onCameraFlipClicked() {
        cameraScanner.toggleFacing();
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
                    Log.d("com.cryptoqr.mobile", throwable.getMessage(), throwable);
                });
    }

    private void handleCloseSession() {
        Intent intent = InitActivityBase.getLaunchIntent(getContext());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        requireActivity().finish();
        this.startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_GALLERY:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d("com.cryptoqr.mobile", "image data = " + data.getData());
                    try {
                        this.scanSingleImage(data.getData());
                    } catch (IOException e) {
                        Log.e("com.tpago.mobile", "error decoding image data", e);
                    }
                }
        }
    }

    private void processImage(FirebaseVisionImage image) {
        visionBarcodeDetector.detectInImage(image)
                .addOnSuccessListener(firebaseVisionBarcodes -> {
                    Log.d("com.cryptoqr.mobile", "FirebaseVisionBarcodes = " + firebaseVisionBarcodes.size());
                    if (!firebaseVisionBarcodes.isEmpty()) {
                        isInProgress = true;
                        QrScannerFragment.this.onBarCodeResult(firebaseVisionBarcodes.get(0).getRawValue());
                    } else {
                        isInProgress = false;
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("com.tpago.mobile", "FirebaseVisionBarcodes Error  =", e);
                    showErrorMessage(R.string.qr_error_unsupported_code, R.string.qr_error_unsupported_code_message);
                });
    }

    public void scanSingleImage(Uri uri) throws IOException {
        if (uri != null) {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
            if (bitmap != null) {
                processImage(FirebaseVisionImage.fromBitmap(bitmap));
                return;
            }
        }
        showErrorMessage(R.string.qr_error_unsupported_code, R.string.qr_error_unsupported_code_message);
    }
}
