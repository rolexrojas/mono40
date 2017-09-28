package com.tpago.movil.d.ui.main.recipient.index.category;

import static android.support.v4.content.ContextCompat.getDrawable;
import static com.tpago.movil.dep.Objects.checkIfNotNull;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.tpago.movil.R;
import com.tpago.movil.dep.api.ApiImageUriBuilder;
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
import com.tpago.movil.d.domain.Bank;
import com.tpago.movil.d.domain.LogoStyle;
import com.tpago.movil.dep.text.Texts;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
class RecipientListItemHolderBinder implements
  ListItemHolderBinder<Recipient, RecipientListItemHolder> {

  private final Category category;
  private final RecipientDrawableStore recipientDrawableStore = RecipientDrawableStore.create();

  private boolean deleting = false;

  public RecipientListItemHolderBinder(Category category) {
    this.category = category;
  }

  void setDeleting(boolean deleting) {
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
      if (checkIfNotNull(bank)) {
        imageUri = bank.getLogoUri(LogoStyle.PRIMARY_24);
      }
    } else if (type == RecipientType.BILL) {
      imageUri = ApiImageUriBuilder.build(
        context,
        ((BillRecipient) item).getPartner(),
        ApiImageUriBuilder.Style.PRIMARY_24);
    } else if (type == RecipientType.PRODUCT) {
      imageUri = ((ProductRecipient) item).getProduct()
        .getBank()
        .getLogoUri(LogoStyle.PRIMARY_24);
    } else if (type == RecipientType.USER) {
      final UserRecipient r = (UserRecipient) item;
      imageUri = r.pictureUri();
      if (imageUri == null || imageUri.equals(Uri.EMPTY)) {
        imageDrawable = getDrawable(context, this.recipientDrawableStore.get(r));
      }
    }
    if (checkIfNotNull(imageDrawable)) {
      holder.recipientPictureImageView.setImageDrawable(imageDrawable);
    } else if (checkIfNotNull(imageUri)) {
      Picasso.with(context)
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
          if (checkIfNotNull(b)) {
            dueDate = b.getDate();
            totalOwedCurrency = r.getCurrency();
            totalOwedValue = Formatter.amount(b.getTotal());
          }
        } else {
          final ProductRecipient r = (ProductRecipient) item;
          final ProductBillBalance b = r.getBalance();
          if (checkIfNotNull(b)) {
            dueDate = b.dueDate();
            totalOwedCurrency = DCurrencies.map(r.getProduct().getCurrency());
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
