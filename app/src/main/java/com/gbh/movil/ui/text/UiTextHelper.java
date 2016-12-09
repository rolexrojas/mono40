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
