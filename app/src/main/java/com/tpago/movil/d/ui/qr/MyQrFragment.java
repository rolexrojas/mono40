package com.tpago.movil.d.ui.qr;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tpago.movil.BuildConfig;
import com.tpago.movil.R;
import com.tpago.movil.api.Api;
import com.tpago.movil.data.picasso.CircleTransformation;
import com.tpago.movil.dep.App;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.session.User;

import net.glxn.qrgen.android.QRCode;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;


public class MyQrFragment extends Fragment {

    private static final int FILE_EXPORT_REQUEST_CODE = 900;
    @Inject
    SessionManager sessionManager;
    @Inject
    Api api;

    @BindView(R.id.qrCodeImage)
    ImageView qrCodeImageView;
    @BindView(R.id.qr_code_profile_icon)
    CircleImageView qrCodeProfileIcon;
    @BindView(R.id.qr_code_profile_name)
    TextView qrCodeProfileName;
    @BindView(R.id.bottomsheet)
    BottomSheetLayout bottomSheet;
    // Default token for Testing since endpoints are throwing {"error":{"code":"0014","description":"El servicio no está disponible. Favor intente de nuevo.","msisdn":null,"transaction":null}}
    String token = null;
    private Bitmap qrBitmap;
    private Target tPagoLogo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplicationContext()).component().inject(this);
        tPagoLogo = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d("com.tpago.mobile", "profile image loaded");
                MyQrFragment.this.qrBitmap = overlay(MyQrFragment.this.qrBitmap, bitmap);
                MyQrFragment.this.qrCodeImageView.setImageBitmap(MyQrFragment.this.qrBitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.e("com.tpago.mobile", "error loading profile image", e);
                MyQrFragment.this.qrBitmap = overlay(MyQrFragment.this.qrBitmap, BitmapFactory.decodeResource(getResources(), R.drawable.tpago_logo_qr));
                MyQrFragment.this.qrCodeImageView.setImageBitmap(MyQrFragment.this.qrBitmap);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_qr, container, false);
        ButterKnife.bind(this, view);
        getProfileAndQr();
        return view;
    }

    public void getProfileAndQr() {
        User user = sessionManager.getUser();
        this.qrCodeProfileName.setText(user.name().toString());
        Uri uri = user.picture();

        this.getQr();

        Picasso.get()
                .load(uri)
                .resizeDimen(R.dimen.largeImageSize, R.dimen.largeImageSize)
                .transform(new CircleTransformation())
                .noFade()
                .into(qrCodeProfileIcon);
    }

    public void getQr() {
        this.token = sessionManager.getCustomerSecretToken();

        this.qrBitmap = QRCode.from(token)
                .withColor(0xFF9B188F, 0xFFFFFF)
                .withErrorCorrection(ErrorCorrectionLevel.H)
                .withSize(1024, 1024)
                .bitmap();

        User user = sessionManager.getUser();

        Uri uri = user.picture();

        qrCodeImageView.setImageBitmap(qrBitmap);

        if (uri != null) {
            Picasso.get().load(uri)
                    .resizeDimen(R.dimen.largeImageSize, R.dimen.largeImageSize)
                    .transform(new CircleTransformation())
                    .into(tPagoLogo);
        } else {
            MyQrFragment.this.qrBitmap = overlay(MyQrFragment.this.qrBitmap, BitmapFactory.decodeResource(getResources(), R.drawable.tpago_logo_qr));
            MyQrFragment.this.qrCodeImageView.setImageBitmap(MyQrFragment.this.qrBitmap);
        }

    }

    public Bitmap overlay(Bitmap qrBitmap, Bitmap logoBitmap) {
        Bitmap bmOverlay = Bitmap.createBitmap(qrBitmap.getWidth(), qrBitmap.getHeight(), qrBitmap.getConfig());

        Bitmap scaledLogo = Bitmap.createScaledBitmap(logoBitmap, 250, 250, false);
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(qrBitmap, 0, 0, null);


        int cx = (qrBitmap.getWidth() - scaledLogo.getWidth()) >> 1; // same as (...) / 2
        int cy = (qrBitmap.getHeight() - scaledLogo.getHeight()) >> 1;

        canvas.drawBitmap(scaledLogo, cx, cy, null);
        return bmOverlay;
    }

    @OnClick(R.id.qr_cancel)
    public void onCancelClicked() {
        this.getActivity().finish();
    }

    @OnClick(R.id.qr_share)
    public void onShareClicked() {
        showShareChooser();
//        Uri uri = null;
//        try {
//            String bitmapPath = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), this.qrBitmap, "tPagoQr", null);
//            Uri bitmapUri = Uri.parse(bitmapPath);
//
//            Intent exportIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
//            exportIntent.addCategory(Intent.CATEGORY_OPENABLE);
//            exportIntent.setType("image/png");
//            String filename = "MyQrCode.png";
//            exportIntent.putExtra(Intent.EXTRA_TITLE, filename);
//
//            bottomSheet.showWithSheetView(new IntentPickerSheetView(getContext(), exportIntent, "Share with...", new IntentPickerSheetView.OnIntentPickedListener() {
//                @Override
//                public void onIntentPicked(IntentPickerSheetView.ActivityInfo activityInfo) {
//                    bottomSheet.dismissSheet();
//                    startActivityForResult(activityInfo.getConcreteIntent(exportIntent), FILE_EXPORT_REQUEST_CODE);
//                }
//            }));
//        } catch (Exception e) {
//            Log.d(this.getTag(), "Exception while trying to write file for sharing: " + e.getMessage());
//        }
    }

    private void showShareChooser() {
//        String bitmapPath = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), this.qrBitmap, "tPagoQr", null);
//        Uri bitmapUri = Uri.parse(bitmapPath);
//
//        Intent exportIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
//        exportIntent.addCategory(Intent.CATEGORY_OPENABLE);
//        exportIntent.setType("image/png");
//        String filename = "MyQrCode.png";
//        exportIntent.putExtra(Intent.EXTRA_TITLE, filename);
//
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_MEDIA_SHARED);
//        shareIntent.setType("image/*");
//        shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
//
//
//        Intent chooserIntent = Intent.createChooser(shareIntent, "Compartir en...");
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{exportIntent});
        startActivity(getNativeShareIntent(getContext()));

    }

    public Intent getNativeShareIntent(final Context context) {
        String bitmapPath = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), this.qrBitmap, "tPagoQr", null);
        Uri bitmapUri = Uri.parse(bitmapPath);

        final PackageManager pm = context.getPackageManager();
        final Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        sendIntent.setType("image/jpeg");
        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<>();

        for (int i = 0; i < resInfo.size(); i++) {
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            final Intent intent = new Intent();
            intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
            intent.setPackage(packageName);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
            intent.setType("image/jpeg");
            intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm),
                    ri.getIconResource()));
        }

        // TODO Implement the method getSaveToGalleryIntent,
        // Could be a simple intent linking to activity.
        intentList.add(getSaveToGalleryIntent(getContext()));

        Intent openInChooser = Intent.createChooser(intentList.remove(0),
                "Share");
        LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);
        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        return openInChooser;
    }

    private LabeledIntent getSaveToGalleryIntent(final Context context) {
        String bitmapPath = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), this.qrBitmap, "tPagoQr", null);
        Uri bitmapUri = Uri.parse(bitmapPath);

        final Intent intent = new Intent(context, SaveToGalleryActivity.class);
        intent.putExtra(SaveToGalleryActivity.EXTRA_BITMAP_URI, bitmapPath);
        return new LabeledIntent(intent, BuildConfig.APPLICATION_ID,
                "Save to gallery",
                R.drawable.ic_gallery);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("com.tpago.mobile", "OnActivityResult = ResultCode" + resultCode);
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case FILE_EXPORT_REQUEST_CODE:
                Log.d("com.tpago.mobile", "OnActivityResult = " + data);
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        Maybe.fromCallable(() -> {
                            try {
                                OutputStream os = getContext().getContentResolver().openOutputStream(uri);
                                if (os != null) {
                                    qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                                    os.flush();
                                    os.close();
                                }
                            } catch (IOException e) {
                                Log.e("com.tpago.mobile", e.getMessage(), e);
                            }
                            return null;
                        })
                                .doOnError(new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Log.e("com.tpago.mobile", throwable.getMessage(), throwable);
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                    }
                }
                break;
        }
    }

    private class ExportCSV extends AsyncTask<Uri, Void, Boolean> {
        private final WeakReference<Context> context;

        ExportCSV(Context c) {
            context = new WeakReference<>(c);
        }

        @Override
        protected Boolean doInBackground(Uri... uris) {
            Uri uri = uris[0];
            Context c = context.get();

            if (c == null) {
                return false;
            }

            boolean success = false;

            try {
                OutputStream os = c.getContentResolver().openOutputStream(uri);
                if (os != null) {
                    MyQrFragment.this.qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                Log.d("com.tpago.mobile", e.getMessage(), e);
            }
            return success;
        }
    }
}
