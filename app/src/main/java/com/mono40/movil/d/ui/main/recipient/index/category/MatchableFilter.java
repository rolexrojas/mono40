package com.mono40.movil.d.ui.main.recipient.index.category;

import com.mono40.movil.d.domain.Matchable;
import com.mono40.movil.util.StringHelper;

import io.reactivex.functions.Predicate;

/**
 * @author Hector Vasquez
 */
final class MatchableFilter implements Predicate<Matchable> {

  static MatchableFilter create(String query) {
    return new MatchableFilter(query);
  }

  private final String query;

  private MatchableFilter(String query) {
    this.query = StringHelper.emptyIfNull(query);
  }

  @Override
  public boolean test(Matchable matchable) {
    return StringHelper.isNullOrEmpty(this.query) || matchable.matches(this.query);
  }
}
