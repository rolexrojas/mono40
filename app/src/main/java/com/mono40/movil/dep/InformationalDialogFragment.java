package com.mono40.movil.dep;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.widget.TextView;

import com.mono40.movil.R;
import com.mono40.movil.dep.text.Texts;
import com.mono40.movil.util.ObjectHelper;

import butterknife.ButterKnife;

/**
 * @author hecvasro
 */
@Deprecated
public final class InformationalDialogFragment extends DialogFragment {

    private static final String KEY_TITLE = "titleText";
    private static final String KEY_MESSAGE = "messageText";
    private static final String KEY_TEXT_BUTTON_POSITIVE = "positiveButtonText";
    private static final String KEY_TEXT_BUTTON_NEGATIVE = "negativeButtonText";

    private ResultHandler resultHandler;

    private String title;
    private String message;
    private String positiveButtonText;
    private String negativeButtonText;

    public static InformationalDialogFragment create(
            String title,
            String message,
            String positiveButtonText,
            String negativeButtonText
    ) {
        final Bundle bundle = new Bundle();
        if (Texts.checkIfEmpty(title)) {
            throw new IllegalArgumentException("Texts.checkIfEmpty(title) == true");
        }
        bundle.putString(KEY_TITLE, title);
        if (Texts.checkIfEmpty(message)) {
            throw new IllegalArgumentException("Texts.checkIfEmpty(message) == true");
        }
        bundle.putString(KEY_MESSAGE, message);
        if (Texts.checkIfEmpty(positiveButtonText)) {
            throw new IllegalArgumentException("Texts.checkIfEmpty(positiveButtonText) == true");
        }
        bundle.putString(KEY_TEXT_BUTTON_POSITIVE, positiveButtonText);
        if (Texts.checkIfNotEmpty(negativeButtonText)) {
            bundle.putString(KEY_TEXT_BUTTON_NEGATIVE, negativeButtonText);
        }
        final InformationalDialogFragment fragment = new InformationalDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static InformationalDialogFragment create(
            String title,
            String message,
            String positiveButtonText
    ) {
        return create(title, message, positiveButtonText, null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Attaches the result handler.
        final Fragment targetFragment = getTargetFragment();
        if (ObjectHelper.isNotNull(targetFragment) && targetFragment instanceof ResultHandler) {
            resultHandler = (ResultHandler) targetFragment;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = ObjectHelper.checkNotNull(this.getArguments(), "this.getArguments()");
        if (!args.containsKey(KEY_TITLE)) {
            throw new IllegalArgumentException("args.containsKey(KEY_TITLE) == false");
        }
        title = args.getString(KEY_TITLE, null);
        if (!args.containsKey(KEY_MESSAGE)) {
            throw new IllegalArgumentException("args.containsKey(KEY_MESSAGE) == false");
        }
        message = args.getString(KEY_MESSAGE, null);
        if (!args.containsKey(KEY_MESSAGE)) {
            throw new IllegalArgumentException("args.containsKey(KEY_POSITIVE_BUTTON_TEXT) == false");
        }
        positiveButtonText = args.getString(KEY_TEXT_BUTTON_POSITIVE, null);
        if (args.containsKey(KEY_TEXT_BUTTON_NEGATIVE)) {
            negativeButtonText = args.getString(KEY_TEXT_BUTTON_NEGATIVE, null);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context context = getActivity();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(title)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ObjectHelper.isNotNull(resultHandler)) {
                            resultHandler.onPositiveResult();
                        }
                    }
                })
                .setView(R.layout.d_dialog_informational);
        if (Texts.checkIfNotEmpty(negativeButtonText)) {
            builder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (ObjectHelper.isNotNull(resultHandler)) {
                        resultHandler.onNegativeResult();
                    }
                }
            });
        }
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TextView) getDialog().findViewById(R.id.label_message))
                .setText(message);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Detaches the result handler.
        resultHandler = null;
    }

    public interface ResultHandler {

        void onPositiveResult();

        void onNegativeResult();
    }
}
