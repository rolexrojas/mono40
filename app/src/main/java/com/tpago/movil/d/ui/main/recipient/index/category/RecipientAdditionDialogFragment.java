package com.tpago.movil.d.ui.main.recipient.index.category;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.EditText;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.RecipientType;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.dep.text.Texts;
import com.tpago.movil.dep.Objects;
import com.tpago.movil.dep.Preconditions;

import butterknife.ButterKnife;

/**
 * @author hecvasro
 */
public final class RecipientAdditionDialogFragment extends DialogFragment {
  private static final String KEY_RECIPIENT = "recipient";

  private Recipient recipient;

  private OnSaveButtonClickedListener listener;

  public static RecipientAdditionDialogFragment create(Recipient recipient) {
    final Bundle args = new Bundle();
    args.putParcelable(KEY_RECIPIENT, Preconditions.assertNotNull(recipient, "recipient == null"));
    final RecipientAdditionDialogFragment fragment = new RecipientAdditionDialogFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    final Fragment f = getParentFragment();
    if (Objects.checkIfNotNull(f) && f instanceof OnSaveButtonClickedListener) {
      listener = (OnSaveButtonClickedListener) f;
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final Bundle bundle = Preconditions.assertNotNull(getArguments(), "getArguments() == null");
    recipient = bundle.getParcelable(KEY_RECIPIENT);
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    return Dialogs.builder(getContext())
      .setCancelable(false)
      .setTitle(R.string.recipient_addition_title)
      .setPositiveButton(R.string.action_save, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          if (Objects.checkIfNotNull(listener)) {
            final EditText editText = ButterKnife.findById(getDialog(), R.id.edit_text);
            final String label = editText.getText().toString().trim();
            listener.onSaveButtonClicked(
              recipient,
              Texts.checkIfEmpty(label) ? null : label);
          }
        }
      })
      .setNegativeButton(R.string.skip, null)
      .setView(R.layout.d_dialog_recipient_addition)
      .create();
  }

  @Override
  public void onStart() {
    super.onStart();
    final Dialog dialog = getDialog();
    final TextView textView = ButterKnife.findById(dialog, R.id.text_view);
    final TextInputLayout textInputLayout = ButterKnife.findById(dialog, R.id.text_input_layout);
    if (recipient.getType().equals(RecipientType.BILL)) {
      textView.setText(R.string.recipient_addition_message_bill);
      textInputLayout.setHint(getString(R.string.recipient_addition_hint_bill));
    } else {
      textView.setText(R.string.recipient_addition_message_phone_number);
      textInputLayout.setHint(getString(R.string.recipient_addition_hint_phone_number));
    }
    final EditText editText = ButterKnife.findById(dialog, R.id.edit_text);
    editText.setText(recipient.getLabel());
  }
}
