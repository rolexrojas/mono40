package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
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
