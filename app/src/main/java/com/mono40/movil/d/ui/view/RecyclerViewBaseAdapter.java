package com.mono40.movil.d.ui.view;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;

public abstract class RecyclerViewBaseAdapter<T, V extends RecyclerViewBaseAdapter.BaseViewHolder<T>> extends RecyclerView.Adapter<V> {
    private List<T> items;
    private LayoutInflater layoutInflater;
    protected RecyclerAdapterCallback<T> listener;

    public RecyclerViewBaseAdapter(List<T> items) {
        this.items = items;
    }

    public RecyclerViewBaseAdapter(List<T> items, RecyclerAdapterCallback<T> listener) {
        this(items);
        this.listener = listener;
    }

    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        View view = layoutInflater.inflate(getLayoutResource(), parent, false);
        return getViewHolder(view);
    }

    @Override
    public void onBindViewHolder(V holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void setListener(RecyclerAdapterCallback<T> listener) {
        this.listener = listener;
    }

    @LayoutRes
    protected abstract int getLayoutResource();

    protected abstract V getViewHolder(View view);

    public static abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
        RecyclerAdapterCallback<T> listener;

        public BaseViewHolder(View itemView, RecyclerAdapterCallback<T> listener) {
            super(itemView);
            this.listener = listener;
            ButterKnife.bind(this, itemView);
        }

        @SuppressWarnings("unchecked")
        public void bind(T item) {
            itemView.setTag(item);
            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onItemClick((T) view.getTag());
                }
            });
        }
    }

    public interface RecyclerAdapterCallback<T> {
        void onItemClick(T item);
    }
}
