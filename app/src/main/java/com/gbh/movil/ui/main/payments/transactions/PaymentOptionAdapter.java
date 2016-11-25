package com.gbh.movil.ui.main.payments.transactions;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gbh.movil.R;
import com.gbh.movil.Utils;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.domain.Product;

import butterknife.ButterKnife;

/**
 * TODO
 *
 * @author hecvasro
 */
public class PaymentOptionAdapter extends ArrayAdapter<Product> {
  public PaymentOptionAdapter(@NonNull Context context) {
    super(context, R.layout.list_item_payment_option, R.id.bank_name);
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
      // TODO: Load bank's logo.
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
