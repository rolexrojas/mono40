package com.tpago.movil.d.ui.main.recipient.index.category;

import static android.support.v4.content.ContextCompat.getDrawable;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.tpago.movil.R;
import com.tpago.movil.company.Company;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.company.partner.Partner;
import com.tpago.movil.company.partner.PartnerStore;
import com.tpago.movil.dep.api.DCurrencies;
import com.tpago.movil.d.data.Formatter;
import com.tpago.movil.d.domain.BillBalance;
import com.tpago.movil.d.domain.BillRecipient;
import com.tpago.movil.d.domain.LoanBillBalance;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.PhoneNumberRecipient;
import com.tpago.movil.d.domain.ProductBillBalance;
import com.tpago.movil.d.domain.ProductRecipient;
import com.tpago.movil.d.domain.RecipientType;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.UserRecipient;
import com.tpago.movil.d.ui.main.list.ListItemHolderBinder;
import com.tpago.movil.dep.text.Texts;
import com.tpago.movil.paypal.PayPalAccountRecipient;
import com.tpago.movil.util.ObjectHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hecvasro
 */
class RecipientListItemHolderBinder implements
    ListItemHolderBinder<Recipient, RecipientListItemHolder> {

  private final Category category;
  private final RecipientDrawableStore recipientDrawableStore = RecipientDrawableStore.create();
  private final CompanyHelper companyHelper;
  private final PartnerStore partnerStore;

  private boolean deleting = false;

  public RecipientListItemHolderBinder(Category category, CompanyHelper companyHelper, PartnerStore partnerStore) {
    this.category = category;
    this.companyHelper = ObjectHelper.checkNotNull(companyHelper, "companyHelper");
    this.partnerStore = ObjectHelper.checkNotNull(partnerStore, "partnerStore");
    ;
  }

  final void setDeleting(boolean deleting) {
    this.deleting = deleting;
  }

  @Override
  public void bind(@NonNull Recipient item, @NonNull RecipientListItemHolder holder) {
    final String label = item.getLabel();
    final String identifier = item.getIdentifier();
    final RecipientType type = item.getType();
    final Context context = holder.getContext();

    Drawable imageDrawable = null;
    Uri imageUri = null;
    if (type == RecipientType.PHONE_NUMBER) {
      final PhoneNumberRecipient r = (PhoneNumberRecipient) item;
      imageDrawable = getDrawable(context, this.recipientDrawableStore.get(r));
    } else if (type == RecipientType.NON_AFFILIATED_PHONE_NUMBER) {
      final NonAffiliatedPhoneNumberRecipient r = (NonAffiliatedPhoneNumberRecipient) item;
      final Bank bank = r.getBank();
      final Partner carrier = r.getCarrier();
      if (ObjectHelper.isNotNull(bank)) {
        imageUri = this.companyHelper.getLogoUri(bank, Company.LogoStyle.COLORED_24);
      } else if (ObjectHelper.isNotNull(carrier)) {
        imageUri = this.companyHelper.getLogoUri(carrier, Company.LogoStyle.COLORED_24);
      }
    } else if (type == RecipientType.BILL) {
      Partner partner = ((BillRecipient) item).getPartner();
      imageUri = this.companyHelper.getLogoUri(partner, Company.LogoStyle.COLORED_24);
    } else if (type == RecipientType.PRODUCT) {
      final Bank bank = ((ProductRecipient) item).getProduct()
          .getBank();
      imageUri = this.companyHelper.getLogoUri(bank, Company.LogoStyle.COLORED_24);
    } else if (type == RecipientType.USER) {
      final UserRecipient r = (UserRecipient) item;
      imageUri = r.pictureUri();
      if (imageUri == null || imageUri.equals(Uri.EMPTY)) {
        imageDrawable = getDrawable(context, this.recipientDrawableStore.get(r));
      }
    } else if (type == RecipientType.PAY_PAL_ACCOUNT) {
      if (ObjectHelper.isNotNull(this.partnerStore)) {
        List<Partner> partners = this.partnerStore.getProviders().blockingGet();
        Partner partner = null;
        for (Partner p : partners) {
          if (p.id().toUpperCase().equals("PAL")) {
            partner = p;
          }
        }
        imageUri = this.companyHelper.getLogoUri(partner, Company.LogoStyle.COLORED_24);
      }
    }
    if (ObjectHelper.isNotNull(imageDrawable)) {
      holder.recipientPictureImageView.setImageDrawable(imageDrawable);
    } else if (ObjectHelper.isNotNull(imageUri)) {
      Picasso.get()
          .load(imageUri)
          .resizeDimen(R.dimen.icon_size_24, R.dimen.icon_size_24)
          .into(holder.recipientPictureImageView);
    } else {
      holder.recipientPictureImageView.setImageDrawable(null);
    }
    if (Texts.checkIfNotEmpty(label)) {
      holder.recipientLabelTextView.setText(label);
      holder.recipientLabelTextView.setGravity(Gravity.START | Gravity.BOTTOM);
      holder.recipientExtraTextView.setText(identifier);
      holder.recipientExtraTextView.setGravity(Gravity.START | Gravity.TOP);
      holder.recipientExtraTextView.setVisibility(View.VISIBLE);
    } else {
      holder.recipientLabelTextView.setText(identifier);
      holder.recipientLabelTextView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
      holder.recipientExtraTextView.setText(null);
      holder.recipientExtraTextView.setVisibility(View.GONE);
    }
    String dueDate = null;
    String totalOwedCurrency = null;
    String totalOwedValue = null;
    if (deleting) {
      holder.deleteCheckbox.setVisibility(View.VISIBLE);
      holder.deleteCheckbox.setChecked(item.isSelected());
      holder.proceedActionView.setVisibility(View.GONE);
    } else {
      holder.deleteCheckbox.setVisibility(View.GONE);
      holder.deleteCheckbox.setChecked(false);
      if (type.equals(RecipientType.BILL) || type.equals(RecipientType.PRODUCT)) {
        holder.totalOwedPrefixableTextView.setVisibility(View.VISIBLE);
        holder.dueDateTextView.setVisibility(View.VISIBLE);
        holder.proceedActionView.setVisibility(View.GONE);
        if (type.equals(RecipientType.BILL)) {
          final BillRecipient r = (BillRecipient) item;
          final BillBalance b = r.getBalance();
          if (ObjectHelper.isNotNull(b)) {
            dueDate = b.getDate();
            totalOwedCurrency = r.getCurrency();
            totalOwedValue = Formatter.amount(b.getTotal());
          }
        } else {
          final ProductRecipient r = (ProductRecipient) item;
          final ProductBillBalance b = r.getBalance();
          if (ObjectHelper.isNotNull(b)) {
            dueDate = b.dueDate();
            totalOwedCurrency = DCurrencies.map(r.getProduct()
                .getCurrency());
            final BigDecimal v;
            if (b instanceof LoanBillBalance) {
              v = b.periodAmount();
            } else {
              v = b.currentAmount();
            }
            totalOwedValue = Formatter.amount(v);
          }
        }
      } else {
        holder.totalOwedPrefixableTextView.setVisibility(View.GONE);
        holder.dueDateTextView.setVisibility(View.GONE);
        holder.proceedActionView.setVisibility(View.VISIBLE);
      }
    }
    holder.dueDateTextView.setText(dueDate);
    holder.totalOwedPrefixableTextView.setPrefix(totalOwedCurrency);
    holder.totalOwedPrefixableTextView.setContent(totalOwedValue);
  }
}
