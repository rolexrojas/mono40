package com.gbh.movil.ui.main.purchase;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.gbh.movil.Utils;
import com.gbh.movil.data.StringHelper;
import com.gbh.movil.data.res.AssetProvider;
import com.gbh.movil.data.util.Binder;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.domain.Product;
import com.squareup.picasso.Picasso;

/**
 * TODO
 *
 * @author hecvasro
 */
final class CommercePaymentOptionBinder implements Binder<Product, CommercePaymentOptionHolder> {
  private final Context context;
  private final StringHelper stringHelper;
  private final AssetProvider assetProvider;

  /**
   * TODO
   *
   * @param context
   *   TODO
   */
  CommercePaymentOptionBinder(@NonNull Context context, @NonNull StringHelper stringHelper,
    @NonNull AssetProvider assetProvider) {
    this.context = context;
    this.stringHelper = stringHelper;
    this.assetProvider = assetProvider;
  }

  @Override
  public void bind(@NonNull Product item, @NonNull CommercePaymentOptionHolder holder) {
    final Bank bank = item.getBank();
    final Drawable drawable = holder.getRootViewBackground();
    if (Utils.isNotNull(drawable)) {
      drawable.mutate().setColorFilter(assetProvider.getPrimaryColor(bank), PorterDuff.Mode.SRC_IN);
    }
    Picasso.with(context)
      .load(assetProvider.getLogoUri(bank, AssetProvider.STYLE_36_WHITE))
      .into(holder.getBankLogoImageView());
    final int textColor = assetProvider.getTextColor(bank);
    final TextView productIdentifierTextView = holder.getProductIdentifierTextView();
    productIdentifierTextView.setText(item.getIdentifier());
    productIdentifierTextView.setTextColor(textColor);
    final TextView productAliasTextView = holder.getProductNumberTextView();
    productAliasTextView.setText(stringHelper.maskedProductNumber(item));
    productAliasTextView.setTextColor(textColor);
    // TODO: Populate user's name.
  }
}
