package com.tpago.movil.app.ui.init.unlock;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import com.google.auto.value.AutoValue;
import com.tpago.movil.Email;
import com.tpago.movil.R;
import com.tpago.movil.app.ui.FragmentCreator;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.dep.init.InitActivityBase;
import com.tpago.movil.dep.text.BaseTextWatcher;
import com.tpago.movil.dep.widget.TextInput;
import com.tpago.movil.util.StringHelper;
import com.tpago.movil.util.UiUtil;


import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EmailPasswordFragment extends BaseUnlockFragment {
	@BindView(R.id.unlockButton)
	Button unlockButton;

	@BindView(R.id.userPasswordTextInput)
	TextInput userEmailTextInput;

	private String userEmail;
	private TextWatcher userEmailTextWatcher;

	public static EmailPasswordFragment create() {
		return new EmailPasswordFragment();
	}

	public static FragmentCreator creator() {
		return  new AutoValue_EmailPasswordFragment_Creator();
	}

	@Override
	protected int layoutResId() {
		return R.layout.fragment_email_password;
	}

	protected void handleSuccess() {
		this.alertManager.builder()
			.title(R.string.recipient_addition_title)
			.message(R.string.request_password_sucess_email)
			.positiveButtonAction(() -> moveToNextScreen())
			.show();
	}

	private void moveToNextScreen() {
		if (!this.sessionManager.isUserSet()) {
			this.fragmentReplacer.manager().popBackStack();
		} else if (!this.sessionManager.isSessionOpen()) {
			this.fragmentReplacer.begin(UnlockFragment.create())
				.transition(FragmentReplacer.Transition.FIFO)
				.commit();
		}
	}
	@Override
	protected void handleError(Throwable throwable) {
		this.alertManager.builder()
			.title(R.string.request_password_error_title)
			.message(R.string.request_password_error_email)
			.show();
	}

	@OnClick(R.id.unlockButton)
	final void onUnlockButtonClicked() {
		if (Email.isValid(this.userEmail)) {
			sessionManager.requestForgotPassword(this.userEmail)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.doOnSubscribe((disposable) -> this.takeoverLoader.show())
				.doFinally(this.takeoverLoader::hide)
				.subscribe(() -> handleSuccess(), this::handleError);
		} else {
			UiUtil.setEnabled(this.unlockButton, false);
			this.userEmailTextInput.setErraticStateEnabled(true);
			this.alertManager.builder()
				.title(R.string.request_password_error_title)
				.message(R.string.request_password_bad_email)
				.show();
		}
	}

	private void afterUserEmailTextInputChanged(String s) {
		this.userEmail = s;
		if (!StringHelper.isNullOrEmpty(this.userEmail)) {
			UiUtil.setEnabled(this.unlockButton, true);
			this.userEmailTextInput.setErraticStateEnabled(false);
		} else {
			UiUtil.setEnabled(this.unlockButton, false);
		}
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		InitActivityBase.get(this.getActivity())
			.getInitComponent()
			.inject(this);
		UiUtil.setEnabled(this.unlockButton, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		this.userEmailTextWatcher = new BaseTextWatcher() {
				@Override
				public void afterTextChanged(Editable s) {
			afterUserEmailTextInputChanged(s.toString());
				}
		};
		this.userEmailTextInput.addTextChangedListener(this.userEmailTextWatcher);
		this.userEmailTextInput.setOnEditorActionListener((v, id, e) -> {
			if (id == EditorInfo.IME_ACTION_DONE) {
				this.onUnlockButtonClicked();
				}
			return false;
		});
	}

	@Override
	public void onPause() {
		this.userEmailTextInput.setOnEditorActionListener(null);
		this.userEmailTextInput.removeTextChangedListener(this.userEmailTextWatcher);
		this.userEmailTextWatcher = null;
		super.onPause();
	}

	@AutoValue
	public static abstract class Creator extends FragmentCreator {

		Creator() {}

		@Override
		public Fragment create() {
			return new EmailPasswordFragment();
		}
	}
}
