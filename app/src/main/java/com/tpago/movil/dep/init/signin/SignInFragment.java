package com.tpago.movil.dep.init.signin;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.FragmentActivityBase;
import com.tpago.movil.app.ui.activity.ActivityQualifier;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.app.ui.init.unlock.ChangePassword.ChangePasswordFragment;
import com.tpago.movil.app.ui.init.unlock.EmailPasswordFragment;
import com.tpago.movil.app.ui.main.settings.auth.alt.AltAuthMethodFragment;
import com.tpago.movil.dep.init.BaseInitFragment;
import com.tpago.movil.dep.init.InitFragment;
import com.tpago.movil.dep.init.LogoAnimator;
import com.tpago.movil.dep.text.BaseTextWatcher;
import com.tpago.movil.dep.widget.Keyboard;
import com.tpago.movil.dep.widget.TextInput;
import com.tpago.movil.util.ChangePasswordRadioMenuUtil;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.RadioGroupUtil;
import com.tpago.movil.util.UiUtil;
import com.tpago.movil.util.function.Action;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class SignInFragment extends BaseInitFragment implements SignInPresenter.View {

    public static SignInFragment create() {
        return new SignInFragment();
    }

    private Unbinder unbinder;

    private TextWatcher emailTextInputTextWatcher;
    private TextWatcher passwordTextInputTextWatcher;

    private SignInPresenter presenter;
    private Dialog restorePasswordDialog;
    private int selectedOption = 0;
    private boolean shouldInit;

    @Inject
    @ActivityQualifier
    FragmentReplacer fragmentReplacer;

    @Inject
    LogoAnimator logoAnimator;
    @Inject
    KeyguardManager keyguardManager;
    @Inject
    FingerprintManagerCompat fingerprintManager;
    @Inject
    AlertManager alertManager;

    @BindView(R.id.text_input_email)
    TextInput emailTextInput;
    @BindView(R.id.text_input_password)
    TextInput passwordTextInput;
    @BindView(R.id.button_sign_in)
    Button signInButton;

    @OnClick(R.id.button_sign_in)
    void onSignInButtonClicked() {
        presenter.onSignInButtonClicked();
    }

    @OnClick(R.id.userForgotPassword)
    final void onUserForgotPasswordViewClicked() {
        if (ObjectHelper.isNull(restorePasswordDialog)) {
            restorePasswordDialog = createDialog();
        }
        restorePasswordDialog.show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Injects all the annotated dependencies.
        getInitComponent()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Binds all the annotated resources, views and methods.
        unbinder = ButterKnife.bind(this, view);
        // Creates the presenter.
        presenter = new SignInPresenter(this, getInitComponent());
    }

    @Override
    public void onStart() {
        super.onStart();
        // Notifies the presenter that the view its being started.
        presenter.onViewStarted();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Moves the logo out of the screen.
        logoAnimator.moveOutOfScreen();
        // Shows the keyboard.
        Keyboard.show(emailTextInput);
        // Attaches the email text input to the presenter.
        emailTextInputTextWatcher = new BaseTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                presenter.onEmailTextInputContentChanged(s.toString());
            }
        };
        emailTextInput.addTextChangedListener(emailTextInputTextWatcher);
        // Attaches the password text input to the presenter.
        passwordTextInputTextWatcher = new BaseTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                presenter.onPasswordTextInputContentChanged(s.toString());
            }
        };
        passwordTextInput.addTextChangedListener(passwordTextInputTextWatcher);
        passwordTextInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    presenter.onSignInButtonClicked();
                }
                return false;
            }
        });

        if (shouldInit) {
            gotToInit();
        }
    }

    private void gotToInit() {
        fragmentReplacer.begin(InitFragment.create())
                .transition(FragmentReplacer.Transition.FIFO)
                .commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Detaches the last updateName text input from the presenter.
        passwordTextInput.setOnEditorActionListener(null);
        passwordTextInput.removeTextChangedListener(passwordTextInputTextWatcher);
        passwordTextInputTextWatcher = null;
        // Detaches the first updateName text input from the presenter.
        emailTextInput.removeTextChangedListener(emailTextInputTextWatcher);
        emailTextInputTextWatcher = null;
        restorePasswordDialog = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        // Notifies the presenter that the view its being stopped.
        presenter.onViewStopped();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Destroys the presenter.
        presenter = null;
        // Unbinds all the annotated resources, views and method.
        unbinder.unbind();
    }

    private Dialog createDialog() {
        String[] groupName = {getString(R.string.reset_with_email)};
        Dialog dialog = ChangePasswordRadioMenuUtil.createChangePasswordRadioMenuDialog(getActivity());
        RadioGroup radioGroup = dialog.findViewById(R.id.radio_group);

        TextView cancel = dialog.findViewById(R.id.cancel_action);
        TextView confirm = dialog.findViewById(R.id.do_action);
        UiUtil.setEnabled(confirm, false);

        cancel.setOnClickListener((view) -> dialog.cancel());

        confirm.setOnClickListener((view -> {
            radioGroup.setOnCheckedChangeListener(null);
            radioGroup.clearCheck();
            radioGroup.removeAllViews();
            dialog.cancel();
            switch (selectedOption) {
                case 0:
                    moveTo(EmailPasswordFragment.create());
                    break;
            }
        }));

        RadioGroupUtil.setRadioButtons(radioGroup, groupName, getActivity());

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            selectedOption = Integer.valueOf(group.getCheckedRadioButtonId());
            UiUtil.setEnabled(confirm, true);
        });

        return dialog;
    }

    private void moveTo(Fragment screen) {
        this.fragmentReplacer.begin(screen)
                .transition(FragmentReplacer.Transition.FIFO)
                .addToBackStack(true, "Email")
                .commit();
    }

    @Override
    public void setEmailTextInputContent(String content) {
        emailTextInput.setText(content);
    }

    @Override
    public void showEmailTextInputAsErratic(boolean showAsErratic) {
        emailTextInput.setErraticStateEnabled(showAsErratic);
    }

    @Override
    public void setPasswordTextInputContent(String content) {
        passwordTextInput.setText(content);
    }

    @Override
    public void showPasswordTextInputAsErratic(boolean showAsErratic) {
        passwordTextInput.setErraticStateEnabled(showAsErratic);
    }

    @Override
    public void setSignInButtonEnabled(boolean enabled) {
        signInButton.setEnabled(enabled);
    }

    @Override
    public void showSignInButtonAsEnabled(boolean showAsEnabled) {
        UiUtil.setEnabled(signInButton, showAsEnabled);
    }

    public void showActivationFingerprintMessage() {
        this.alertManager.builder()
                .message(
                        getString(R.string.activatefingerprintmessage)
                )
                .negativeButtonText("Seguridad")
                .negativeButtonAction(
                        () -> this.startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS))
                )
                .show();
    }

    public void openChangePassword(boolean shouldRequestPIN, boolean shouldCloseSession, boolean shouldDestroySession) {
        this.startActivity(
                FragmentActivityBase.createLaunchIntent(
                        this.getContext(),
                        ChangePasswordFragment.creator()
                ).putExtra(ChangePasswordFragment.SHOULD_REQUEST_PIN, shouldRequestPIN)
                        .putExtra(ChangePasswordFragment.SHOULD_CLOSE_SESSION, shouldCloseSession)
                        .putExtra(ChangePasswordFragment.SHOULD_DESTROY_SESSION, shouldDestroySession)
        );
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void moveToInitScreen() {
        showAltUnlockMethods();
    }

    public void showAltUnlockMethods() {
        this.shouldInit = true;
        this.alertManager.builder()
                .title(R.string.altUnlockMethod)
                .message(R.string.altUnlockMethodEnableQuestion)
                .positiveButtonText(R.string.enableAsVerb)
                .positiveButtonAction(
                        () -> this.startActivity(
                                FragmentActivityBase.createLaunchIntent(
                                        this.getContext(),
                                        AltAuthMethodFragment.creator()
                                )
                        )
                )
                .negativeButtonText(R.string.laterAsVerb)
                .negativeButtonAction(new Action() {
                    @Override
                    public void run() {
                     gotToInit();
                    }
                })
                .show();
    }

}
