package com.tpago.movil.d.ui.qr;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.sumimakito.awesomeqr.AwesomeQrRenderer;
import com.github.sumimakito.awesomeqr.RenderResult;
import com.github.sumimakito.awesomeqr.option.RenderOption;
import com.github.sumimakito.awesomeqr.option.color.Color;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.tpago.movil.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyQrFragment extends Fragment {
    @BindView(R.id.qrCodeImage)
    ImageView qrCodeImageView;

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
//        color.setLight(0x9B188F);
        color.setAuto(false);

        RenderOption renderOption = new RenderOption();
        renderOption.setContent("Special, thus awesome."); // content to encode
        renderOption.setSize(800); // size of the final QR code image
        renderOption.setBorderWidth(20); // width of the empty space around the QR code
//        renderOption.setEcl(ErrorCorrectionLevel.M); // (optional) specify an error correction level
//        renderOption.setPatternScale(0.35f); // (optional) specify a scale for patterns
//        renderOption.setRoundedPatterns(true); // (optional) if true, blocks will be drawn as dots instead
//        renderOption.setClearBorder(true); // if set to true, the background will NOT be drawn on the border area
        renderOption.setColor(color); // set a color palette for the QR code
//        renderOption.setBackground(background); // set a background, keep reading to find more about it
//        renderOption.setLogo(logo); // set a logo, keep reading to find more about it

        try {
            RenderResult render = AwesomeQrRenderer.render(renderOption);
            qrCodeImageView.setImageBitmap(render.getBitmap());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
