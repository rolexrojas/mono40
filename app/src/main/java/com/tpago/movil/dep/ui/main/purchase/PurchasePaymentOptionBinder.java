package com.tpago.movil.dep.ui.main.purchase;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.dep.data.res.DepAssetProvider;
import com.tpago.movil.dep.misc.Utils;
import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.data.util.Binder;
import com.tpago.movil.Bank;
import com.tpago.movil.dep.domain.Product;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * @author hecvasro
 */
final class PurchasePaymentOptionBinder implements Binder<Product, PurchasePaymentOptionHolder> {
  private final Context context;
  private final StringHelper stringHelper;
  private final DepAssetProvider assetProvider;

  PurchasePaymentOptionBinder(
    @NonNull Context context,
    @NonNull StringHelper stringHelper,
    @NonNull DepAssetProvider assetProvider) {
    this.context = context;
    this.stringHelper = stringHelper;
    this.assetProvider = assetProvider;
  }

  @Override
  public void bind(@NonNull Product item, @NonNull PurchasePaymentOptionHolder holder) {
    final Bank bank = item.getBank();
    final int textColor = assetProvider.getTextColor(bank);
    final Uri paymentOptionUri = assetProvider.getImageUri(item);
    if (paymentOptionUri.equals(Uri.EMPTY)) {
      final Drawable drawable = holder.getRootViewBackground();
      if (Utils.isNotNull(drawable)) {
        drawable.mutate()
          .setColorFilter(assetProvider.getPrimaryColor(bank), PorterDuff.Mode.SRC_IN);
      }
      holder.getImageView().setVisibility(View.GONE);
      holder.getBankLogoImageView().setEnabled(true);
      holder.getBankLogoImageView().setVisibility(View.VISIBLE);
      Picasso.with(context)
        .load(assetProvider.getLogoUri(bank, DepAssetProvider.STYLE_36_WHITE))
        .noFade()
        .into(holder.getBankLogoImageView());
      holder.getProductIdentifierTextView().setVisibility(View.VISIBLE);
    } else {
      final Drawable drawable = holder.getRootViewBackground();
      if (Utils.isNotNull(drawable)) {
        drawable.mutate()
          .setColorFilter(ContextCompat.getColor(context, R.color.transparent), PorterDuff.Mode.SRC_IN);
      }
      holder.getImageView().setVisibility(View.VISIBLE);
      holder.getBankLogoImageView().setEnabled(false);
      holder.getBankLogoImageView().setVisibility(View.INVISIBLE);
      Picasso.with(context)
        .load(paymentOptionUri)
        .transform(new RoundedCornersTransformation(context.getResources().getDimensionPixelOffset(R.dimen.commerce_payment_option_border_radius_extra), 0)) // TODO: Remove once images are updated on the API.
        .noFade()
        .into(holder.getImageView());
      holder.getProductIdentifierTextView().setVisibility(View.INVISIBLE);
    }
    holder.getProductIdentifierTextView().setText(item.getIdentifier());
    holder.getProductIdentifierTextView().setTextColor(textColor);
    final TextView productAliasTextView = holder.getProductNumberTextView();
    productAliasTextView.setText(stringHelper.maskedProductNumber(item));
    productAliasTextView.setTextColor(textColor);
  }
}
