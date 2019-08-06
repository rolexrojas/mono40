package com.tpago.movil.d.ui.qr;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tpago.movil.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.DebouncingOnClickListener;

public class QrActivity extends AppCompatActivity {
    private static final String MY_QR_CODE_FRAGMENT = "MY_QR_CODE_FRAGMENT";
    private static final String QR_CODE_SCANNER_FRAGMENT = "QR_CODE_SCANNER_FRAGMENT";
    @BindView(R.id.container)
    ViewPager viewPager;
    @BindView(R.id.qrScanTab)
    QrTab qrScanTab;
    @BindView(R.id.myQrTab)
    QrTab myQrTab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.qr_activity_title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new QrScannerFragment();
                } else {
                    return new MyQrFragment();
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPageSelected(int position) {
                qrScanTab.setIsSelected(position == 0);
                qrScanTab.setZ(position == 0 ? 2 : 1);
                myQrTab.setIsSelected(position == 1);
                myQrTab.setZ(position == 1 ? 2 : 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        myQrTab.setOnClickListener(new DebouncingOnClickListener() {
            @Override
            public void doClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        qrScanTab.setOnClickListener(new DebouncingOnClickListener() {
            @Override
            public void doClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
    }
}
