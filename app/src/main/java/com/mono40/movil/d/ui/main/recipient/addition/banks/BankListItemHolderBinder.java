package com.mono40.movil.d.ui.main.recipient.addition.banks;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.mono40.movil.company.Company;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.company.bank.Bank;
import com.mono40.movil.d.ui.main.list.ListItemHolderBinder;
import com.mono40.movil.util.ObjectHelper;

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
