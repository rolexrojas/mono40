package com.tpago.movil.d.ui.main.list;

import androidx.annotation.NonNull;

import com.tpago.movil.util.ObjectHelper;

import java.util.Arrays;

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
    return super.equals(object) || (ObjectHelper.isNotNull(object) && object instanceof NoResultsListItemItem
      && ((NoResultsListItemItem) object).query.equals(query));
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(
      Arrays.asList(NoResultsListItemItem.class.hashCode(), query)
        .toArray()
    );
  }

  @Override
  public String toString() {
    return NoResultsListItemItem.class.getSimpleName() + ":{query='" + query + "'}";
  }
}
