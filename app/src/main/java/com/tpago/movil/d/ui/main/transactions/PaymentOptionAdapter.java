package com.tpago.movil.d.ui.main.transactions;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.d.misc.Utils;
import com.tpago.movil.d.data.res.AssetProvider;
import com.tpago.movil.d.domain.Bank;
import com.tpago.movil.d.domain.Product;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;

/**
 * TODO
 *
 * @author hecvasro
 */
public class PaymentOptionAdapter extends ArrayAdapter<Product> {
  private final AssetProvider assetProvider;

  public PaymentOptionAdapter(@NonNull Context context,
    @NonNull AssetProvider assetProvider) {
    super(context, R.layout.list_item_payment_option, R.id.bank_name);
    this.assetProvider = assetProvider;
  }

  /**
   * TODO
   *
   * @param position
   *   TODO
   * @param convertView
   *   TODO
   * @param parent
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  private View getCustomView(int position, View convertView, @NonNull ViewGroup parent) {
    if (Utils.isNull(convertView)) {
      convertView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_payment_option, parent, false);
    }
    final Product product = getItem(position);
    if (Utils.isNotNull(product)) {
      final Bank bank = product.getBank();
      final ImageView imageView = ButterKnife.findById(convertView, R.id.bank_logo);
      Picasso.with(convertView.getContext())
        .load(assetProvider.getLogoUri(bank, AssetProvider.STYLE_20_GRAY))
        .into(imageView);
      final TextView textView = ButterKnife.findById(convertView, R.id.bank_name);
      textView.setText(bank.getName());
    }
    return convertView;
  }

  @Override
  @NonNull
  public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
    return getCustomView(position, convertView, parent);
  }

  @NonNull
  @Override
  public View getView(int position, View convertView, @NonNull ViewGroup parent) {
    return getCustomView(position, convertView, parent);
  }
}
