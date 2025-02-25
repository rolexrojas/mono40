package com.tpago.movil.d.domain;

import androidx.annotation.NonNull;

import java.util.List;

import rx.Observable;

/**
 * @author hecvasro
 */
@Deprecated
public interface RecipientRepo extends RecipientProvider {
  @NonNull Observable<Recipient> save(@NonNull Recipient recipient);
  @NonNull Observable<List<Recipient>> saveAll(@NonNull List<Recipient> recipients);
  Observable<Recipient> remove(Recipient recipient);
  void clear();
  void saveSync(Recipient recipient);
  boolean checkIfExists(Recipient recipient);
}
