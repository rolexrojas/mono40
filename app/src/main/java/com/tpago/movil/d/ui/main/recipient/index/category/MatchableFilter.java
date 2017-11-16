package com.tpago.movil.d.ui.main.recipient.index.category;

import com.tpago.movil.d.domain.Matchable;
import rx.functions.Func1;

/**
 * @author Hector Vasquez
 */
final class MatchableFilter implements Func1<Matchable, Boolean> {

  static MatchableFilter create(String query) {
    return new MatchableFilter(query);
  }

  private final String query;

  private MatchableFilter(String query) {
    if (query != null && query.isEmpty()) {
      query = null;
    }
    this.query = query;
  }

  @Override
  public Boolean call(Matchable matchable) {
    return this.query == null || matchable.matches(this.query);
  }
}
