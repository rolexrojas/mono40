package com.tpago.movil.app.ui.alert;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.util.function.Action;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class Alert {

  private static DialogInterface.OnClickListener createButtonListener(Action action) {
    return ObjectHelper.isNotNull(action) ? (d, i) -> action.run() : null;
  }

  static Builder builder(Context context, StringMapper stringMapper) {
    return new Builder(context, stringMapper);
  }

  abstract Context context();

  abstract String title();

  abstract String message();

  abstract String positiveButtonText();

  @Nullable
  abstract Action positiveButtonAction();

  @Nullable
  abstract String negativeButtonText();

  @Nullable
  abstract Action negativeButtonAction();

  public final void show() {
    final AlertDialog alertDialog = new AlertDialog.Builder(this.context())
      .setCancelable(false)
      .setTitle(this.title())
      .setMessage(this.message())
      .setPositiveButton(
        this.positiveButtonText(),
        createButtonListener(this.positiveButtonAction())
      )
      .setNegativeButton(
        this.negativeButtonText(),
        createButtonListener(this.negativeButtonAction())
      )
      .create();
    alertDialog.show();
  }

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();

  public static final class Builder {

    private final Context context;
    private final StringMapper stringMapper;

    private String title;
    private String message;
    private String positiveButtonText;
    private Action positiveButtonAction;
    private String negativeButtonText;
    private Action negativeButtonAction;

    private Builder(Context context, StringMapper stringMapper) {
      this.context = ObjectHelper.checkNotNull(context, "context");
      this.stringMapper = ObjectHelper.checkNotNull(stringMapper, "stringMapper");
    }

    public final Builder title(String title) {
      this.title = StringHelper.checkIsNotNullNorEmpty(title, "title");
      return this;
    }

    public final Builder title(@StringRes int id) {
      return this.title(this.stringMapper.apply(id));
    }

    public final Builder message(String message) {
      this.message = StringHelper.checkIsNotNullNorEmpty(message, "message");
      return this;
    }

    public final Builder message(@StringRes int id) {
      return this.message(this.stringMapper.apply(id));
    }

    public final Builder positiveButtonText(String positiveButtonText) {
      this.positiveButtonText = StringHelper
        .checkIsNotNullNorEmpty(positiveButtonText, "positiveButtonText");
      return this;
    }

    public final Builder positiveButtonText(@StringRes int id) {
      return this.positiveButtonText(this.stringMapper.apply(id));
    }

    public final Builder positiveButtonAction(@Nullable Action action) {
      this.positiveButtonAction = action;
      return this;
    }

    public final Builder negativeButtonText(@Nullable String text) {
      this.negativeButtonText = text;
      return this;
    }

    public final Builder negativeButtonText(@StringRes int id) {
      return this.negativeButtonText(this.stringMapper.apply(id));
    }

    public final Builder negativeButtonAction(@Nullable Action action) {
      this.negativeButtonAction = action;
      return this;
    }

    public final Alert build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("title", StringHelper.isNullOrEmpty(this.title))
        .addPropertyNameIfMissing("message", StringHelper.isNullOrEmpty(this.message))
        .addPropertyNameIfMissing(
          "positiveButtonText",
          StringHelper.isNullOrEmpty(this.positiveButtonText)
        )
        .checkNoMissingProperties();

      if (StringHelper.isNullOrEmpty(this.negativeButtonText)) {
        this.negativeButtonAction = null;
      }

      return new AutoValue_Alert(
        this.context,
        this.title,
        this.message,
        this.positiveButtonText,
        this.positiveButtonAction,
        this.negativeButtonText,
        this.negativeButtonAction
      );
    }

    public final void show() {
      this.build()
        .show();
    }
  }
}
