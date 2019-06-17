package com.tpago.movil.d.ui.qr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.tpago.movil.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QrActivity extends AppCompatActivity {
    private static final String MY_QR_CODE_FRAGMENT = "MY_QR_CODE_FRAGMENT";
    private static final String QR_CODE_SCANNER_FRAGMENT = "QR_CODE_SCANNER_FRAGMENT";
    @BindView(R.id.container)
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        ButterKnife.bind(this);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new QrScannerFragment();
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        viewPager.setCurrentItem(0);
//        QrScannerFragment qrScannerFragment = new QrScannerFragment();
//        findViewById(R.id.container);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container, qrScannerFragment, QR_CODE_SCANNER_FRAGMENT)
//                .commit();
    }
}
