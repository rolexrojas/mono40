package com.tpago.movil.d.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.tpago.movil.util.ObjectHelper;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class AppDialog {

  private final AlertDialog alertDialog;

  private AppDialog(
    @NonNull final Context context, @NonNull final String title,
    @Nullable final String message, @Nullable final String positiveActionText,
    @Nullable final OnActionClickedListener positiveActionListener,
    @Nullable final String negativeActionText,
    @Nullable final OnActionClickedListener negativeActionListener
  ) {
    final AlertDialog.Builder builder = new AlertDialog.Builder(context)
      .setCancelable(false)
      .setTitle(title);
    if (!TextUtils.isEmpty(message)) {
      builder.setMessage(message);
    }
    if (!TextUtils.isEmpty(positiveActionText)) {
      builder.setPositiveButton(positiveActionText, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          if (ObjectHelper.isNotNull(positiveActionListener)) {
            positiveActionListener.onActionClicked(Action.POSITIVE);
          }
        }
      });
    }
    if (!TextUtils.isEmpty(negativeActionText)) {
      builder.setNegativeButton(negativeActionText, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          if (ObjectHelper.isNotNull(negativeActionListener)) {
            negativeActionListener.onActionClicked(Action.NEGATIVE);
          }
        }
      });
    }
    alertDialog = builder.create();
  }

  public final void show() {
    alertDialog.show();
  }

  public enum Action {
    POSITIVE,
    NEGATIVE
  }

  public interface OnActionClickedListener {

    void onActionClicked(@NonNull Action action);
  }

  public static final class Builder {

    private final Context context;
    private final String title;
    private String message;
    private String positiveActionText;
    private OnActionClickedListener positiveActionListener;
    private String negativeActionText;
    private OnActionClickedListener negativeActionListener;

    private Builder(@NonNull Context context, @NonNull String title) {
      this.context = context;
      this.title = title;
    }

    @NonNull
    public final Builder message(@NonNull String message) {
      this.message = message;
      return this;
    }

    @NonNull
    public final Builder positiveAction(
      @NonNull String text,
      @Nullable OnActionClickedListener listener
    ) {
      positiveActionText = text;
      positiveActionListener = listener;
      return this;
    }

    @NonNull
    public final Builder positiveAction(@NonNull String text) {
      return positiveAction(text, null);
    }

    @NonNull
    public final Builder negativeAction(
      @NonNull String text,
      @Nullable OnActionClickedListener listener
    ) {
      negativeActionText = text;
      negativeActionListener = listener;
      return this;
    }

    @NonNull
    public final Builder negativeAction(@NonNull String text) {
      return negativeAction(text, null);
    }

    @NonNull
    public final AppDialog build() {
      return new AppDialog(
        context,
        title,
        message,
        positiveActionText,
        positiveActionListener,
        negativeActionText,
        negativeActionListener
      );
    }
  }

  @Deprecated
  public static final class Creator {

    private final Context context;

    public Creator(@NonNull Context context) {
      this.context = context;
    }

    @NonNull
    @Deprecated
    public final Builder create(@NonNull String title) {
      return new Builder(context, title);
    }
  }
}
