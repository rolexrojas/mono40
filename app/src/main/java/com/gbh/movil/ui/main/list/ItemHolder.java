package com.gbh.movil.ui.main.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gbh.movil.Utils;

/**
 * TODO
 *
 * @author hecvasro
 */
public abstract class ItemHolder extends RecyclerView.ViewHolder {
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
  public ItemHolder(@NonNull final View rootView,
    @Nullable final OnClickListener onClickListener) {
    super(rootView);
    this.rootView = rootView;
    if (Utils.isNotNull(onClickListener)) {
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
  public ItemHolder(@NonNull View rootView) {
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
