package com.tpago.movil.d.ui.main.recipient.addition.partners;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.tpago.movil.company.partner.Partner;
import com.tpago.movil.company.partner.PartnerStore;
import com.tpago.movil.d.ui.main.recipient.addition.RecipientCandidateListPresenter;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

import io.reactivex.Observable;

/**
 * @author hecvasro
 */
final class PartnerListPresenter extends RecipientCandidateListPresenter {

  private final PartnerStore partnerStore;

  PartnerListPresenter(PartnerStore partnerStore) {
    this.partnerStore = ObjectHelper.checkNotNull(partnerStore, "partnerStore");
  }

  @Override
  protected boolean canStartListeningQueryChangeEvents() {
    return true;
  }

  private boolean isNameContained(Partner partner, String query) {
    final String name = partner.name()
      .toUpperCase();
    partner.name();
    Log.e("Partner", partner.name() +  partner.id());
    return (StringHelper.isNullOrEmpty(query) || name.contains(query.toUpperCase())) &&
        !(partner.id().toUpperCase().equals("PAL") || partner.id().toUpperCase().equals("BYA"));
  }

  @NonNull
  @Override
  protected Observable<Object> search(@Nullable final String query) {
    return this.partnerStore.getProviders()
      .flatMapObservable(Observable::fromIterable)
      .filter((partner) -> this.isNameContained(partner, query))
      .cast(Object.class);
  }
}
