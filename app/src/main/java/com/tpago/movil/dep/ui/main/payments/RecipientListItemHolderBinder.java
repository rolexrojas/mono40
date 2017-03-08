package com.tpago.movil.dep.ui.main.payments;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.tpago.movil.dep.data.Formatter;
import com.tpago.movil.dep.data.res.DepAssetProvider;
import com.tpago.movil.dep.domain.BillBalance;
import com.tpago.movil.dep.domain.BillRecipient;
import com.tpago.movil.dep.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.dep.domain.RecipientType;
import com.tpago.movil.dep.misc.Utils;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.ui.main.list.ListItemHolderBinder;
import com.tpago.movil.graphics.CircleTransformation;
import com.tpago.movil.util.Objects;

/**
 * @author hecvasro
 */
class RecipientListItemHolderBinder implements ListItemHolderBinder<Recipient, RecipientListItemHolder> {
  private final DepAssetProvider assetProvider;

  RecipientListItemHolderBinder(DepAssetProvider assetProvider) {
    this.assetProvider = assetProvider;
  }

  private boolean deleting = false;

  boolean isDeleting() {
    return deleting;
  }

  void setDeleting(boolean deleting) {
    this.deleting = deleting;
  }

  @Override
  public void bind(@NonNull Recipient item, @NonNull RecipientListItemHolder holder) {
    final String label = item.getLabel();
    final String identifier = item.getIdentifier();
    final RecipientType type = item.getType();
    Uri imageUri = Uri.EMPTY;
    boolean shouldBeCropped = false;
    if (type.equals(RecipientType.NON_AFFILIATED_PHONE_NUMBER)) {
      imageUri = assetProvider.getLogoUri(
        ((NonAffiliatedPhoneNumberRecipient) item).getBank(),
        DepAssetProvider.STYLE_24_PRIMARY);
    } else if (type.equals(RecipientType.BILL)) {
      imageUri = assetProvider.getLogoUri(
        ((BillRecipient) item).getPartner(),
        DepAssetProvider.STYLE_24_PRIMARY);
    }
    if (!imageUri.equals(Uri.EMPTY)) {
      final RequestCreator creator = Picasso.with(holder.getContext())
        .load(imageUri);
      if (shouldBeCropped) {
        creator.transform(new CircleTransformation());
      }
      creator.into(holder.recipientPictureImageView);
    }
    if (Utils.isNotNull(label)) {
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
    if (isDeleting()) {
      holder.deleteCheckbox.setVisibility(View.VISIBLE);
      holder.deleteCheckbox.setChecked(item.isSelected());
      holder.proceedActionView.setVisibility(View.GONE);
    } else {
      holder.deleteCheckbox.setVisibility(View.GONE);
      holder.deleteCheckbox.setChecked(false);
      if (type.equals(RecipientType.NON_AFFILIATED_PHONE_NUMBER)) {
        holder.totalOwedPrefixableTextView.setVisibility(View.GONE);
        holder.dueDateTextView.setVisibility(View.GONE);
        holder.proceedActionView.setVisibility(View.VISIBLE);
      } else if (type.equals(RecipientType.BILL)) {
        holder.totalOwedPrefixableTextView.setVisibility(View.VISIBLE);
        holder.dueDateTextView.setVisibility(View.VISIBLE);
        holder.proceedActionView.setVisibility(View.GONE);
        final BillRecipient r = (BillRecipient) item;
        final BillBalance b = r.getBalance();
        if (Objects.isNotNull(b)) {
//          dueDate = new SimpleDateFormat("dd MMMM", new Locale("es", "DO"))
//            .format(b.getDate())
//            .toUpperCase();
          dueDate = b.getDate().trim();
          totalOwedCurrency = r.getCurrency();
          totalOwedValue = Formatter.amount(b.getTotal());
        }
      }
    }
    holder.dueDateTextView.setText(dueDate);
    holder.totalOwedPrefixableTextView.setPrefix(totalOwedCurrency);
    holder.totalOwedPrefixableTextView.setContent(totalOwedValue);
  }
}
