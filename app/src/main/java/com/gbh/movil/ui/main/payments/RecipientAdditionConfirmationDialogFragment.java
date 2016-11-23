package com.gbh.movil.ui.main.payments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.gbh.movil.R;
import com.gbh.movil.Utils;
import com.gbh.movil.domain.Recipient;

/**
 * TODO
 *
 * @author hecvasro
 */
public class RecipientAdditionConfirmationDialogFragment extends DialogFragment {
  private static final String ARGUMENT_RECIPIENT = "recipient";

  private Recipient recipient;

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
    return new AlertDialog.Builder(getActivity())
      .setTitle(R.string.contact_added_title)
      .setMessage(String
        .format(getString(R.string.contact_added_message), recipient.getIdentifier()))
      .setNegativeButton(R.string.action_skip, null)
      .setPositiveButton(R.string.action_save, null)
      .create();
  }
}
