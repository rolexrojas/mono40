package com.mono40.movil.d.ui.main.recipient.addition.partners;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.mono40.movil.company.Company;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.company.partner.Partner;
import com.mono40.movil.d.ui.main.list.ListItemHolderBinder;
import com.mono40.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
final class PartnerListItemHolderBinder
  implements ListItemHolderBinder<Partner, PartnerListItemHolder> {

  static PartnerListItemHolderBinder create(CompanyHelper companyHelper) {
    return new PartnerListItemHolderBinder(companyHelper);
  }

  private final CompanyHelper companyHelper;

  private PartnerListItemHolderBinder(CompanyHelper companyHelper) {
    this.companyHelper = ObjectHelper.checkNotNull(companyHelper, "companyHelper");
  }

  @Override
  public void bind(@NonNull Partner item, @NonNull PartnerListItemHolder holder) {
    Picasso.get()
      .load(this.companyHelper.getLogoUri(item, Company.LogoStyle.COLORED_24))
      .into(holder.imageView);
    holder.textView.setText(item.name());
  }
}
