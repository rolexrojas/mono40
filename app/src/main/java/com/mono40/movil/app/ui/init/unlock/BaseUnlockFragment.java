package com.mono40.movil.app.ui.init.unlock;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.mono40.movil.R;
import com.mono40.movil.app.ui.activity.ActivityQualifier;
import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.app.ui.fragment.base.FragmentBase;
import com.mono40.movil.app.ui.fragment.FragmentReplacer;
import com.mono40.movil.app.ui.loader.takeover.TakeoverLoader;
import com.mono40.movil.data.DeviceIdSupplier;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.data.picasso.CircleTransformation;
import com.mono40.movil.dep.init.InitFragment;
import com.mono40.movil.dep.init.LogoAnimator;
import com.mono40.movil.reactivex.DisposableUtil;
import com.mono40.movil.session.SessionManager;
import com.mono40.movil.session.UnlockMethodSignatureSupplier;
import com.mono40.movil.session.User;
import com.mono40.movil.util.FailureData;
import com.mono40.movil.util.Result;
import com.mono40.movil.util.StringHelper;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import timber.log.Timber;

public abstract class BaseUnlockFragment extends FragmentBase {

  @BindView(R.id.userPictureImageView) ImageView userPictureImageView;
  @BindView(R.id.userNameTextView) TextView userNameTextView;

  @Inject DeviceIdSupplier deviceIdSupplier;
  @Inject SessionManager sessionManager;

  @Inject StringMapper stringMapper;
  @Inject AlertManager alertManager;
  @Inject TakeoverLoader takeoverLoader;

  FragmentReplacer fragmentReplacer;
  @Inject
  @ActivityQualifier
  FragmentReplacer activityFragmentReplacer;

  @Inject LogoAnimator logoAnimator;

  protected Disposable disposable = Disposables.disposed();

  protected void handleSuccess(Result<?> result) {
    if (result.isSuccessful()) {
      this.activityFragmentReplacer.begin(InitFragment.create())
        .commit();
    } else {
      final FailureData failureData = result.failureData();

      final int code = failureData.code();
      if (code == UnlockMethodSignatureSupplier.FailureCode.UNAUTHORIZED) {
        showErrorMessage("Código incorrecto","El código introducido no coincide con el código utilizado durante la configuración del desbloqueo rápido.");
      } else {
        showErrorMessage(getString(R.string.error_generic_title), StringHelper.isNullOrEmpty(result.failureData().description()) ? getString(R.string.error_generic) : result.failureData().description());
      }
    }
  }

  private void showErrorMessage(String title, String message){
    this.alertManager.builder()
      .title(title)
      .message(message)
      .show();
  }
  protected void handleError(Throwable throwable) {
    Timber.e(throwable, "Opening a session");
    this.alertManager.showAlertForGenericFailure();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.fragmentReplacer = FragmentReplacer
      .create(this.getFragmentManager(), R.id.subcontainerFrameLayout);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public void onResume() {
    super.onResume();

    if(this.sessionManager.isUserSet()){
      final User user = this.sessionManager.getUser();

      Picasso.get()
        .load(user.picture())
        .resizeDimen(R.dimen.normalImageSize, R.dimen.normalImageSize)
        .transform(new CircleTransformation())
        .noFade()
        .into(this.userPictureImageView);

      this.userNameTextView.setText(this.getString(R.string.welcomeUser, user.firstName()));
    }
  }

  @Override
  public void onPause() {
    DisposableUtil.dispose(this.disposable);

    super.onPause();
  }
}
