package com.tpago.movil.d.ui.main.payments;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.tpago.movil.R;
import com.tpago.movil.api.ApiImageUriBuilder;
import com.tpago.movil.api.DCurrencies;
import com.tpago.movil.d.data.Formatter;
import com.tpago.movil.d.domain.BillBalance;
import com.tpago.movil.d.domain.BillRecipient;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.ProductBillBalance;
import com.tpago.movil.d.domain.ProductRecipient;
import com.tpago.movil.d.domain.RecipientType;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.UserRecipient;
import com.tpago.movil.d.ui.main.list.ListItemHolderBinder;
import com.tpago.movil.domain.LogoStyle;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Objects;

/**
 * @author hecvasro
 */
class RecipientListItemHolderBinder implements ListItemHolderBinder<Recipient, RecipientListItemHolder> {
  private boolean deleting = false;

  void setDeleting(boolean deleting) {
    this.deleting = deleting;
  }

  @Override
  public void bind(@NonNull Recipient item, @NonNull RecipientListItemHolder holder) {
    final String label = item.getLabel();
    final String identifier = item.getIdentifier();
    final RecipientType type = item.getType();
    Uri imageUri = Uri.EMPTY;
    final Context context = holder.getContext();
    if (type.equals(RecipientType.NON_AFFILIATED_PHONE_NUMBER)) {
      imageUri = ((NonAffiliatedPhoneNumberRecipient) item).getBank()
        .getLogoUri(LogoStyle.PRIMARY_24);
    } else if (type.equals(RecipientType.BILL)) {
      imageUri = ApiImageUriBuilder.build(
        context,
        ((BillRecipient) item).getPartner(),
        ApiImageUriBuilder.Style.PRIMARY_24);
    } else if (type.equals(RecipientType.PRODUCT)) {
      imageUri = ((ProductRecipient) item).getProduct()
        .getBank()
        .getLogoUri(LogoStyle.PRIMARY_24);
    } else if (type.equals(RecipientType.USER)) {
      imageUri = ((UserRecipient) item).pictureUri();
    }
    if (imageUri.equals(Uri.EMPTY)) {
      holder.recipientPictureImageView.setImageDrawable(null);
    } else {
      Picasso.with(context)
        .load(imageUri)
        .resizeDimen(R.dimen.icon_size_24, R.dimen.icon_size_24)
        .into(holder.recipientPictureImageView);
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
          if (Objects.checkIfNotNull(b)) {
            dueDate = b.getDate();
            totalOwedCurrency = r.getCurrency();
            totalOwedValue = Formatter.amount(b.getTotal());
          }
        } else {
          final ProductRecipient r = (ProductRecipient) item;
          final ProductBillBalance b = r.getBalance();
          if (Objects.checkIfNotNull(b)) {
            dueDate = b.dueDate();
            totalOwedCurrency = DCurrencies.map(r.getProduct().getCurrency());
            totalOwedValue = Formatter.amount(b.currentAmount());
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
