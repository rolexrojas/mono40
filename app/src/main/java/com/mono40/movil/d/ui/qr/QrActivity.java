package com.mono40.movil.d.ui.qr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.mono40.movil.R;
import com.mono40.movil.d.ui.main.DepMainActivityBase;
import com.mono40.movil.dep.init.InitActivityBase;
import com.mono40.movil.util.LogoutTimerService;

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
    private LocalBroadcastManager localBroadcastManager;
    private LogoutReceiver logoutReceiver;

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
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
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

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        logoutReceiver = new QrActivity.LogoutReceiver();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        localBroadcastManager.sendBroadcast(new Intent(LogoutTimerService.USER_INTERACTION_BROADCAST));
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(LogoutTimerService.LOGOUT_BROADCAST);
        localBroadcastManager.registerReceiver(logoutReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(logoutReceiver);
    }

    class LogoutReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            QrActivity.this.startActivity(InitActivityBase.getLaunchIntent(QrActivity.this));
            QrActivity.this.finish();
        }
    }
}
