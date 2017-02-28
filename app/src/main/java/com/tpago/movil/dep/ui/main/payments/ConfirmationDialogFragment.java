package com.tpago.movil.dep.ui.main.payments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.tpago.movil.R;
import com.tpago.movil.dep.misc.Utils;
import com.tpago.movil.dep.domain.Recipient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author hecvasro
 */
public class ConfirmationDialogFragment extends DialogFragment {
  private static final String FORMAT_ARGUMENT_MISSING = "Argument '%1$s' is missing";

  private static final String EXTRA_RECIPIENT = "recipient";
  private static final String EXTRA_TITLE = "title";
  private static final String EXTRA_MESSAGE = "message";

  private OnSaveButtonClickedListener parent;
  private Recipient recipient;
  private String title;
  private String message;
  private Unbinder unbinder;

  @Nullable
  @BindView(R.id.recipient_alias_label)
  TextInputLayout identifierTextInputLayout;
  @Nullable
  @BindView(R.id.recipient_alias)
  EditText identifierEditText;

  /**
   * TODO
   *
   * @param recipient
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public static ConfirmationDialogFragment newInstance(@NonNull Recipient recipient,
    @NonNull String title, @Nullable String message) {
    final Bundle bundle = new Bundle();
    bundle.putSerializable(EXTRA_RECIPIENT, recipient);
    if (!TextUtils.isEmpty(title)) {
      bundle.putString(EXTRA_TITLE, title);
    }
    if (!TextUtils.isEmpty(message)) {
      bundle.putString(EXTRA_MESSAGE, message);
    }
    final ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    // Attaches the parent to the fragment.
    final Fragment fragment = getParentFragment();
    if (Utils.isNotNull(fragment)) {
      if (!(fragment instanceof OnSaveButtonClickedListener)) {
        throw new ClassCastException("Parent must implement the 'OnSaveButtonClickedListener' interface");
      } else {
        parent = (OnSaveButtonClickedListener) fragment;
      }
    } else {
      final Activity activity = getActivity();
      if (!(activity instanceof OnSaveButtonClickedListener)) {
        throw new ClassCastException("Parent must implement the 'OnSaveButtonClickedListener' interface");
      } else {
        parent = (OnSaveButtonClickedListener) activity;
      }
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final Bundle bundle = Utils.isNotNull(savedInstanceState) ? savedInstanceState : getArguments();
    if (Utils.isNull(bundle)) {
      throw new NullPointerException(String.format(
        "Arguments '%1$s', '%2$s' and '%3$s' are missing", EXTRA_RECIPIENT, EXTRA_TITLE,
        EXTRA_MESSAGE));
    } else if (!bundle.containsKey(EXTRA_RECIPIENT)) {
      throw new NullPointerException(String.format(FORMAT_ARGUMENT_MISSING, EXTRA_RECIPIENT));
    } else if (!bundle.containsKey(EXTRA_TITLE)) {
      throw new NullPointerException(String.format(FORMAT_ARGUMENT_MISSING, EXTRA_TITLE));
    } else {
      recipient = (Recipient) bundle.getSerializable(EXTRA_RECIPIENT);
      title = bundle.getString(EXTRA_TITLE);
      message = bundle.getString(EXTRA_MESSAGE);
    }
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final Activity activity = getActivity();
    final AlertDialog.Builder builder = new AlertDialog.Builder(activity)
      .setTitle(title)
      .setMessage(message)
      .setNegativeButton(R.string.action_skip, null)
      .setPositiveButton(R.string.action_save, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          parent.onSaveButtonClicked(recipient, identifierEditText.getText().toString().trim());
        }
      });
    final View view = LayoutInflater.from(activity)
      .inflate(R.layout.dialog_recipient_addition_confirmation, null);
    unbinder = ButterKnife.bind(this, view);
    if (identifierEditText != null) {
      identifierEditText.setText(recipient.getLabel());
    }
    builder.setView(view);
    return builder.create();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    // Unbinds all the annotated views and methods.
    if (Utils.isNotNull(unbinder)) {
      unbinder.unbind();
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    // Detaches the parent from the fragment.
    parent = null;
  }

  /**
   * TODO
   */
  public interface OnSaveButtonClickedListener {
    /**
     * TODO
     *
     * @param recipient
     *   TODO
     * @param label
     *   TODO
     */
    void onSaveButtonClicked(@NonNull Recipient recipient, @Nullable String label);
  }
}
