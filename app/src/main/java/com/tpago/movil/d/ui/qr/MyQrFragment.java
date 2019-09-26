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
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
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
import java.util.concurrent.Callable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import rx.SingleSubscriber;

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
    String token = null;
    private Bitmap qrBitmap;
    TakeoverLoader takeoverLoader;
    private boolean isReady;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplicationContext()).component().inject(this);
        takeoverLoader = TakeoverLoader.create(getChildFragmentManager());
    }

    private void showTakeOver() {
        takeoverLoader.show();

    }

    private void dismissTakeOverLoader() {
        takeoverLoader.hide();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (takeoverLoader != null) {
            if (isVisibleToUser && !isReady) {
                showTakeOver();
            } else {
                dismissTakeOverLoader();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_qr, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupProfile();
        setupQrCode();
    }

    private void setupProfile() {
        User user = sessionManager.getUser();
        this.qrCodeProfileName.setText(user.name().toString());
        Uri uri = user.picture();
        if (uri != null) {
            Picasso.get()
                    .load(uri)
                    .resizeDimen(R.dimen.largeImageSize, R.dimen.largeImageSize)
                    .transform(new CircleTransformation())
                    .noFade()
                    .into(qrCodeProfileIcon);
        } else {
            Picasso.get()
                    .load(R.drawable.tpago_logo_qr)
                    .resizeDimen(R.dimen.largeImageSize, R.dimen.largeImageSize)
                    .transform(new CircleTransformation())
                    .into(qrCodeProfileIcon);
        }
    }

    public static Bitmap getCircledBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    private void setupQrCode() {
        this.token = sessionManager.getCustomerSecretToken();
        Uri userProfilePic = sessionManager.getUser().picture();
        Bitmap userProfileBitmap = null;
        if (userProfilePic != null) {
            try {
                userProfileBitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), userProfilePic);
            } catch (IOException e) {
                e.printStackTrace();
                userProfileBitmap = null;
            }
            if (userProfileBitmap != null) {
                userProfileBitmap = getCircledBitmap(userProfileBitmap);
            }
        }
        Bitmap finalUserProfileBitmap = userProfileBitmap;
        Single.fromCallable(() -> generateQrCode(token, finalUserProfileBitmap)).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        if (bitmap != null) {
                            qrBitmap = bitmap;
                            qrCodeImageView.setImageBitmap(bitmap);
                            dismissTakeOverLoader();
                            isReady = true;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }


    private Bitmap generateQrCode(String customerToken, Bitmap userProfilePic) {
        Bitmap qrBitmap = QRCode.from(customerToken)
                .withColor(0xFF9B188F, 0xFFFFFF)
                .withErrorCorrection(ErrorCorrectionLevel.H)
                .withSize(1024, 1024)
                .bitmap();
        if (userProfilePic == null) {
            return overlay(qrBitmap, BitmapFactory.decodeResource(getResources(), R.drawable.tpago_logo_qr),
                    400, 400);
        } else {
            return overlay(qrBitmap, userProfilePic, 150, 150);
        }
    }

    public Bitmap overlay(Bitmap qrBitmap, Bitmap logoBitmap, int width, int height) {
        Bitmap bmOverlay = Bitmap.createBitmap(qrBitmap.getWidth(), qrBitmap.getHeight(), qrBitmap.getConfig());

        Bitmap scaledLogo = Bitmap.createScaledBitmap(logoBitmap, width, height, false);
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
    }

    private void showShareChooser() {
        startActivity(getNativeShareIntent(getContext()));

    }

    public Intent getNativeShareIntent(final Context context) {
        String bitmapPath = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
                qrBitmap,
                "tPagoQr", null);
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
        String bitmapPath = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
                qrBitmap, "tPagoQr", null);
        Uri bitmapUri = Uri.parse(bitmapPath);

        final Intent intent = new Intent(context, SaveToGalleryActivity.class);
        intent.putExtra(SaveToGalleryActivity.EXTRA_BITMAP_URI, bitmapPath);
        return new LabeledIntent(intent, BuildConfig.APPLICATION_ID,
                "Guardar a la galeria",
                R.drawable.ic_save_to_gallery);
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
                                    ((BitmapDrawable) qrCodeImageView.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, os);
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
                    ((BitmapDrawable) qrCodeImageView.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, os);
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
