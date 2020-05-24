package com.mono40.movil.app.ui.main.settings.bankdetail;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.auto.value.AutoValue;
import com.squareup.picasso.Picasso;
import com.mono40.movil.R;
import com.mono40.movil.app.ui.FragmentCreator;
import com.mono40.movil.app.ui.fragment.base.FragmentBase;
import com.mono40.movil.app.ui.main.settings.MultiLineSettingsOption;
import com.mono40.movil.d.ui.main.DepMainActivityBase;
import com.mono40.movil.util.Bank;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class
BankDetailFragment extends FragmentBase {

    public BankDetailFragment() {
        // Required empty public constructor
    }

    public static BankDetailFragment newInstance(int bankCode, String bankName, String bankLogoURL) {
        Bundle bundle = new Bundle();
        bundle.putInt("bankCode", bankCode);
        bundle.putString("bankName", bankName);
        bundle.putString("bankLogoURL", bankLogoURL);
        BankDetailFragment fragment = new BankDetailFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private Bank bank;
    private int bankCode;
    private String bankName;
    private String bankLogoURL;
    private Drawable drawImage;

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            bankCode = bundle.getInt("bankCode");
            bankName = bundle.getString("bankName");
            bankLogoURL = bundle.getString("bankLogoURL");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        //Getting the arguments for the fragment
        readBundle(this.getArguments());
        bank = new Bank(this.bankCode, this.bankName, this.bankLogoURL);

        //Loading the logo
        ImageView image = new ImageView(this.getContext());
        Picasso.get().load(this.bank.getLogoURL()).into(image);

        this.drawImage = image.getDrawable();
        this.setBankData();
        this.hideEmptyEntries();
    }

    public void setBankData(){
        //Updating Bank Info

        MultiLineSettingsOption emailView = this.getView().findViewById(R.id.sendemail);
        emailView.secondaryText(this.bank.getEmail());

        MultiLineSettingsOption domesticCallView = this.getView().findViewById(R.id.domesticcall);
        domesticCallView.secondaryText(this.bank.getDomesticPhone());

        MultiLineSettingsOption freeCallView = this.getView().findViewById(R.id.freecall);
        freeCallView.secondaryText(this.bank.getFreeCallPhone());

        MultiLineSettingsOption bankURLView = this.getView().findViewById(R.id.bankurl);
        bankURLView.secondaryText(this.bank.getURL());
    }

    public void hideEmptyEntries(){
        if(this.bank.getEmail().isEmpty()){
            getActivity().findViewById(R.id.sendemail).setVisibility(View.GONE);
            getActivity().findViewById(R.id.emailseparator).setVisibility(View.GONE);
        }

        if(this.bank.getDomesticPhone().isEmpty()){
            getActivity().findViewById(R.id.domesticcall).setVisibility(View.GONE);
            getActivity().findViewById(R.id.domesticcallseparator).setVisibility(View.GONE);
        }

        if(this.bank.getFreeCallPhone().isEmpty()){
            getActivity().findViewById(R.id.freecall).setVisibility(View.GONE);
            getActivity().findViewById(R.id.freecallseparator).setVisibility(View.GONE);
        }

        if(this.bank.getURL().isEmpty()){
            getActivity().findViewById(R.id.bankurl).setVisibility(View.GONE);
        }

    }
    @Override
    public void onResume() {
        super.onResume();

        DepMainActivityBase.get(this.getActivity())
                .toolbar()
                .setLogo(drawImage);

        DepMainActivityBase.get(this.getActivity())
                .toolbar()
                .setTitle(this.bankName);
    }

    @Override
    public void onPause() {
        super.onPause();
        DepMainActivityBase.get(this.getActivity())
                .toolbar()
                .setLogo(null);
    }

    @OnClick(R.id.sendemail)
    final void onEmailPressed() {
        String URL = bank.getEmail();
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + URL));
        startActivity(i);
    }

    @OnClick(R.id.domesticcall)
    final void onDomesticCallPhonePressed() {
        String URL = bank.getDomesticPhone();
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + URL));
        startActivity(i);
    }

    @OnClick(R.id.freecall)
    final void onFreeCallPhonePressed() {
        String URL =bank.getFreeCallPhone();
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + URL));
        startActivity(i);
    }

    @OnClick(R.id.bankurl)
    final void onSitePressed() {
        String URL = bank.getURL();
        openLinkIntent("http://" + URL);
    }

    private final void openLinkIntent(String URL) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        startActivity(i);
    }

    @Override
    protected int layoutResId() {
        return R.layout.fragment_bank_detail;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_bank_detail, container, false);
    }

}
