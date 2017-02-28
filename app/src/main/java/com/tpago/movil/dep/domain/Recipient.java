package com.tpago.movil.dep.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.dep.misc.Utils;
import com.tpago.movil.dep.domain.util.StringUtils;

import java.io.Serializable;
import java.util.List;

import rx.Observable;
import rx.functions.Func2;

/**
 * Abstract recipient representation.
 *
 * @author hecvasro
 */
public abstract class Recipient implements Serializable, Matchable {
  /**
   * Recipient's {@link RecipientType type}.
   */
  private final RecipientType type;

  /**
   * Recipient's label.
   */
  private String label;

  private boolean selected = false;

  /**
   * Constructs a new recipient.
   *
   * @param type
   *   Recipient's {@link RecipientType type}.
   * @param label
   *   Recipient's label.
   */
  protected Recipient(@NonNull RecipientType type, @Nullable String label) {
    this.type = type;
    this.label = label;
  }

  /**
   * Constructs a new recipient.
   *
   * @param type
   *   Recipient's {@link RecipientType type}.
   */
  protected Recipient(@NonNull RecipientType type) {
    this(type, null);
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public static Observable.Transformer<Recipient, List<Recipient>> toSortedListByIdentifier() {
    return new Observable.Transformer<Recipient, List<Recipient>>() {
      @Override
      public Observable<List<Recipient>> call(Observable<Recipient> observable) {
        return observable
          .toSortedList(new Func2<Recipient, Recipient, Integer>() {
            @Override
            public Integer call(Recipient a, Recipient b) {
              return a.getIdentifier().compareTo(b.getIdentifier());
            }
          });
      }
    };
  }

  /**
   * Gets the {@link RecipientType type} of the recipient.
   *
   * @return Recipient's {@link RecipientType type}.
   */
  @NonNull
  public final RecipientType getType() {
    return type;
  }

  /**
   * Gets the label of the recipient.
   *
   * @return Recipient's label.
   */
  @Nullable
  public String getLabel() {
    return label;
  }

  /**
   * Gets the identifier of the recipient.
   *
   * @return Recipient's identifier.
   */
  @NonNull
  public abstract String getIdentifier();

  /**
   * Sets the label of the recipient.
   *
   * @param label
   *   Recipient's label.
   */
  public void setLabel(@Nullable String label) {
    this.label = label;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  @Override
  public String toString() {
    return "Recipient:{type='" + type + "',label='" + label + "'}";
  }

  @Override
  public boolean matches(@Nullable String query) {
    return Utils.isNotNull(label) && StringUtils.matches(label, query);
  }
}
