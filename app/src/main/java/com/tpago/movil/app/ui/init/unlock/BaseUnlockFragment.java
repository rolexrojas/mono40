package com.tpago.movil.app.ui.init.unlock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tpago.movil.R;
import com.tpago.movil.api.Api;
import com.tpago.movil.app.ui.ActivityQualifier;
import com.tpago.movil.app.ui.AlertData;
import com.tpago.movil.app.ui.AlertManager;
import com.tpago.movil.app.ui.BaseFragment;
import com.tpago.movil.app.ui.FragmentReplacer;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.data.DeviceIdSupplier;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.dep.init.InitFragment;
import com.tpago.movil.dep.init.LogoAnimator;
import com.tpago.movil.reactivex.DisposableHelper;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.user.User;
import com.tpago.movil.util.FailureData;
import com.tpago.movil.util.Result;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import timber.log.Timber;

public abstract class BaseUnlockFragment extends BaseFragment {

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

      String title = null;
      String message = failureData.description();

      final int code = failureData.code();
      if (code == Api.FailureCode.INCORRECT_CODE) {
        title = "Código incorrecto";
        message
          = "El código introducido no coincide con el código utilizado durante la configuración del desbloqueo rápido.";
      }

      final AlertData alertData = AlertData.builder(this.stringMapper)
        .title(title)
        .message(message)
        .build();

      this.alertManager.show(alertData);
    }
  }

  protected final void handleError(Throwable throwable) {
    Timber.e(throwable, "Opening a session");
    this.alertManager.show(AlertData.createForGenericFailure(this.stringMapper));
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.fragmentReplacer = FragmentReplacer
      .create(this.getFragmentManager(), R.id.subcontainerFrameLayout);
  }

  @Override
  public void onResume() {
    super.onResume();

    final User user = this.sessionManager.getUser();

    Picasso.with(this.getContext())
      .load(user.picture())
      .resizeDimen(R.dimen.normalImageSize, R.dimen.normalImageSize)
      .noFade()
      .into(this.userPictureImageView);

    this.userNameTextView.setText(this.getString(R.string.welcomeUser, user.firstName()));
  }

  @Override
  public void onPause() {
    DisposableHelper.dispose(this.disposable);

    super.onPause();
  }
}
