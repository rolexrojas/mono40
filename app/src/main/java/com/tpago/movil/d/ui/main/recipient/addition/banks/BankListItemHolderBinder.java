package com.tpago.movil.d.ui.main.recipient.addition.banks;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.tpago.movil.company.Company;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.d.ui.main.list.ListItemHolderBinder;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
class BankListItemHolderBinder implements ListItemHolderBinder<Bank, BankListItemHolder> {

  static BankListItemHolderBinder create(CompanyHelper companyHelper) {
    return new BankListItemHolderBinder(companyHelper);
  }

  private final CompanyHelper companyHelper;

  private BankListItemHolderBinder(CompanyHelper companyHelper) {
    this.companyHelper = ObjectHelper.checkNotNull(companyHelper, "companyHelper");
  }

  @Override
  public void bind(@NonNull Bank item, @NonNull BankListItemHolder holder) {
    Picasso.get()
      .load(this.companyHelper.getLogoUri(item, Company.LogoStyle.COLORED_24))
      .into(holder.imageView);
    holder.textView.setText(item.name());
  }
}
