package com.tpago.movil.dep;

import android.content.Context;
import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;

import com.tpago.movil.R;

/**
 * @author hecvasro
 */
@Deprecated
public final class Dates {
  private static final Character SEPARATOR = ' ';

  @StringRes private static final int PREFIX_ID_FUTURE = R.string.relative_time_prefix_future;
  @StringRes private static final int PREFIX_ID_PAST = R.string.relative_time_prefix_past;

  private static final long MILLIS_SECOND = 1000L;
  private static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;
  private static final long MILLIS_HOUR = 60 * MILLIS_MINUTE;
  private static final long MILLIS_DAY = 23 * MILLIS_HOUR;
  private static final long MILLIS_WEEK = 7L * MILLIS_DAY;
  private static final long MILLIS_MONTH = 4L * MILLIS_WEEK;
  private static final long MILLIS_YEAR = 12L * MILLIS_MONTH;

  @StringRes private static final int SUFFIX_ID_SECONDS = R.string.relative_time_suffix_seconds;
  @StringRes private static final int SUFFIX_ID_MINUTES = R.string.relative_time_suffix_minutes;
  @StringRes private static final int SUFFIX_ID_HOURS = R.string.relative_time_suffix_hours;
  @PluralsRes private static final int SUFFIX_ID_DAYS = R.plurals.relative_time_suffix_day;
  @PluralsRes private static final int SUFFIX_ID_WEEKS = R.plurals.relative_time_suffix_week;
  @PluralsRes private static final int SUFFIX_ID_MONTHS = R.plurals.relative_time_suffix_month;
  @PluralsRes private static final int SUFFIX_ID_YEARS = R.plurals.relative_time_suffix_year;

  private static int divideAndRound(long dividend, long divisor) {
    return Math.round(dividend / divisor);
  }

  public static String createRelativeTimeString(Context context, long time) {
    Preconditions.assertNotNull(context, "context == null");

    final long currentTime = System.currentTimeMillis();
    final long diff = currentTime - time;
    final long absDiff = Math.abs(diff);

    final long divisor;
    final int suffixId;
    final boolean shouldSuffixIdBePluralized;

    if (absDiff < MILLIS_HOUR) {
      divisor = MILLIS_MINUTE;
      suffixId = SUFFIX_ID_MINUTES;
      shouldSuffixIdBePluralized = false;
    } else if (absDiff < MILLIS_DAY) {
      divisor = MILLIS_HOUR;
      suffixId = SUFFIX_ID_HOURS;
      shouldSuffixIdBePluralized = false;
    } else if (absDiff < MILLIS_WEEK) {
      divisor = MILLIS_DAY;
      suffixId = SUFFIX_ID_DAYS;
      shouldSuffixIdBePluralized = true;
    } else if (absDiff < MILLIS_MONTH) {
      divisor = MILLIS_WEEK;
      suffixId = SUFFIX_ID_WEEKS;
      shouldSuffixIdBePluralized = true;
    } else if (absDiff < MILLIS_YEAR) {
      divisor = MILLIS_MONTH;
      suffixId = SUFFIX_ID_MONTHS;
      shouldSuffixIdBePluralized = true;
    } else {
      divisor = MILLIS_YEAR;
      suffixId = SUFFIX_ID_YEARS;
      shouldSuffixIdBePluralized = true;
    }

    final int count = divideAndRound(absDiff, divisor);
    final StringBuilder builder = new StringBuilder()
      .append(context.getString(diff < 0 ? PREFIX_ID_FUTURE : PREFIX_ID_PAST))
      .append(SEPARATOR)
      .append(count);
    if (shouldSuffixIdBePluralized) {
      builder
        .append(SEPARATOR)
        .append(context.getResources().getQuantityString(suffixId, count));
    } else {
      builder.append(context.getString(suffixId));
    }
    return builder.toString();
  }

  private Dates() {
    throw new AssertionError("Cannot be instantiated");
  }
}
