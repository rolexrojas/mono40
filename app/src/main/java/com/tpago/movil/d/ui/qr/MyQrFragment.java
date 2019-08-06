package com.tpago.movil.d.ui.qr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sumimakito.awesomeqr.option.logo.Logo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tpago.movil.R;
import com.tpago.movil.api.Api;
import com.tpago.movil.data.picasso.CircleTransformation;
import com.tpago.movil.dep.App;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.session.User;

import net.glxn.qrgen.android.QRCode;

import java.io.File;
import java.io.FileOutputStream;

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
    Logo logo;
    // Default token for Testing since endpoints are throwing {"error":{"code":"0014","description":"El servicio no está disponible. Favor intente de nuevo.","msisdn":null,"transaction":null}}
    String token = null;
    private Bitmap qrBitmap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplicationContext()).component().inject(this);
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
        this.logo = new Logo();
        this.logo.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo_qr_tpago_1080));
        Uri uri = user.picture();

        if (uri == null) {
            this.getQr();
            return;
        }

        Picasso.get()
                .load(uri)
                .resizeDimen(R.dimen.largeImageSize, R.dimen.largeImageSize)
                .transform(new CircleTransformation())
                .placeholder(R.drawable.avatar_monster_32)
                .error(R.drawable.avatar_monster_32)
                .noFade()
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        // Setting profile icon to image at the top
                        qrCodeProfileIcon.setImageBitmap(bitmap);
                        // Setting profile icon to logo on QR
                        logo.setBitmap(bitmap);
                        // Generating My QR
                        getQr();
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
    }

    public void getQr() {
        this.token = sessionManager.getCustomerSecretToken();

        Bitmap qrBitmap = QRCode.from(token).withColor(0xFF9B188F, 0xFFFFFF).withSize(500, 500).bitmap();

        User user = sessionManager.getUser();

        Uri uri = user.picture();

        if (user.picture() != null) {

            Picasso.get().load(uri).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    MyQrFragment.this.qrBitmap = overlay(qrBitmap, bitmap);
                    qrCodeImageView.setImageBitmap(MyQrFragment.this.qrBitmap);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        } else {
            MyQrFragment.this.qrBitmap = overlay(qrBitmap, BitmapFactory.decodeResource(getResources(), R.drawable.tpago_logo_qr));
            qrCodeImageView.setImageBitmap(MyQrFragment.this.qrBitmap);
        }
    }

    public Bitmap overlay(Bitmap qrBitmap, Bitmap logoBitmap) {
        Bitmap bmOverlay = Bitmap.createBitmap(qrBitmap.getWidth(), qrBitmap.getHeight(), qrBitmap.getConfig());

        Bitmap scaledLogo = Bitmap.createScaledBitmap(logoBitmap, 150, 150, false);
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(qrBitmap, new Matrix(), null);


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
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            File file = new File(this.getActivity().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "my-qr-code.png");
            FileOutputStream stream = new FileOutputStream(file);
            this.qrBitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.close();
            uri = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/png");
            startActivity(intent);
        } catch (Exception e) {
            Log.d(this.getTag(), "Exception while trying to write file for sharing: " + e.getMessage());
        }
    }
}
