package com.tpago.movil.app.ui.main.settings.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tpago.movil.R;
import com.tpago.movil.app.di.ComponentBuilderSupplier;
import com.tpago.movil.app.di.ComponentBuilderSupplierContainer;
import com.tpago.movil.app.ui.FragmentActivityBase;
import com.tpago.movil.app.ui.fragment.base.BaseFragmentModule;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.fragment.FragmentQualifier;
import com.tpago.movil.app.ui.init.unlock.ChangePassword.ChangePasswordFragment;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.app.ui.main.BaseMainFragment;
import com.tpago.movil.d.ui.main.DepMainActivityBase;
import com.tpago.movil.data.picasso.CircleTransformation;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.dep.init.InitActivityBase;
import com.tpago.movil.dep.widget.TextInput;
import com.tpago.movil.reactivex.DisposableUtil;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.util.ChangePasswordRadioMenuUtil;
import com.tpago.movil.util.RadioGroupUtil;
import com.tpago.movil.util.UiUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class ProfileFragment extends BaseMainFragment implements ProfilePresentation,
        ComponentBuilderSupplierContainer {

    public static ProfileFragment create() {
        return new ProfileFragment();
    }

    private Disposable destroySessionDisposable = Disposables.disposed();
    private int selectedOption = 0;
    FragmentActivityBase activity;
    @Inject
    TakeoverLoader takeoverLoader;

    @BindView(R.id.pictureImageView)
    ImageView pictureImageView;
    @BindView(R.id.firstNameTextInput)
    TextInput firstNameTextInput;
    @BindView(R.id.lastNameTextInput)
    TextInput lastNameTextInput;
    @BindView(R.id.phoneNumberTextInput)
    TextInput phoneNumberTextInput;
    @BindView(R.id.emailTextInput)
    TextInput emailTextInput;

    @Inject
    @FragmentQualifier
    ComponentBuilderSupplier componentBuilderSupplier;

    @Inject
    AlertManager alertManager;
    @Inject
    ProfilePresenter presenter;
    @Inject
    SessionManager sessionManager;
    @Inject
    StringMapper stringMapper;

    Disposable disposable;
    MenuItem submitButton;

    @OnClick(R.id.pictureImageView)
    final void onPictureImageViewClicked() {
        this.presenter.onUserPictureClicked();
    }

    @OnClick(R.id.changeMyPasswordSettingsOption)
    final void onChangePasswordButtonPressed() {
        createDialog().show();
    }

    @StringRes
    @Override
    protected int titleResId() {
        return R.string.profile;
    }

    @Override
    protected String subTitle() {
        return "";
    }

    @Override
    @LayoutRes
    protected int layoutResId() {
        return R.layout.profile;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Injects all annotated dependencies.
        DepMainActivityBase.get(this.getActivity())
                .getComponent()
                .create(BaseFragmentModule.create(this), ProfileModule.create(this))
                .inject(this);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.settings_profile_change_password, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        this.submitButton = menu.findItem(R.id.menu_item_submit);
        this.hideSaveButton();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_submit) {
            updateProfile();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firstNameTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSaveButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lastNameTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSaveButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void updateSaveButton() {
        boolean firstNameChanged = !sessionManager.getUser().firstName().equals(firstNameTextInput.getText().toString());
        boolean lastNameChanged = !sessionManager.getUser().lastName().equals(lastNameTextInput.getText().toString());
        if (firstNameChanged || lastNameChanged) {
            showSaveButton();
        } else {
            hideSaveButton();
        }
    }

    private void updateProfile() {
        this.disposable = sessionManager.requestEditProfile(firstNameTextInput.getText().toString(), lastNameTextInput.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((disposable) -> this.takeoverLoader.show())
                .doFinally(this.takeoverLoader::hide)
                .subscribe(() -> handleUpdateProfileSuccess(), this::handleError);
    }

    private void handleError(Throwable throwable) {
        Timber.e(throwable, "Updating profile");
        this.alertManager.showAlertForGenericFailure();
    }

    @Override
    public void onResume() {
        super.onResume();

        this.presenter.onPresentationResumed();
        updateSaveButton();
    }

    @Override
    public void onPause() {
        DisposableUtil.dispose(this.destroySessionDisposable);
        DisposableUtil.dispose(this.disposable);

        this.presenter.onPresentationPaused();

        super.onPause();
    }

    private Dialog createDialog() {
        String[] groupName = {getString(R.string.reset_with_pin), getString(R.string.reset_with_email)};
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
                    openChangePassword(true, false);
                    break;
                case 1:
                    changePasswordWithEmail();
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

    @Override
    public void openChangePassword(boolean shouldRequestPIN, boolean shouldCloseSession) {
        this.startActivity(
                FragmentActivityBase.createLaunchIntent(
                        this.getContext(),
                        ChangePasswordFragment.creator()
                ).putExtra(ChangePasswordFragment.SHOULD_REQUEST_PIN, shouldRequestPIN)
                        .putExtra(ChangePasswordFragment.SHOULD_CLOSE_SESSION, shouldCloseSession)
        );
    }

    private void changePasswordWithEmail() {
        sessionManager.requestForgotPassword(this.sessionManager.getUser().email().value())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((disposable) -> this.takeoverLoader.show())
                .doFinally(this.takeoverLoader::hide)
                .subscribe(() -> handleSuccess(), (error) -> this.alertManager.showAlertForGenericFailure());
    }

    private void handleSuccess() {
        this.alertManager.builder()
                .title(R.string.recipient_addition_title)
                .message(R.string.request_password_sucess_email)
                .show();
    }

    private void handleUpdateProfileSuccess() {
        this.alertManager.builder()
                .title(R.string.request_profile_update_title)
                .message(R.string.request_profile_update_sucess)
                .show();
    }

    private void handleSignOutSuccess() {
        final Activity activity = this.getActivity();
        activity.startActivity(InitActivityBase.getLaunchIntent(activity));
        activity.finish();
    }

    private void handleSignOutFailure(Throwable throwable) {
        Timber.e(throwable, "Signing out");
        this.alertManager.showAlertForGenericFailure();
    }

    private void confirmSignOut() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setTitle(R.string.closeSession);
        builder1.setMessage(R.string.unlinkMessage);
        builder1.setCancelable(true);

        builder1.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                destroySessionDisposable = sessionManager.destroySession().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe((d) -> takeoverLoader.show())
                        .doFinally(takeoverLoader::hide)
                        .subscribe(() -> handleSignOutSuccess(), (t) -> handleSignOutFailure(t));
            }
        });

        builder1.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        AlertDialog permissionAlert = builder1.create();
        permissionAlert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                permissionAlert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.d_widget_text_input_light_erratic));
                permissionAlert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.d_widget_text_input_light_erratic));
            }
        });
        permissionAlert.show();
    }

    @OnClick(R.id.signOutSettingsOption)
    final void onSignOutSettingsOptionClicked() {
        this.confirmSignOut();
    }

    @Override
    public void setUserPicture(Uri uri) {
        Picasso.get()
                .load(uri)
                .resizeDimen(R.dimen.largeImageSize, R.dimen.largeImageSize)
                .transform(new CircleTransformation())
                .placeholder(R.drawable.profile_picture_placeholder_light)
                .error(R.drawable.profile_picture_placeholder_light)
                .noFade()
                .into(this.pictureImageView);
    }

    private void showSaveButton() {
        if (this.submitButton != null) {
            this.submitButton.setVisible(true);
        }
    }

    private void hideSaveButton() {
        if (this.submitButton != null) {
            this.submitButton.setVisible(false);
        }
    }

    @Override
    public void setUserFirstName(String content) {
        this.firstNameTextInput.setText(content);
    }

    @Override
    public void setUserLastName(String content) {
        this.lastNameTextInput.setText(content);
    }

    @Override
    public void setUserPhoneNumber(String content) {
        this.phoneNumberTextInput.setText(content);
    }

    @Override
    public void setUserEmail(String content) {
        this.emailTextInput.setText(content);
    }

    @Override
    public ComponentBuilderSupplier componentBuilderSupplier() {
        return this.componentBuilderSupplier;
    }
}
