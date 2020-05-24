package com.mono40.movil.d.ui.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mono40.movil.R;
import com.mono40.movil.d.ui.misc.UiUtils;
import com.mono40.movil.reactivex.DisposableUtil;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

/**
 * @author hecvasro
 */
public class SearchView extends LinearLayout {

  private String hint;
  private Disposable queryChangedDisposable = Disposables.disposed();

  @BindColor(R.color.d_search_view_drawable)
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
    this.setOrientation(LinearLayout.HORIZONTAL);
    final TypedArray array = context
      .obtainStyledAttributes(
        attrs,
        R.styleable.DepSearchView,
        defStyleAttr,
        R.style.Dep_App_Widget_SearchView
      );
    try {
      this.hint = array.getString(R.styleable.DepSearchView_hint);
    } finally {
      array.recycle();
    }
    LayoutInflater.from(context)
      .inflate(R.layout.d_search_view, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
    UiUtils.setColorFilter(this.searchImageView, this.drawableColor, PorterDuff.Mode.SRC_IN);
    UiUtils.setColorFilter(this.clearImageView, this.drawableColor, PorterDuff.Mode.SRC_IN);
    this.setHint(this.hint);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();

    this.containerFrameLayout.setOnClickListener((view) -> {
      if (this.searchImageView.getVisibility() == View.VISIBLE) {
        this.queryEditText.requestFocus();
      } else {
        this.queryEditText.setText(null);
      }
    });

    this.queryChangedDisposable = this.onQueryChanged()
      .subscribe((query) -> {
        final boolean canClear = !TextUtils.isEmpty(query);
        this.searchImageView.setVisibility(!canClear ? View.VISIBLE : View.GONE);
        this.clearImageView.setVisibility(canClear ? View.VISIBLE : View.GONE);
      });
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();

    this.containerFrameLayout.setOnClickListener(null);

    DisposableUtil.dispose(this.queryChangedDisposable);
  }

  /**
   * TODO
   *
   * @param hint
   *   TODO
   */
  public void setHint(@Nullable String hint) {
    this.hint = hint;
    this.queryEditText.setHint(hint);
  }

  /**
   * TODO
   */
  public void clear() {
    this.queryEditText.setText(null);
  }

  /**
   * Creates an {@link Observable} that emits all query change events.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code queryEditText}.
   * Unsubscribe to free this reference.
   * <p>
   * <em>Note:</em> A getValueContent will be emitted immediately on subscribe.
   * <p>
   * <em>Note:</em> By default {@link #onQueryChanged()} operates on {@link
   * AndroidSchedulers#mainThread()}.
   *
   * @return An {@link Observable} that emits all query change events.
   */
  @NonNull
  public Observable<String> onQueryChanged() {
    return Observable.create((emitter) -> {
      final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
          if (!emitter.isDisposed()) {
            emitter.onNext(s.toString());
          }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
      };
      this.queryEditText.addTextChangedListener(textWatcher);
      emitter.setCancellable(() -> this.queryEditText.removeTextChangedListener(textWatcher));
      emitter.onNext(
        this.queryEditText.getText()
          .toString()
      );
    });
  }
}
