package com.tpago.movil.d.ui.qr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.flipboard.bottomsheet.commons.IntentPickerSheetView;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tpago.movil.R;
import com.tpago.movil.api.Api;
import com.tpago.movil.data.picasso.CircleTransformation;
import com.tpago.movil.dep.App;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.session.User;

import net.glxn.qrgen.android.QRCode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class MyQrFragment extends Fragment {

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
        Uri uri = null;
        try {
            String bitmapPath = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), this.qrBitmap, "tPagoQr", null);
            Uri bitmapUri = Uri.parse(bitmapPath);

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/png");
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);

            bottomSheet.showWithSheetView(new IntentPickerSheetView(getContext(), intent, "Share with...", new IntentPickerSheetView.OnIntentPickedListener() {
                @Override
                public void onIntentPicked(IntentPickerSheetView.ActivityInfo activityInfo) {
                    bottomSheet.dismissSheet();
                    startActivity(activityInfo.getConcreteIntent(intent ));
                }
            }));
        } catch (Exception e) {
            Log.d(this.getTag(), "Exception while trying to write file for sharing: " + e.getMessage());
        }
    }
}
