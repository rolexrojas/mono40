package com.tpago.movil.d.ui.text;

import androidx.annotation.NonNull;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;

import rx.Observable;
import rx.functions.Func1;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class UiTextHelper {
  private UiTextHelper() {
  }

  /**
   * TODO
   *
   * @param view
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public static Observable<String> textChanges(@NonNull TextView view) {
    return RxTextView.textChanges(view)
      .map(new Func1<CharSequence, String>() {
        @Override
        public String call(CharSequence charSequence) {
          return charSequence.toString();
        }
      });
  }
}
