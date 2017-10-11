package com.tpago.movil.d.ui.main.purchase;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpago.movil.d.domain.Banks;
import com.tpago.movil.R;
import com.tpago.movil.dep.User;
import com.tpago.movil.dep.api.ApiImageUriBuilder;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.data.util.Binder;
import com.tpago.movil.d.domain.Bank;
import com.tpago.movil.d.domain.Product;
import com.squareup.picasso.Picasso;
import com.tpago.movil.d.domain.ProductType;
import com.tpago.movil.d.domain.LogoStyle;
import com.tpago.movil.util.ObjectHelper;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * @author hecvasro
 */
final class PurchasePaymentOptionBinder implements Binder<Product, PurchasePaymentOptionHolder> {

  private final User user;
  private final Context context;

  private final StringHelper stringHelper;

  PurchasePaymentOptionBinder(
    User user,
    Context context,
    StringHelper stringHelper
  ) {
    this.user = user;
    this.context = context;
    this.stringHelper = stringHelper;
  }

  @Override
  public void bind(@NonNull Product item, @NonNull PurchasePaymentOptionHolder holder) {
    final boolean isCreditCard = Product.checkIfCreditCard(item);
    final Bank bank = item.getBank();
    final Uri backgroundUri = ApiImageUriBuilder.build(context, item);
    final boolean shouldShowGeneric = backgroundUri.equals(Uri.EMPTY);

    final int backgroundColor;
    final ImageView backgroundImageView = holder.getBackgroundImageView();
    final ImageView bankLogoImageView = holder.getBankLogoImageView();
    final ImageView issuerImageView = holder.getIssuerImageView();
    final TextView productTypeTextView = holder.getProductTypeTextView();
    if (shouldShowGeneric) {
      backgroundColor = Banks.getColor(bank);
      backgroundImageView.setVisibility(View.GONE);

      bankLogoImageView.setVisibility(View.VISIBLE);
      Picasso.with(context)
        .load(bank.getLogoUri(LogoStyle.WHITE_36))
        .noFade()
        .into(bankLogoImageView);

      if (isCreditCard) {
        issuerImageView.setVisibility(View.VISIBLE);
        // TODO: Load the Visa or MasterCard logo.
      } else {
        issuerImageView.setVisibility(View.GONE);
      }

      productTypeTextView.setVisibility(View.VISIBLE);
      productTypeTextView.setText(ProductType.findStringId(item));
    } else {
      backgroundColor = ContextCompat.getColor(context, R.color.common_transparent);
      backgroundImageView.setVisibility(View.VISIBLE);
      Picasso.with(context)
        .load(backgroundUri)
        .transform(new RoundedCornersTransformation(
          context.getResources()
            .getDimensionPixelOffset(R.dimen.commerce_payment_option_border_radius_extra),
          0
        )) // TODO: Remove once images are updated on the API.
        .noFade()
        .into(backgroundImageView);

      bankLogoImageView.setVisibility(View.GONE);

      issuerImageView.setVisibility(View.GONE);

      productTypeTextView.setVisibility(View.GONE);
    }

    final Drawable backgroundDrawable = holder.getRootViewBackground();
    if (ObjectHelper.isNotNull(backgroundDrawable)) {
      backgroundDrawable
        .mutate()
        .setColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN);
    }

    final TextView productNumberTextView = holder.getProductNumberTextView();
    final String productNumber;
    if (Product.checkIfCreditCard(item)) {
      productNumber = stringHelper.maskedProductNumber(item);
    } else {
      productNumber = item.getAlias();
    }
    productNumberTextView.setText(productNumber);

    final TextView ownerTextView = holder.getOwnerNameTextView();
    ownerTextView.setText(user.name());
  }
}
