package com.mono40.movil.d.ui.main.purchase;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mono40.movil.company.Company;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.company.bank.Bank;
import com.mono40.movil.d.domain.Banks;
import com.mono40.movil.R;
import com.mono40.movil.dep.User;
import com.mono40.movil.dep.api.ApiImageUriBuilder;
import com.mono40.movil.d.data.StringHelper;
import com.mono40.movil.d.data.util.Binder;
import com.mono40.movil.d.domain.Product;
import com.squareup.picasso.Picasso;
import com.mono40.movil.d.domain.ProductType;
import com.mono40.movil.util.ObjectHelper;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * @author hecvasro
 */
final class PurchasePaymentOptionBinder implements Binder<Product, PurchasePaymentOptionHolder> {

  private final User user;
  private final Context context;

  private final StringHelper stringHelper;

  private final CompanyHelper companyHelper;

  PurchasePaymentOptionBinder(
    User user,
    Context context,
    StringHelper stringHelper,
    CompanyHelper companyHelper
  ) {
    this.user = user;
    this.context = context;
    this.stringHelper = stringHelper;
    this.companyHelper = ObjectHelper.checkNotNull(companyHelper, "companyHelper");
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
      Picasso.get()
        .load(this.companyHelper.getLogoUri(bank, Company.LogoStyle.WHITE_36))
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
      backgroundColor = ContextCompat.getColor(context, R.color.transparent);
      backgroundImageView.setVisibility(View.VISIBLE);
      Picasso.get()
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
      productNumber = item.getNumberSanitized();
    } else {
      productNumber = item.getAlias();
    }
    productNumberTextView.setText(productNumber);

    final TextView ownerTextView = holder.getOwnerNameTextView();
    ownerTextView.setText(user.name());
  }
}
