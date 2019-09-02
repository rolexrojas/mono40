package com.tpago.movil.d.ui.main.purchase;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.shapeofview.shapes.RoundRectView;
import com.squareup.picasso.Picasso;
import com.tpago.movil.R;
import com.tpago.movil.company.Company;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.d.domain.Banks;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductType;
import com.tpago.movil.d.ui.view.RecyclerViewBaseAdapter;
import com.tpago.movil.dep.api.ApiImageUriBuilder;
import com.tpago.movil.session.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class CardListAdapter extends RecyclerViewBaseAdapter<Product, CardListAdapter.CardListViewHolder> {
    private CompanyHelper companyHelper;
    private User user;

    public CardListAdapter() {
        super(new ArrayList<>(), null);
    }

    public CardListAdapter(List<Product> items, RecyclerAdapterCallback<Product> listener,
                           User user, CompanyHelper companyHelper) {
        super(items, listener);
        this.user = user;
        this.companyHelper = companyHelper;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.item_card;
    }

    @Override
    protected CardListViewHolder getViewHolder(View view) {
        return new CardListViewHolder(view, item -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        }, user, companyHelper);
    }

    public static class CardListViewHolder extends RecyclerViewBaseAdapter.BaseViewHolder<Product> {
        private final CompanyHelper companyHelper;
        @BindView(R.id.accountContainer)
        RoundRectView accountContainer;
        @BindView(R.id.accountAlias)
        TextView accountAlias;
        @BindView(R.id.accountBankName)
        TextView accountBankName;
        @BindView(R.id.accountOwnerName)
        TextView accountOwnerName;
        @BindView(R.id.accountType)
        TextView accountType;
        User user;
        @BindView(R.id.accountContainerImage)
        ImageView containerImageBackground;
        @BindView(R.id.bankLogo)
        ImageView bankLogo;
        private RecyclerAdapterCallback<Product> listener;

        public CardListViewHolder(View itemView, RecyclerAdapterCallback<Product> listener,
                                  User user, CompanyHelper companyHelper) {
            super(itemView, listener);
            this.user = user;
            this.listener = listener;
            this.companyHelper = companyHelper;
        }

        @Override
        public void bind(Product item) {
            super.bind(item);
            accountContainer.setBackgroundColor(Banks.getColor(item.getBank()));
            accountContainer.setBorderColor(Banks.getColor(item.getBank()));
            accountAlias.setText(item.getAlias());
            accountOwnerName.setText(user.name().toString());
            accountType.setText(ProductType.findStringId(item));
            accountBankName.setText(item.toProduct().bank().name());

            final Uri bankLogoUri = companyHelper.getLogoUri(item.toProduct().bank(), Company.LogoStyle.COLORED_24);
            Picasso.get()
                    .load(bankLogoUri)
                    .noFade()
                    .into(bankLogo);


            Context context = itemView.getContext();

            final Uri backgroundUri = ApiImageUriBuilder.build(context, item);
            Picasso.get()
                    .load(backgroundUri)
                    .transform(new RoundedCornersTransformation(
                            context.getResources()
                                    .getDimensionPixelOffset(R.dimen.commerce_payment_option_border_radius_extra),
                            0
                    )) // TODO: Remove once images are updated on the API.
                    .noFade()
                    .into(containerImageBackground);


            this.itemView.setOnClickListener(v -> this.listener.onItemClick(item));
        }
    }
}
