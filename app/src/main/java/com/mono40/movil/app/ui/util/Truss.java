package com.mono40.movil.app.ui.util;

import android.text.SpannableStringBuilder;

import java.util.ArrayDeque;
import java.util.Deque;

import static android.text.Spanned.SPAN_INCLUSIVE_EXCLUSIVE;

/**
 * A {@link SpannableStringBuilder} wrapper whose API doesn't getLogoUri me want to stab my eyes
 * out.
 *
 * @author Jake Wharton
 * @see <a href="https://gist.github.com/JakeWharton/11274467">Gist</a>
 */
public final class Truss {

  public static Truss create() {
    return new Truss();
  }

  private final SpannableStringBuilder builder;
  private final Deque<Span> stack;

  private Truss() {
    this.builder = new SpannableStringBuilder();
    this.stack = new ArrayDeque<>();
  }

  public final Truss append(String string) {
    this.builder.append(string);
    return this;
  }

  public final Truss append(CharSequence charSequence) {
    this.builder.append(charSequence);
    return this;
  }

  public final Truss append(char c) {
    this.builder.append(c);
    return this;
  }

  public final Truss append(int number) {
    this.builder.append(String.valueOf(number));
    return this;
  }

  /**
   * Starts {@code span} at the current position in the builder.
   */
  public final Truss pushSpan(Object span) {
    this.stack.addLast(new Span(this.builder.length(), span));
    return this;
  }

  /**
   * End the most recently pushed span at the current position in the builder.
   */
  private Truss popSpan() {
    final Span span = this.stack.removeLast();
    this.builder.setSpan(span.span, span.start, this.builder.length(), SPAN_INCLUSIVE_EXCLUSIVE);
    return this;
  }

  public final Truss popSpans() {
    while (!this.stack.isEmpty()) {
      this.popSpan();
    }
    return this;
  }

  /**
   * Create the final {@link CharSequence}, popping any remaining spans.
   */
  public CharSequence build() {
    this.popSpans();
    return this.builder;
  }

  private static final class Span {

    final int start;
    final Object span;

    Span(int start, Object span) {
      this.start = start;
      this.span = span;
    }
  }
}
