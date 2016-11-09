package com.gbh.movil.ui.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gbh.movil.R;
import com.gbh.movil.RxUtils;
import com.gbh.movil.ui.UiUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
public class SearchView extends LinearLayout {
  private String hint;

  private Subscription queryChangedSubscription = Subscriptions.unsubscribed();
  private Subscription containerFrameLayoutClickedSubscription = Subscriptions.unsubscribed();

  @BindColor(R.color.search_view_drawable)
  int drawableColor;

  @BindView(R.id.edit_text_query)
  EditText queryEditText;
  @BindView(R.id.frame_layout_container)
  FrameLayout containerFrameLayout;
  @BindView(R.id.image_view_search)
  ImageView searchImageView;
  @BindView(R.id.image_view_clear)
  ImageView clearImageView;

  public SearchView(Context context) {
    this(context, null);
  }

  public SearchView(Context context, AttributeSet attrs) {
    this(context, attrs, R.attr.searchViewStyle);
  }

  public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setOrientation(LinearLayout.HORIZONTAL);
    final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SearchView,
      defStyleAttr, R.style.Widget_SearchView);
    try {
      hint = array.getString(R.styleable.SearchView_hint);
    } finally {
      array.recycle();
    }
    LayoutInflater.from(context).inflate(R.layout.search_view, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
    UiUtils.setColorFilter(searchImageView, drawableColor, PorterDuff.Mode.SRC_IN);
    UiUtils.setColorFilter(clearImageView, drawableColor, PorterDuff.Mode.SRC_IN);
    setHint(hint);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    queryChangedSubscription = onQueryChanged()
      .subscribe(new Action1<String>() {
        @Override
        public void call(String query) {
          final boolean canClear = !TextUtils.isEmpty(query);
          containerFrameLayout.setEnabled(canClear);
          searchImageView.setVisibility(!canClear ? View.VISIBLE : View.GONE);
          clearImageView.setVisibility(canClear ? View.VISIBLE : View.GONE);
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable);
        }
      });
    containerFrameLayoutClickedSubscription = RxView.clicks(containerFrameLayout)
      .subscribe(new Action1<Void>() {
        @Override
        public void call(Void notification) {
          queryEditText.setText(null);
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable);
        }
      });
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    RxUtils.unsubscribe(containerFrameLayoutClickedSubscription);
    RxUtils.unsubscribe(queryChangedSubscription);
  }

  public void setHint(@Nullable String hint) {
    this.hint = hint;
    this.queryEditText.setHint(hint);
  }

  /**
   * Creates an {@link Observable} that emits all query change events.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code queryEditText}.
   * Unsubscribe to free this reference.
   * <p>
   * <em>Note:</em> A getValue will be emitted immediately on subscribe.
   * <p>
   * <em>Note:</em> By default {@link #onQueryChanged()} operates on {@link
   * AndroidSchedulers#mainThread()}.
   *
   * @return An {@link Observable} that emits all query change events.
   */
  @NonNull
  public Observable<String> onQueryChanged() {
    return RxTextView.textChanges(queryEditText)
      // Creates a copy in order for it to be safe to cache or delay reading.
      .map(new Func1<CharSequence, String>() {
        @Override
        public String call(CharSequence charSequence) {
          return charSequence.toString();
        }
      });
  }
}
