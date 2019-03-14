package com.tpago.movil.app.ui.main.settings.timeout;

import android.view.View;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.d.ui.view.RecyclerViewBaseAdapter;
import com.tpago.movil.dep.ConfigManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimeoutSessionOptionsAdapter extends RecyclerViewBaseAdapter<TimeoutSessionOption, TimeoutSessionOptionsAdapter.TimeoutSessionOptionViewHolder> {

    public TimeoutSessionOptionsAdapter(List<TimeoutSessionOption> items) {
        super(items);
    }

    public TimeoutSessionOptionsAdapter(List<TimeoutSessionOption> items, RecyclerAdapterCallback<TimeoutSessionOption> listener) {
        super(items, listener);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.payment_method_list_item;
    }

    @Override
    protected TimeoutSessionOptionViewHolder getViewHolder(View view) {
        return new TimeoutSessionOptionViewHolder(view, item -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    static class TimeoutSessionOptionViewHolder extends RecyclerViewBaseAdapter.BaseViewHolder<TimeoutSessionOption> {
        @BindView(R.id.primaryTextView)
        TextView primaryText;

        public TimeoutSessionOptionViewHolder(View itemView, RecyclerAdapterCallback<TimeoutSessionOption> listener) {
            super(itemView, listener);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(TimeoutSessionOption item) {
            super.bind(item);
            primaryText.setText(item.getTitle());
        }
    }
}
