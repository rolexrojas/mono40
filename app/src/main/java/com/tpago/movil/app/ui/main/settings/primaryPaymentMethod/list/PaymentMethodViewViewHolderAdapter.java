package com.tpago.movil.app.ui.main.settings.primaryPaymentMethod.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.tpago.movil.R;
import com.tpago.movil.app.ui.main.settings.primaryPaymentMethod.PrimaryPaymentMethodFragment;
import com.tpago.movil.company.Company;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.util.ObjectHelper;

import java.util.List;

/**
 * Created by solucionesgbh on 6/4/18.
 */

public class PaymentMethodViewViewHolderAdapter extends RecyclerView.Adapter<PaymentMethodViewHolder>  {
    Context context;
    CompanyHelper companyHelper;
    ProductManager productManager;
    PrimaryPaymentMethodFragment fragment;

    public PaymentMethodViewViewHolderAdapter(Context context, ProductManager productManager,
                                              CompanyHelper companyHelper, PrimaryPaymentMethodFragment fragment) {
        this.context = context;
        this.companyHelper = companyHelper;
        this.productManager = productManager;
        this.fragment = fragment;
    }

    @Override
    public PaymentMethodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PaymentMethodViewHolder(
            LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_method_list_item, parent, false), productManager
        );
    }

    @Override
    public void onBindViewHolder(PaymentMethodViewHolder holder, int position) {
        if(ObjectHelper.isNotNull(holder)) {
            List<Product> productList = this.productManager.getPaymentOptionList();
            final Product product = productList.get(position);
            Picasso.get()
                .load(companyHelper.getLogoUri(product.getBank(), Company.LogoStyle.COLORED_24))
                .into(holder.icon);
            if(ObjectHelper.isNotNull(this.productManager.getDefaultPaymentOption()) && productList.get(position).getId().equals(this.productManager.getDefaultPaymentOption().getId())) {
                holder.indicator.setVisibility(View.VISIBLE);
            } else {
                holder.indicator.setVisibility(View.GONE);
            }
            holder.primaryTextView.setText(product.getAlias());
            holder.productPosition = position;
            holder.fragment = this.fragment;
        }
    }

    @Override
    public int getItemCount() {
        return this.productManager.getPaymentOptionList().size();
    }

}
