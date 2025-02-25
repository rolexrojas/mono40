package com.tpago.movil.d.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
public interface RecipientProvider {
  /**
   * Gets all the {@link Recipient recipients} registered in the provider.
   * <p>
   * By default the resulting {@link List list} is ordered alphabetically by its {@link
   * Recipient#getIdentifier() identifier}.
   *
   * @return All the {@link Recipient recipients} registered in the provider.
   */
  @NonNull
  Observable<List<Recipient>> getAll(@Nullable String query);
}
