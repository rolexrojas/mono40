package com.tpago.movil.app.ui;

import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

@AutoValue
public abstract class AlertShowEvent {

  private static DialogInterface.OnClickListener createButtonListener(ButtonAction action) {
    return ObjectHelper.isNotNull(action) ? (d, i) -> action.accept() : null;
  }

  public static Builder builder(StringMapper stringMapper) {
    return new Builder(stringMapper);
  }

  abstract String title();

  abstract String message();

  abstract String positiveButtonText();

  @Nullable
  abstract ButtonAction positiveButtonAction();

  @Nullable
  abstract String negativeButtonText();

  @Nullable
  abstract ButtonAction negativeButtonAction();

  @Nullable
  @Memoized
  DialogInterface.OnClickListener positiveButtonListener() {
    return createButtonListener(this.positiveButtonAction());
  }

  @Nullable
  @Memoized
  DialogInterface.OnClickListener negativeButtonListener() {
    return createButtonListener(this.negativeButtonAction());
  }

  @Memoized
  @Override
  public abstract int hashCode();

  @Memoized
  @Override
  public abstract String toString();

  interface ButtonAction {

    void accept();
  }

  public static final class Builder {

    private final StringMapper stringMapper;

    private String title;
    private String message;
    private String positiveButtonText;
    private ButtonAction positiveButtonAction;
    private String negativeButtonText;
    private ButtonAction negativeButtonAction;

    private Builder(StringMapper stringMapper) {
      this.stringMapper = stringMapper;
    }

    public final Builder title(String title) {
      this.title = ObjectHelper.checkNotNull(StringHelper.emptyIfNull(title), "title");
      return this;
    }

    public final Builder title(@StringRes int id) {
      return this.title(this.stringMapper.apply(id));
    }

    public final Builder message(String message) {
      this.message = ObjectHelper.checkNotNull(StringHelper.emptyIfNull(message), "message");
      return this;
    }

    public final Builder message(@StringRes int id) {
      return this.message(this.stringMapper.apply(id));
    }

    public final Builder positiveButton(String text) {
      this.positiveButtonText = ObjectHelper.checkNotNull(StringHelper.emptyIfNull(text), "text");
      return this;
    }

    public final Builder positiveButton(@StringRes int id) {
      return this.positiveButton(this.stringMapper.apply(id));
    }

    public final Builder positiveButton(String text, ButtonAction action) {
      this.positiveButtonText = ObjectHelper.checkNotNull(StringHelper.emptyIfNull(text), "text");
      this.positiveButtonAction = ObjectHelper.checkNotNull(action, "action");
      return this;
    }

    public final Builder positiveButton(@StringRes int id, ButtonAction action) {
      return this.positiveButton(this.stringMapper.apply(id), action);
    }

    public final Builder negativeButton(String text) {
      this.negativeButtonText = ObjectHelper.checkNotNull(StringHelper.emptyIfNull(text), "text");
      return this;
    }

    public final Builder negativeButton(@StringRes int id) {
      return this.negativeButton(this.stringMapper.apply(id));
    }

    public final Builder negativeButton(String text, ButtonAction action) {
      this.negativeButtonText = ObjectHelper.checkNotNull(StringHelper.emptyIfNull(text), "text");
      this.negativeButtonAction = ObjectHelper.checkNotNull(action, "action");
      return this;
    }

    public final Builder negativeButton(@StringRes int id, ButtonAction action) {
      return this.negativeButton(this.stringMapper.apply(id), action);
    }

    public final AlertShowEvent build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("title", StringHelper.isNullOrEmpty(this.title))
        .addPropertyNameIfMissing("message", StringHelper.isNullOrEmpty(this.message))
        .addPropertyNameIfMissing(
          "positiveButtonText",
          StringHelper.isNullOrEmpty(this.positiveButtonText)
        )
        .checkNoMissingProperties();

      return new AutoValue_AlertShowEvent(
        this.title,
        this.message,
        this.positiveButtonText,
        this.positiveButtonAction,
        this.negativeButtonText,
        this.negativeButtonAction
      );
    }
  }
}
