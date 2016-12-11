package com.gbh.movil.ui.text;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;

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
      // Creates a copy in order for it to be safe to cache or delay reading.
      .map(new Func1<CharSequence, String>() {
        @Override
        public String call(CharSequence charSequence) {
          return charSequence.toString();
        }
      });
  }

  /**
   * TODO
   *
   * @param textView
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public static Observable<String> afterTextChanges(@NonNull TextView textView) {
    return RxTextView.afterTextChangeEvents(textView)
      .map(new Func1<TextViewAfterTextChangeEvent, String>() {
        @Override
        public String call(TextViewAfterTextChangeEvent event) {
          return event.editable().toString();
        }
      });
  }
}
