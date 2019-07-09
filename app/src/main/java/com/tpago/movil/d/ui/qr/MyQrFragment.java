package com.tpago.movil.d.ui.qr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.github.sumimakito.awesomeqr.AwesomeQrRenderer;
import com.github.sumimakito.awesomeqr.RenderResult;
import com.github.sumimakito.awesomeqr.option.RenderOption;
import com.github.sumimakito.awesomeqr.option.color.Color;
import com.github.sumimakito.awesomeqr.option.logo.Logo;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tpago.movil.R;
import com.tpago.movil.api.Api;
import com.tpago.movil.data.picasso.CircleTransformation;
import com.tpago.movil.dep.App;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.session.User;

import java.io.File;
import java.io.FileOutputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


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
    String token = "type=TRF-IN;data=bFOvDFnfqywdlikH4XAIJOE21WH00MrobLFY3z035ypT5IVLC1zjwpNn99Q1KESD7+fiQATQV4eQoLx/" +
            "PzpAFlnxyiAGeq1wkyBJjTqmm7z9uQkkXXfkBwJE0l1Z6nTn9sGYHxFXICWLCIaUy7P2hFr0g7HTaqWDsVJ2hFf0zFIXaYIVcXbm" +
            "EEG9CdTnhoAfeJIb0+/lXU1Iewe+/81n5XLGmv6P21DxNpGcuhZm46t0Wcpz1p6Jq9BrbQM62QHrXyRCfgKzqfZ9mluq" +
            "z0ef+6De011sd98T5vnllDFLBZOJsDa5iZieG+Ac8t65ZItygCfSVITrxZsEt85O7sRIrJ13vfmbm4vI/j+d2/orODfw1ths1/bv4" +
            "DesR7hddnivFO71IgUTBx+yLznfN3WzDut88bMPMoimrcmx1jJwogiz79BnUCRqNhTx8G7/hlYIqkvRzZEKbAymsb8mpA" +
            "RXT+xwDD1umJmg1lGlh6ojILhyWHV6i5Jnj2YrG/IeipXfDc5yv+e7AmLjOXAbyUTwTEB+S2hTMDp+gszqiTCntBL5+emx" +
            "oF63/nHcXGTP6yR4gA3wv7+WR1MRt78kPqYPPqwQvoFfqh09Fwp0awfmc5YrOe6TmhcfHsDdvIC737pOkdpGZ/Cr5Y" +
            "jBK65sNYOWkX/WntFQgr0AJx4QL11UGwc=";
    RenderResult render;

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
        this.logo.setScale(0.3f);
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
        api.getQrForCustomer()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((result) -> {
                    if (result.isSuccessful()) {
                        this.token = result.successData().token();
                    }

                    Color color = new Color(false, 0xFFFFFF, 0xFFFFFF, 0xFF9B188F);
                    color.setAuto(false);

                    RenderOption renderOption = new RenderOption();
                    if (this.logo != null) renderOption.setLogo(this.logo);
                    renderOption.setContent(this.token); // content to encode
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
                });
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
