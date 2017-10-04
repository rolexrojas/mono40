package com.tpago.movil.app.ui;

import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.R;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

@AutoValue
public abstract class AlertData {

  private static DialogInterface.OnClickListener createButtonListener(ButtonAction action) {
    return ObjectHelper.isNotNull(action) ? (d, i) -> action.accept() : null;
  }

  public static Builder builder(StringMapper stringMapper) {
    return new Builder(stringMapper);
  }

  public static AlertData createForGenericFailure(StringMapper stringMapper) {
    return builder(stringMapper)
      .build();
  }

  public abstract String title();

  public abstract String message();

  public abstract String positiveButtonText();

  @Nullable
  public abstract ButtonAction positiveButtonAction();

  @Nullable
  public abstract String negativeButtonText();

  @Nullable
  public abstract ButtonAction negativeButtonAction();

  @Nullable
  @Memoized
  public DialogInterface.OnClickListener positiveButtonListener() {
    return createButtonListener(this.positiveButtonAction());
  }

  @Nullable
  @Memoized
  public DialogInterface.OnClickListener negativeButtonListener() {
    return createButtonListener(this.negativeButtonAction());
  }

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();

  public interface ButtonAction {

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

    public final Builder title(@Nullable String title) {
      this.title = title;
      return this;
    }

    public final Builder title(@StringRes int id) {
      return this.title(this.stringMapper.apply(id));
    }

    public final Builder message(@Nullable String message) {
      this.message = message;
      return this;
    }

    public final Builder message(@StringRes int id) {
      return this.message(this.stringMapper.apply(id));
    }

    public final Builder positiveButtonText(@Nullable String text) {
      this.positiveButtonText = text;
      return this;
    }

    public final Builder positiveButtonText(@StringRes int id) {
      return this.positiveButtonText(this.stringMapper.apply(id));
    }

    public final Builder positiveButtonAction(@Nullable ButtonAction action) {
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

    public final Builder negativeButtonAction(@Nullable ButtonAction action) {
      this.negativeButtonAction = action;
      return this;
    }

    public final AlertData build() {
      if (StringHelper.isNullOrEmpty(this.title)) {
        this.title = this.stringMapper.apply(R.string.weAreSorry);
      }
      if (StringHelper.isNullOrEmpty(this.message)) {
        this.message = this.stringMapper.apply(R.string.anUnexpectedErrorOccurred);
      }
      if (StringHelper.isNullOrEmpty(this.positiveButtonText)) {
        this.positiveButtonText = this.stringMapper.apply(R.string.ok);
      }

      return new AutoValue_AlertData(
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
