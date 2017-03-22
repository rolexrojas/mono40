package com.tpago.movil.d.ui.main.list;

import android.support.annotation.NonNull;

import com.tpago.movil.d.misc.Utils;

/**
 * TODO
 *
 * @author hecvasro
 */
public class NoResultsListItemItem {
  /**
   * TODO
   */
  private final String query;

  /**
   * TODO
   *
   * @param query
   *   TODO
   */
  public NoResultsListItemItem(@NonNull String query) {
    this.query = query;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final String getQuery() {
    return query;
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object) && object instanceof NoResultsListItemItem
      && ((NoResultsListItemItem) object).query.equals(query));
  }

  @Override
  public int hashCode() {
    return Utils.hashCode(NoResultsListItemItem.class.hashCode(), query);
  }

  @Override
  public String toString() {
    return NoResultsListItemItem.class.getSimpleName() + ":{query='" + query + "'}";
  }
}
