package com.tpago.movil.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Objects;

/**
 * @author hecvasro
 */
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
    String negativeButtonText) {
    final Bundle bundle = new Bundle();
    if (Texts.isEmpty(title)) {
      throw new IllegalArgumentException("title.isEmpty() == true");
    }
    bundle.putString(KEY_TITLE, title);
    if (Texts.isEmpty(message)) {
      throw new IllegalArgumentException("message.isEmpty() == true");
    }
    bundle.putString(KEY_MESSAGE, message);
    if (Texts.isEmpty(positiveButtonText)) {
      throw new IllegalArgumentException("positiveButtonText.isEmpty() == true");
    }
    bundle.putString(KEY_TEXT_BUTTON_POSITIVE, positiveButtonText);
    if (Texts.isNotEmpty(negativeButtonText)) {
      bundle.putString(KEY_TEXT_BUTTON_NEGATIVE, negativeButtonText);
    }
    final InformationalDialogFragment fragment = new InformationalDialogFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  public static InformationalDialogFragment create(
    String title,
    String message,
    String positiveButtonText) {
    return create(title, message, positiveButtonText, null);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    // Attaches the result handler.
    final Fragment targetFragment = getTargetFragment();
    if (Objects.isNotNull(targetFragment) && targetFragment instanceof ResultHandler) {
      resultHandler = (ResultHandler) targetFragment;
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final Bundle bundle = Objects.isNull(savedInstanceState) ? getArguments() : savedInstanceState;
    if (Objects.isNotNull(bundle)) {
      title = bundle.getString(KEY_TITLE, null);
      message = bundle.getString(KEY_MESSAGE, null);
      positiveButtonText = bundle.getString(KEY_TEXT_BUTTON_POSITIVE, null);
      negativeButtonText = bundle.getString(KEY_TEXT_BUTTON_NEGATIVE, null);
    }
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
      .setCancelable(false)
      .setTitle(title)
      .setMessage(message)
      .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          if (Objects.isNotNull(resultHandler)) {
            resultHandler.onPositiveResult();
          }
        }
      });
    if (Texts.isNotEmpty(negativeButtonText)) {
      builder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          if (Objects.isNotNull(resultHandler)) {
            resultHandler.onNegativeResult();
          }
        }
      });
    }
    return builder.create();
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
