package com.tpago.movil.d.ui.main.recipient.addition.partners;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.tpago.movil.company.Company;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.partner.Partner;
import com.tpago.movil.d.ui.main.list.ListItemHolderBinder;
import com.tpago.movil.util.ObjectHelper;

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
