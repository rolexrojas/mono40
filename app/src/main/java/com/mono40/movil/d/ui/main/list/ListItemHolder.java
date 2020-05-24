package com.mono40.movil.d.ui.main.list;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.mono40.movil.d.data.util.Holder;
import com.mono40.movil.util.ObjectHelper;

import butterknife.ButterKnife;

/**
 * @author hecvasro
 */
public abstract class ListItemHolder extends RecyclerView.ViewHolder implements Holder {

  protected final View rootView;

  protected ListItemHolder(
    @NonNull final View rootView,
    @Nullable final OnClickListener onClickListener
  ) {
    super(rootView);
    this.rootView = rootView;
    // Binds all the annotated views and methods.
    ButterKnife.bind(this, this.rootView);
    if (ObjectHelper.isNotNull(onClickListener)) {
      // Adds a listener that gets notified every time the root view gets clicked.
      this.rootView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          onClickListener.onClick(getAdapterPosition());
        }
      });
    }
  }

  public ListItemHolder(@NonNull View rootView) {
    this(rootView, null);
  }

  @NonNull
  public final Context getContext() {
    return getRootView().getContext();
  }

  @NonNull
  public final View getRootView() {
    return rootView;
  }

  public interface OnClickListener {

    void onClick(int position);
  }
}
