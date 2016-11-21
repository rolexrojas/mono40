package com.gbh.movil.ui.main.list;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;

/**
 * TODO
 *
 * @author hecvasro
 */
public class NoResultsItem implements Item {
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
  public NoResultsItem(@NonNull String query) {
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
    return super.equals(object) || (Utils.isNotNull(object) && object instanceof NoResultsItem
      && ((NoResultsItem) object).query.equals(query));
  }

  @Override
  public int hashCode() {
    return Utils.hashCode(NoResultsItem.class.hashCode(), query);
  }

  @Override
  public String toString() {
    return NoResultsItem.class.getSimpleName() + ":{query='" + query + "'}";
  }
}
