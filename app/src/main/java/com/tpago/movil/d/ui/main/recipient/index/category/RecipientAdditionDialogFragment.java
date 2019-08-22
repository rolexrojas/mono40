package com.tpago.movil.d.ui.main.recipient.index.category;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.widget.EditText;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.RecipientType;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

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
        args.putParcelable(KEY_RECIPIENT, ObjectHelper.checkNotNull(recipient, "recipient"));
        final RecipientAdditionDialogFragment fragment = new RecipientAdditionDialogFragment();
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
        this.recipient = ObjectHelper.checkNotNull(this.getArguments(), "this.getArguments()")
                .getParcelable(KEY_RECIPIENT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return Dialogs.builder(getContext())
                .setCancelable(false)
                .setTitle(R.string.recipient_addition_title)
                .setPositiveButton(R.string.action_save, (dialog, which) -> {
                    if (ObjectHelper.isNotNull(listener)) {
                        final EditText editText = getDialog().findViewById(R.id.edit_text);
                        final String label = editText.getText()
                                .toString()
                                .trim();
                        listener.onSaveButtonClicked(
                                recipient,
                                StringHelper.nullIfEmpty(label)
                        );
                    }
                })
                .setNegativeButton(R.string.skip, (dialogInterface, i) -> {
                    listener.onSaveButtonClicked(recipient, null
                    );
                })
                .setView(R.layout.d_dialog_recipient_addition)
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();
        final Dialog dialog = getDialog();
        final TextView textView = dialog.findViewById(R.id.text_view);
        final TextInputLayout textInputLayout = dialog.findViewById(R.id.text_input_layout);
        if (recipient.getType()
                .equals(RecipientType.BILL)) {
            textView.setText(R.string.recipient_addition_message_bill);
            textInputLayout.setHint(getString(R.string.recipient_addition_hint_bill));
        } else {
            textView.setText(R.string.recipient_addition_message_phone_number);
            textInputLayout.setHint(getString(R.string.recipient_addition_hint_phone_number));
        }
        final EditText editText = dialog.findViewById(R.id.edit_text);
        editText.setText(recipient.getLabel());
    }
}
