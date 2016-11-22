package com.gbh.movil.ui.main.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gbh.movil.Utils;

import butterknife.ButterKnife;

/**
 * TODO
 *
 * @author hecvasro
 */
public abstract class Holder extends RecyclerView.ViewHolder {
  /**
   * TODO
   */
  protected final View rootView;

  /**
   * TODO
   *
   * @param rootView
   *   TODO
   * @param onClickListener
   *   TODO
   */
  protected Holder(@NonNull final View rootView,
    @Nullable final OnClickListener onClickListener) {
    super(rootView);
    this.rootView = rootView;
    // Binds all the annotated views and methods.
    ButterKnife.bind(this, this.rootView);
    if (Utils.isNotNull(onClickListener)) {
      // Adds a listener that gets notified every time the root view gets clicked.
      this.rootView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          onClickListener.onClick(getAdapterPosition());
        }
      });
    }
  }

  /**
   * TODO
   *
   * @param rootView
   *   TODO
   */
  public Holder(@NonNull View rootView) {
    this(rootView, null);
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final View getRootView() {
    return rootView;
  }

  /**
   * TODO
   */
  public interface OnClickListener {
    /**
     * TODO
     *
     * @param position
     *   TODO
     */
    void onClick(int position);
  }
}
