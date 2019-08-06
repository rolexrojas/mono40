package com.tpago.movil.dep.init;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.R;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.app.ui.activity.ActivityQualifier;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.app.ui.init.unlock.UnlockFragment;
import com.tpago.movil.app.ui.permission.PermissionHelper;
import com.tpago.movil.app.ui.permission.PermissionRequestResult;
import com.tpago.movil.app.upgrade.AppUpgradeManager;
import com.tpago.movil.d.domain.InitialDataLoader;
import com.tpago.movil.d.ui.main.DepMainActivityBase;
import com.tpago.movil.dep.init.intro.IntroFragment;
import com.tpago.movil.reactivex.DisposableUtil;
import com.tpago.movil.session.SessionDataLoader;
import com.tpago.movil.session.SessionManager;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class InitFragment extends BaseInitFragment {

    private static final int REQUEST_CODE = 0;

    private static final List<String> REQUIRED_PERMISSIONS = Arrays
            .asList(Manifest.permission.READ_PHONE_STATE);

    @Inject
    AppUpgradeManager upgradeManager;
    @Inject
    SessionManager sessionManager;
    @Inject
    SessionDataLoader sessionDataLoader;

    @Inject
    AlertManager alertManager;
    @Inject
    StringMapper stringMapper;
    @Inject
    @ActivityQualifier
    FragmentReplacer fragmentReplacer;

    @Inject
    LogoAnimator logoAnimator;
    @Inject
    InitialDataLoader initialDataLoader;

    private Disposable upgradeDisposable = Disposables.disposed();
    private Disposable sessionDataDisposable = Disposables.disposed();

    private boolean werePermissionsRequested = false;
    private boolean shouldLoadInitData = false;

    AlertDialog permissionAlert;

    public static InitFragment create() {
        return new InitFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Injects all annotated dependencies.
        this.getInitComponent()
                .inject(this);

        this.configurePermissionAlert();
    }

    private void handleError(Throwable throwable, String message) {
        Timber.e(throwable, message);
        this.alertManager.showAlertForGenericFailure();
    }

    private void resetAndStartLogoAnimator(Disposable disposable) {
        this.logoAnimator.reset();
        this.logoAnimator.start();
    }

    private void handleLoadInitDataSuccess() {
        final Activity activity = this.getActivity();
        activity.startActivity(DepMainActivityBase.getLaunchIntent(activity));
        activity.finish();
    }

    private void handleLoadInitDataError(Throwable throwable) {
        this.handleError(throwable, "Loading initial data");
    }

    @Deprecated
    private void loadInitDataFromInitialDataLoader() {
        this.initialDataLoader.load(sessionManager)
                .await();
    }

    private void loadInitData() {
        if (!this.sessionManager.isUserSet()) {
            this.fragmentReplacer.begin(IntroFragment.create())
                    .transition(FragmentReplacer.Transition.SRFO)
                    .commit();
        } else if (!this.sessionManager.isSessionOpen()) {
            this.fragmentReplacer.begin(UnlockFragment.create())
                    .transition(FragmentReplacer.Transition.FIFO)
                    .commit();
        } else {
            this.sessionDataDisposable = this.sessionDataLoader.load()
                    .concatWith(Completable.fromAction(this::loadInitDataFromInitialDataLoader))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(this::resetAndStartLogoAnimator)
                    .subscribe(this::handleLoadInitDataSuccess, this::handleLoadInitDataError);
        }
    }

    private void handleUpgradeSuccess() {
        final Context context = this.getContext();
        if (PermissionHelper.areGranted(context, REQUIRED_PERMISSIONS)) {
            this.loadInitData();
        } else if (!this.werePermissionsRequested) {
            this.werePermissionsRequested = true;
            PermissionHelper.requestPermissions(this, REQUEST_CODE, REQUIRED_PERMISSIONS);
        } else {
            // TODO: Let the user know that those permissions are required.
        }
    }

    private void handleUpgradeError(Throwable throwable) {
        this.handleError(throwable, "Upgrading");
    }

    @Override
    public void onStart() {
        super.onStart();
        this.upgradeDisposable = this.upgradeManager.upgrade()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleUpgradeSuccess, this::handleUpgradeError);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.shouldLoadInitData) {
            this.shouldLoadInitData = false;
            this.loadInitData();
        }
    }

    @Override
    public void onStop() {
        DisposableUtil.dispose(this.sessionDataDisposable);
        DisposableUtil.dispose(this.upgradeDisposable);
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] results
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, results);
        if (requestCode == REQUEST_CODE) {
            final PermissionRequestResult result = PermissionRequestResult.create(permissions, results);
            if (result.isSuccessful()) {
                this.shouldLoadInitData = true;
            } else {
                this.showPermissionAlert();
            }
        }
    }

    private void configurePermissionAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage(R.string.request_for_permission);
        builder1.setCancelable(false);

        builder1.setPositiveButton(R.string.go_to_configurations, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                getContext().startActivity(intent);
                getActivity().finish();
            }
        });
        permissionAlert = builder1.create();
        permissionAlert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                permissionAlert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.d_widget_text_input_light_erratic));
                permissionAlert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.d_widget_text_input_light_erratic));
            }
        });
    }

    private void showPermissionAlert() {
        permissionAlert.show();
    }
}
