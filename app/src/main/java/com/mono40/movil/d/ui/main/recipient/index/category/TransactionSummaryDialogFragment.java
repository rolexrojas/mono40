package com.mono40.movil.d.ui.main.recipient.index.category;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mono40.movil.R;
import com.mono40.movil.d.domain.MerchantRecipient;
import com.mono40.movil.d.domain.Recipient;
import com.mono40.movil.d.domain.RecipientType;
import com.mono40.movil.d.ui.Dialogs;
import com.mono40.movil.util.ObjectHelper;

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
            String transactionId
    ) {
        final Bundle args = new Bundle();
        args.putParcelable(KEY_RECIPIENT, recipient);
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
        if (ObjectHelper.isNotNull(f) && f instanceof OnSaveButtonClickedListener) {
            listener = (OnSaveButtonClickedListener) f;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = ObjectHelper.checkNotNull(this.getArguments(), "this.getArguments()");
        recipient = bundle.getParcelable(KEY_RECIPIENT);
        alreadyExists = bundle.getBoolean(KEY_ALREADY_EXISTS);
        transactionId = bundle.getString(KEY_TRANSACTION_ID);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = Dialogs.builder(getContext())
                .setCancelable(false)
                .setTitle(R.string.transactionSucceeded)
                .setView(R.layout.d_dialog_transaction_summary);
        if (recipient == null || alreadyExists || recipient instanceof MerchantRecipient) {
            builder.setPositiveButton(R.string.ok, null);
        } else {
            builder.setPositiveButton(R.string.action_save, (dialog, which) -> {
                if (ObjectHelper.isNotNull(listener)) {
                    Log.d("com.tpago.mobile", "TransactionSummaryDialogFragment = " + recipient.getId());
                    final EditText editText = getDialog().findViewById(R.id.edit_text);
                    listener.onSaveButtonClicked(
                            recipient,
                            editText.getText()
                                    .toString()
                                    .trim()
                    );
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
        final TextView transactionTextView = dialog.findViewById(R.id.text_view_transaction);
        transactionTextView.setText(String.format("#%1$s", transactionId));
        if (recipient == null || alreadyExists) {
            ((View) dialog.findViewById(R.id.inclusion))
                    .setVisibility(View.GONE);
        } else {
            final String textValue;
            final String hintValue;
            if (recipient.getType()
                    .equals(RecipientType.BILL)) {
                textValue = getString(R.string.recipient_addition_message_bill);
                hintValue = getString(R.string.recipient_addition_hint_bill);
            } else {
                textValue = getString(R.string.recipient_addition_message_phone_number);
                hintValue = getString(R.string.recipient_addition_hint_phone_number);
            }
            if (recipient.getType() == RecipientType.MERCHANT) {
                ((TextView) dialog.findViewById(R.id.text_view)).setVisibility(View.GONE);
                ((TextInputLayout) dialog.findViewById(R.id.text_input_layout))
                        .setVisibility(View.GONE);
            } else {
                ((TextView) dialog.findViewById(R.id.text_view))
                        .setText(textValue);
                ((TextInputLayout) dialog.findViewById(R.id.text_input_layout))
                        .setHint(hintValue.toUpperCase());
            }
            final EditText editText = dialog.findViewById(R.id.edit_text);
            editText.setText(recipient.getLabel());
        }
    }

    public void setListener(OnSaveButtonClickedListener listener) {
        this.listener = listener;
    }
}
