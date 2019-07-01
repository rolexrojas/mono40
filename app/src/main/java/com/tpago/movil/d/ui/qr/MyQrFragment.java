package com.tpago.movil.d.ui.qr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.github.sumimakito.awesomeqr.AwesomeQrRenderer;
import com.github.sumimakito.awesomeqr.RenderResult;
import com.github.sumimakito.awesomeqr.option.RenderOption;
import com.github.sumimakito.awesomeqr.option.color.Color;
import com.github.sumimakito.awesomeqr.option.logo.Logo;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.tpago.movil.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MyQrFragment extends Fragment {
    @BindView(R.id.qrCodeImage)
    ImageView qrCodeImageView;
    RenderResult render;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_qr, container, false);
        ButterKnife.bind(this, view);
        getQr();
        return view;
    }

    public void getQr() {
        Color color = new Color(false, 0xFFFFFF, 0xFFFFFF, 0xFF9B188F);
        color.setAuto(false);

        Logo logo = new Logo();
        logo.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo_qr_tpago_1080));
        logo.setScale(0.3f);


        RenderOption renderOption = new RenderOption();
        renderOption.setLogo(logo);
        renderOption.setContent("Special, thus awesome."); // content to encode
        renderOption.setSize(800); // size of the final QR code image
        renderOption.setBorderWidth(20); // width of the empty space around the QR code
        renderOption.setEcl(ErrorCorrectionLevel.H); // (optional) specify an error correction level
        renderOption.setRoundedPatterns(true);
        renderOption.setPatternScale(0.35f); // (optional) specify a scale for patterns
        renderOption.setColor(color); // set a color palette for the QR code

        try {
            this.render = AwesomeQrRenderer.render(renderOption);
            qrCodeImageView.setImageBitmap(this.render.getBitmap());

        } catch (Exception e) {
            e.printStackTrace();
        }
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
            this.render.getBitmap().compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.close();
            uri = Uri.fromFile(file);
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/png");
            startActivity(intent);
        } catch (Exception e) {
            Log.d(this.getTag(), "Exception while trying to write file for sharing: " + e.getMessage());
        }
    }
}
