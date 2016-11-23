package com.gbh.movil.ui.main.payments;

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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.gbh.movil.R;
import com.gbh.movil.Utils;
import com.gbh.movil.domain.Recipient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author hecvasro
 */
public class RecipientAdditionConfirmationDialogFragment extends DialogFragment {
  private static final String ARGUMENT_RECIPIENT = "recipient";

  private OnSaveRecipientButtonClickedListener parent;
  private Recipient recipient;
  private Unbinder unbinder;

  @BindView(R.id.recipient_identifier_label)
  TextInputLayout identifierTextInputLayout;
  @BindView(R.id.recipient_identifier)
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
  public static RecipientAdditionConfirmationDialogFragment newInstance(@NonNull Recipient recipient) {
    final Bundle bundle = new Bundle();
    bundle.putSerializable(ARGUMENT_RECIPIENT, recipient);
    final RecipientAdditionConfirmationDialogFragment fragment
      = new RecipientAdditionConfirmationDialogFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    // Attaches the parent to the fragment.
    final Fragment fragment = getParentFragment();
    if (Utils.isNotNull(fragment)) {
      if (!(fragment instanceof OnSaveRecipientButtonClickedListener)) {
        throw new ClassCastException("Parent must implement the 'OnSaveRecipientButtonClickedListener' interface");
      } else {
        parent = (OnSaveRecipientButtonClickedListener) fragment;
      }
    } else {
      final Activity activity = getActivity();
      if (!(activity instanceof OnSaveRecipientButtonClickedListener)) {
        throw new ClassCastException("Parent must implement the 'OnSaveRecipientButtonClickedListener' interface");
      } else {
        parent = (OnSaveRecipientButtonClickedListener) activity;
      }
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final Bundle bundle = Utils.isNotNull(savedInstanceState) ? savedInstanceState : getArguments();
    if (Utils.isNull(bundle) || !bundle.containsKey(ARGUMENT_RECIPIENT)) {
      throw new NullPointerException("Argument '" + ARGUMENT_RECIPIENT + "' is missing");
    } else {
      recipient = (Recipient) bundle.getSerializable(ARGUMENT_RECIPIENT);
    }
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final Activity activity = getActivity();
    final AlertDialog.Builder builder = new AlertDialog.Builder(activity)
      .setTitle(R.string.contact_added_title)
      .setMessage(String
        .format(getString(R.string.contact_added_message), recipient.getIdentifier()))
      .setNegativeButton(R.string.action_skip, null)
      .setPositiveButton(R.string.action_save, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          parent.onSaveRecipientButtonClicked(recipient,
            identifierEditText.getText().toString().trim());
        }
      });
    // Inflates the custom view of the dialog.
    final View view = LayoutInflater.from(activity)
      .inflate(R.layout.dialog_recipient_addition_confirmation, null);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Prepares the custom view of the dialog.
    final String label = recipient.getLabel();
    if (Utils.isNotNull(label)) {
      identifierEditText.setText(label);
    }
    builder.setView(view);
    // Creates the dialog of the fragment.
    return builder.create();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
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
  public interface OnSaveRecipientButtonClickedListener {
    /**
     * TODO
     *
     * @param recipient
     *   TODO
     * @param label
     *   TODO
     */
    void onSaveRecipientButtonClicked(@NonNull Recipient recipient, @Nullable String label);
  }
}
