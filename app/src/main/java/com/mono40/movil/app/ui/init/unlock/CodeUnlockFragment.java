package com.mono40.movil.app.ui.init.unlock;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mono40.movil.Code;
import com.mono40.movil.R;
import com.mono40.movil.app.ui.fragment.FragmentReplacer;
import com.mono40.movil.app.ui.DNumPad;
import com.mono40.movil.session.CodeMethodSignatureSupplier;
import com.mono40.movil.dep.init.InitActivityBase;
import com.mono40.movil.session.SessionManager;
import com.mono40.movil.util.function.Action;
import com.mono40.movil.util.function.Consumer;
import com.mono40.movil.util.digit.Digit;
import com.mono40.movil.util.digit.DigitValueCreator;
import com.mono40.movil.util.Result;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hecvasro
 */
public final class CodeUnlockFragment extends BaseUnlockFragment {

  static CodeUnlockFragment create() {
    return new CodeUnlockFragment();
  }

  private DigitValueCreator<Code> codeCreator = Code.creator();

  @BindView(R.id.valueTextView) TextView valueTextView;

  @BindView(R.id.num_pad) DNumPad DNumPad;
  private Consumer<Integer> numPadDigitConsumer;
  private Action numPadDeleteAction;

  @Inject SessionManager sessionManager;
  @Inject CodeMethodSignatureSupplier.Creator codeSignatureSupplierCreator;

  private void updateValueTextView() {
    this.valueTextView.setText(this.codeCreator.toString());
  }

  private void onNumPadDigitButtonClicked(@Digit int digit) {
    this.codeCreator.addDigit(digit);

    this.updateValueTextView();

    if (this.codeCreator.canCreate()) {
      this.disposable = this.codeSignatureSupplierCreator
        .create(this.codeCreator.create())
        .get()
        .flatMap((result) -> {
          if (result.isSuccessful()) {
            return this.sessionManager
              .openSession(result.successData(), this.deviceIdSupplier.get());
          } else {
            return Single.just(Result.create(result.failureData()));
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe((d) -> this.takeoverLoader.show())
        .doFinally(this.takeoverLoader::hide)
        .subscribe(this::handleSuccess, this::handleError);
    }
  }

  private void onNumPadDeleteButtonClicked() {
    this.codeCreator.removeLastDigit();

    this.updateValueTextView();
  }

  @Override
  protected void handleSuccess(Result<?> result) {
    super.handleSuccess(result);

    if (!result.isSuccessful()) {
      this.codeCreator.clear();
      this.updateValueTextView();
    }
  }

  @OnClick(R.id.usePasswordTextView)
  final void onUserPasswordTextViewClicked() {
    this.fragmentReplacer.begin(PasswordUnlockFragment.create())
      .transition(FragmentReplacer.Transition.FIFO)
      .commit();
  }

  @Override
  protected int layoutResId() {
    return R.layout.unlock_code;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    InitActivityBase.get(this.getActivity())
      .getInitComponent()
      .inject(this);
  }

  @Override
  public void onResume() {
    super.onResume();

    this.logoAnimator.moveOutOfScreen();

    this.valueTextView.setText(this.codeCreator.toString());

    this.numPadDigitConsumer = this::onNumPadDigitButtonClicked;
    this.DNumPad.addDigitConsumer(this.numPadDigitConsumer);
    this.numPadDeleteAction = this::onNumPadDeleteButtonClicked;
    this.DNumPad.addDeleteAction(this.numPadDeleteAction);
  }

  @Override
  public void onPause() {
    this.DNumPad.removeDeleteAction(this.numPadDeleteAction);
    this.numPadDeleteAction = null;
    this.DNumPad.removeDigitConsumer(this.numPadDigitConsumer);
    this.numPadDigitConsumer = null;

    super.onPause();
  }
}
