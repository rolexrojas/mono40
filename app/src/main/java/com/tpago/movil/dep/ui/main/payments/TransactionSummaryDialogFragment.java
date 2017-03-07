package com.tpago.movil.dep.ui.main.payments;

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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.domain.RecipientType;
import com.tpago.movil.dep.ui.Dialogs;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import butterknife.ButterKnife;

/**
 * @author hecvasro
 */
public final class TransactionSummaryDialogFragment extends DialogFragment {
  private static final String KEY_RECIPIENT = "recipient";
  private static final String KEY_ALREADY_EXISTS = "alreadyExists";
  private static final String KEY_TRANSACTION_ID = "transactionId";

  private Recipient recipient;
  private boolean alreadyExists;
  private String transactionId;

  private OnSaveButtonClickedListener listener;

  public static TransactionSummaryDialogFragment create(
    Recipient recipient,
    boolean alreadyExists,
    String transactionId) {
    final Bundle args = new Bundle();
    args.putSerializable(KEY_RECIPIENT, Preconditions.checkNotNull(recipient, "recipient == null"));
    args.putSerializable(KEY_ALREADY_EXISTS, alreadyExists);
    args.putSerializable(KEY_TRANSACTION_ID, transactionId);
    final TransactionSummaryDialogFragment fragment = new TransactionSummaryDialogFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    final Fragment f = getParentFragment();
    if (Objects.isNotNull(f) && f instanceof OnSaveButtonClickedListener) {
      listener = (OnSaveButtonClickedListener) f;
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final Bundle bundle = Preconditions.checkNotNull(getArguments(), "getArguments() == null");
    recipient = (Recipient) bundle.getSerializable(KEY_RECIPIENT);
    alreadyExists = bundle.getBoolean(KEY_ALREADY_EXISTS);
    transactionId = bundle.getString(KEY_TRANSACTION_ID);
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final AlertDialog.Builder builder = Dialogs.builder(getContext())
      .setCancelable(false)
      .setTitle(R.string.transaction_confirmation_title)
      .setView(R.layout.dialog_transaction_summary);
    if (alreadyExists) {
      builder.setPositiveButton(R.string.ok, null);
    } else {
      builder.setPositiveButton(R.string.action_save, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          if (Objects.isNotNull(listener)) {
            final EditText editText = ButterKnife.findById(getDialog(), R.id.edit_text);
            listener.onSaveButtonClicked(recipient, editText.getText().toString().trim());
          }
        }
      })
        .setNegativeButton(R.string.skip, null);
    }
    return builder.create();
  }

  @Override
  public void onStart() {
    super.onStart();
    final Dialog dialog = getDialog();
    final TextView transactionTextView = ButterKnife.findById(dialog, R.id.text_view_transaction);
    transactionTextView.setText(String.format("#%1$s", transactionId));
    if (alreadyExists) {
      ButterKnife.findById(dialog, R.id.inclusion).setVisibility(View.GONE);
    } else {
      final String textValue;
      final String hintValue;
      if (recipient.getType().equals(RecipientType.BILL)) {
        textValue = getString(R.string.recipient_addition_message_bill);
        hintValue = getString(R.string.recipient_addition_hint_bill);
      } else {
        textValue = getString(R.string.recipient_addition_message_phone_number);
        hintValue = getString(R.string.recipient_addition_hint_phone_number);
      }
      ButterKnife.<TextView>findById(dialog, R.id.text_view)
        .setText(textValue);
      ButterKnife.<TextInputLayout>findById(dialog, R.id.text_input_layout)
        .setHint(hintValue.toUpperCase());
      final EditText editText = ButterKnife.findById(dialog, R.id.edit_text);
      editText.setText(recipient.getLabel());
    }
  }
}
