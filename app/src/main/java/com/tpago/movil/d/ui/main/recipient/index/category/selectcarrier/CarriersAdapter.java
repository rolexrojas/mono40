package com.tpago.movil.d.ui.main.recipient.index.category.selectcarrier;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tpago.movil.R;
import com.tpago.movil.company.Company;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.partner.Partner;
import com.tpago.movil.d.ui.view.RecyclerViewBaseAdapter;

import java.util.List;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;

import static com.tpago.movil.d.ui.main.recipient.index.category.selectcarrier.CarriersAdapter.*;

public class CarriersAdapter extends RecyclerViewBaseAdapter<Partner, CarrierViewHolder> {
    CompanyHelper companyHelper;

    public CarriersAdapter(@NonNull List<Partner> items, CompanyHelper companyHelper) {
        super(items);
        this.companyHelper = companyHelper;
    }

    public CarriersAdapter(List<Partner> items, RecyclerAdapterCallback<Partner> listener, CompanyHelper companyHelper) {
        super(items, listener);
        this.companyHelper = companyHelper;
    }

    @Override
    protected int getLayoutResource() {
        return com.tpago.movil.R.layout.d_list_item_bank;
    }

    @Override
    protected CarrierViewHolder getViewHolder(View view) {
        return new CarrierViewHolder(view, item -> {
            if (listener != null) {
                CarriersAdapter.this.listener.onItemClick(item);
            }
        }, companyHelper);
    }

    static class CarrierViewHolder extends RecyclerViewBaseAdapter.BaseViewHolder<Partner> {
        @BindView(R.id.image_view_background)
        ImageView imageView;
        @BindView(R.id.text_view)
        TextView textView;
        CompanyHelper companyHelper;


        CarrierViewHolder(View itemView, RecyclerAdapterCallback<Partner> listener, CompanyHelper companyHelper) {
            super(itemView, listener);
            this.companyHelper = companyHelper;
        }

        @Override
        public void bind(Partner carrier) {
            super.bind(carrier);
            Picasso.get()
                    .load(companyHelper.getLogoUri(carrier, Company.LogoStyle.COLORED_24))
                    .into(imageView);
            textView.setText(carrier.name());
        }
    }
}
