package com.tpago.movil.dep.ui.main.payments;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.tpago.movil.dep.data.Formatter;
import com.tpago.movil.dep.data.res.AssetProvider;
import com.tpago.movil.dep.domain.BillBalance;
import com.tpago.movil.dep.domain.BillRecipient;
import com.tpago.movil.dep.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.dep.domain.RecipientType;
import com.tpago.movil.dep.misc.Utils;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.ui.main.list.ListItemHolderBinder;
import com.tpago.movil.graphics.CircleTransformation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author hecvasro
 */
class RecipientListItemHolderBinder implements ListItemHolderBinder<Recipient, RecipientListItemHolder> {
  private final AssetProvider assetProvider;

  RecipientListItemHolderBinder(AssetProvider assetProvider) {
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
    holder.dueDateTextView.setText(null);
    holder.totalOwedPrefixableTextView.setPrefix(null);
    holder.totalOwedPrefixableTextView.setContent(null);
    final RecipientType type = item.getType();
    Uri imageUri = Uri.EMPTY;
    boolean shouldBeCropped = false;
    if (type.equals(RecipientType.NON_AFFILIATED_PHONE_NUMBER)) {
      holder.totalOwedPrefixableTextView.setVisibility(View.GONE);
      holder.dueDateTextView.setVisibility(View.GONE);
      holder.proceedActionView.setVisibility(View.VISIBLE);
      imageUri = assetProvider.getLogoUri(
        ((NonAffiliatedPhoneNumberRecipient) item).getBank(),
        AssetProvider.STYLE_24_PRIMARY);
    } else if (type.equals(RecipientType.BILL)) {
      holder.totalOwedPrefixableTextView.setVisibility(View.VISIBLE);
      holder.dueDateTextView.setVisibility(View.VISIBLE);
      holder.proceedActionView.setVisibility(View.GONE);
      final BillRecipient r = (BillRecipient) item;
      imageUri = assetProvider.getLogoUri(
        ((BillRecipient) item).getPartner(),
        AssetProvider.STYLE_24_PRIMARY);
      final BillBalance b = r.getBalance();
      holder.dueDateTextView.setText(new SimpleDateFormat("dd MMMM", new Locale("es", "DO"))
        .format(b.getDate())
        .toUpperCase());
      holder.totalOwedPrefixableTextView.setPrefix(r.getCurrency());
      holder.totalOwedPrefixableTextView.setContent(Formatter.amount(b.getTotal()));
    }
    if (!imageUri.equals(Uri.EMPTY)) {
      final RequestCreator creator = Picasso.with(holder.getContext())
        .load(imageUri);
      if (shouldBeCropped) {
        creator.transform(new CircleTransformation());
      }
      creator.into(holder.recipientPictureImageView);
    }
    if (isDeleting()) {
      holder.deleteCheckbox.setVisibility(View.VISIBLE);
      holder.deleteCheckbox.setChecked(item.isSelected());
      holder.proceedActionView.setVisibility(View.GONE);
    } else {
      holder.deleteCheckbox.setVisibility(View.GONE);
      holder.deleteCheckbox.setChecked(false);
      holder.proceedActionView.setVisibility(View.VISIBLE);
    }
  }
}
